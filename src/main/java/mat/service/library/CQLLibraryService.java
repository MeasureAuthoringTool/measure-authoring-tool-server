package mat.service.library;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.authentication.LoggedInUserUtil;
import mat.constants.CQLWorkSpaceConstants;
import mat.constants.ConstantMessages;
import mat.constants.MessageDelegate;
import mat.cql.CQLModelValidator;
import mat.cql.CQLUtil;
import mat.cql.CQLUtilityClass;
import mat.cql.LibHolderObject;
import mat.cql.result.GetUsedCQLArtifactsResult;
import mat.filesystem.ResourceLoader;
import mat.measure.MeasureUtility;
import mat.properties.MATPropertiesService;
import mat.result.SaveCQLLibraryResult;
import mat.result.SaveUpdateCQLResult;
import mat.service.UserService;
import mat.service.library.impl.CQLServiceImpl;
import mat.service.authentication.impl.LoginServiceImpl;
import mat.service.impl.VSACApiServImpl;
import mat.umls.VsacApiResult;
import mat.xml.XmlProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import mat.dao.audit.CQLLibraryAuditLogDAO;
import mat.dao.library.RecentCQLActivityLogDAO;
import mat.dao.user.UserDAO;
import mat.dao.library.CQLLibraryDAO;
import mat.dao.library.CQLLibraryExportDAO;
import mat.dao.library.CQLLibraryShareDAO;
import mat.dao.clause.ShareLevelDAO;
import mat.dto.CQLLibraryOwnerReportDTO;
import mat.model.valueset.CQLValueSetTransferObject;
import mat.model.LockedUserInfo;
import mat.model.code.MatCodeTransferObject;
import mat.entity.RecentCQLActivityLog;
import mat.entity.SecurityRole;
import mat.entity.User;
import mat.model.library.CQLLibrary;
import mat.model.library.CQLLibraryExport;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryAssociation;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLLibraryShareDTO;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.entity.MatUserDetails;
import mat.service.impl.MatContextServiceUtil;
import mat.service.impl.UserServiceImpl;
import mat.cql.error.InvalidLibraryException;
import mat.error.AuthenticationException;

@SuppressWarnings("serial")
@Service
public class CQLLibraryService implements CQLLibraryServiceInterface {
	private static final Logger logger = LogManager.getLogger(CQLLibraryService.class);
	
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CQLServiceImpl cqlService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private ShareLevelDAO shareLevelDAO;
	
	@Autowired
	private CQLLibraryShareDAO cqlLibraryShareDAO;
	
	@Autowired
	private CQLLibraryAuditLogDAO cqlLibraryAuditLogDAO;
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private RecentCQLActivityLogDAO recentCQLActivityLogDAO;
	
	@Autowired
	private CQLLibraryExportDAO cqlLibraryExportDAO;
	
	@Autowired
	private LoginServiceImpl loginService;
	
	javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	private final long lockThreshold = 3 * 60 * 1000; // 3 minutes
	
	private VSACApiServImpl getVsacService() {
		return (VSACApiServImpl) context.getBean("vsacapi");
	}

	@Override
	public SaveCQLLibraryResult searchForIncludes(String setId, String libraryName, String searchText){
        SaveCQLLibraryResult saveCQLLibraryResult = new SaveCQLLibraryResult();
        List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
        List<CQLLibrary> list = cqlLibraryDAO.searchForIncludes(setId, libraryName, searchText);
        saveCQLLibraryResult.setResultsTotal(list.size());
        for(CQLLibrary cqlLibrary : list){
               CQLLibraryDataSetObject object = extractCQLLibraryDataObject(cqlLibrary);
               allLibraries.add(object);
        }
        saveCQLLibraryResult.setCqlLibraryDataSetObjects(allLibraries);
        return saveCQLLibraryResult;
	}
	
	@Override
	public SaveCQLLibraryResult searchForReplaceLibraries(String setId) {
		SaveCQLLibraryResult saveCQLLibraryResult = new SaveCQLLibraryResult(); 
		
		List<CQLLibraryDataSetObject> dataSetObjects = new ArrayList<>(); 
		List<CQLLibrary> libraries = cqlLibraryDAO.searchForReplaceLibraries(setId); 
		for(CQLLibrary library : libraries) {
			CQLLibraryDataSetObject dataSetObject = extractCQLLibraryDataObject(library);
			dataSetObjects.add(dataSetObject);
		}
		
		saveCQLLibraryResult.setCqlLibraryDataSetObjects(dataSetObjects);
	
		return saveCQLLibraryResult;
		
	}
	
	@Override
	public SaveCQLLibraryResult search(String searchText, int filter, int startIndex,int pageSize) {
		SaveCQLLibraryResult searchModel = new SaveCQLLibraryResult();
		List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
		
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		List<CQLLibraryShareDTO> list = cqlLibraryDAO.search(searchText,Integer.MAX_VALUE, user,filter);
		
		searchModel.setResultsTotal(list.size());
		
		if (pageSize <= list.size()) {
			list = list
					.subList(startIndex - 1, pageSize);
		} else if (pageSize > list.size()) {
			list = list.subList(startIndex - 1,
					list.size());
		}
		
		for(CQLLibraryShareDTO dto : list){
			User userForShare = user;
			if(LoggedInUserUtil.getLoggedInUserRole().equalsIgnoreCase("Administrator")){
				userForShare = userDAO.find(dto.getOwnerUserId());
			}
			CQLLibraryDataSetObject object = extractCQLLibraryDataObjectFromShareDTO(userForShare, dto  );
			allLibraries.add(object);
		}
	
		updateCQLLibraryFamily(allLibraries);
		searchModel.setCqlLibraryDataSetObjects(allLibraries);
		
		return searchModel;
	}
	
	@Override
	public void save(CQLLibrary library) {
		library.setQdmVersion(MATPropertiesService.get().getQmdVersion());
		this.cqlLibraryDAO.save(library);
	}
	
	private CQLLibraryDataSetObject extractCQLLibraryDataObject(CQLLibrary cqlLibrary){
		
		CQLLibraryDataSetObject dataSetObject = new CQLLibraryDataSetObject();
		dataSetObject.setId(cqlLibrary.getId());
		dataSetObject.setCqlName(cqlLibrary.getName());
		dataSetObject.setDraft(cqlLibrary.isDraft());
		dataSetObject.setReleaseVersion(cqlLibrary.getReleaseVersion());
		dataSetObject.setQdmVersion(cqlLibrary.getQdmVersion());
		if(cqlLibrary.getRevisionNumber() == null){
			dataSetObject.setRevisionNumber("000");
		} else {
			dataSetObject.setRevisionNumber(cqlLibrary.getRevisionNumber());
		}
		dataSetObject.setFinalizedDate(cqlLibrary.getFinalizedDate());
		dataSetObject.setMeasureId(cqlLibrary.getMeasureId());
		boolean isLocked =isLocked(cqlLibrary.getLockedOutDate());
		dataSetObject.setLocked(isLocked);
		if (isLocked && (cqlLibrary.getLockedUserId() != null)) {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			lockedUserInfo.setUserId(cqlLibrary.getLockedUserId().getId());
			lockedUserInfo.setEmailAddress(cqlLibrary.getLockedUserId()
					.getEmailAddress());
			lockedUserInfo.setFirstName(cqlLibrary.getLockedUserId().getFirstName());
			lockedUserInfo.setLastName(cqlLibrary.getLockedUserId().getLastName());
			dataSetObject.setLockedUserInfo(lockedUserInfo);
		} else {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			dataSetObject.setLockedUserInfo(lockedUserInfo);
		}
		
		
		User user = userService.getById(cqlLibrary.getOwnerId().getId());
		dataSetObject.setOwnerFirstName(user.getFirstName());
		dataSetObject.setOwnerLastName(user.getLastName());
		dataSetObject.setOwnerEmailAddress(user.getEmailAddress());
		dataSetObject.setOwnerId(user.getId());
		dataSetObject.setCqlSetId(cqlLibrary.getSet_id());
		
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		boolean isOwner = currentUserId.equals(user.getId());
		dataSetObject.setSharable(isOwner || isSuperUser);
		dataSetObject.setDeletable(isOwner && cqlLibrary.isDraft());
		
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(cqlLibrary.getVersion(),
				cqlLibrary.getRevisionNumber(), cqlLibrary.isDraft());
		dataSetObject.setVersion(formattedVersion);        
		
		dataSetObject.setEditable(MatContextServiceUtil.get()
				.isCurrentCQLLibraryEditable(cqlLibraryDAO, cqlLibrary.getId()));
		
		List<CQLLibrary> libraryList = new ArrayList<>();
		libraryList.add(cqlLibrary);
		List<CQLLibrary> cqlLibraryFamily = cqlLibraryDAO.getAllLibrariesInSet(libraryList);
		caclulateVersionAndDraft(dataSetObject, cqlLibraryFamily);
		
		return dataSetObject;
		
	}
	
	private void caclulateVersionAndDraft(CQLLibraryDataSetObject dataSetObject, List<CQLLibrary> cqlLibraryFamily) {
		if(dataSetObject.isDraft()) {
			dataSetObject.setVersionable(dataSetObject.isDraft());
			dataSetObject.setDraftable(!dataSetObject.isDraft());
			return;
		}
		dataSetObject.setVersionable(false);
		boolean isDraftable = cqlLibraryFamily.stream().filter(measure -> measure.isDraft()).count() == 0;
		dataSetObject.setDraftable(isDraftable);
	}

	private boolean isLocked(Date lockedOutDate) {
		boolean locked = false;
		if (lockedOutDate == null) {
			return locked;
		}
		long currentTime = System.currentTimeMillis();
		long lockedOutTime = lockedOutDate.getTime();
		long timeDiff = currentTime - lockedOutTime;
		locked = timeDiff < lockThreshold;

		return locked;
	}

	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryId){
		try {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(cqlLibraryId);
			CQLLibraryDataSetObject cqlLibraryDataSetObject = extractCQLLibraryDataObject(cqlLibrary);
			cqlLibraryDataSetObject.setCqlText(getCQLLibraryData(cqlLibrary));
			return cqlLibraryDataSetObject; 
		} catch (ObjectNotFoundException e) {
			return null;
		}
	}
	
	@Override
	public SaveCQLLibraryResult saveDraftFromVersion(String libraryId){
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		CQLLibrary existingLibrary = cqlLibraryDAO.find(libraryId);
		boolean isDraftable = MatContextServiceUtil.get().isCurrentCQLLibraryDraftable(
				cqlLibraryDAO, libraryId);
		if(existingLibrary != null && isDraftable){
			CQLLibrary newLibraryObject = new CQLLibrary();
			newLibraryObject.setDraft(true);
			newLibraryObject.setName(existingLibrary.getName());
			newLibraryObject.setSet_id(existingLibrary.getSet_id());;
			newLibraryObject.setOwnerId(existingLibrary.getOwnerId());
			newLibraryObject.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
			newLibraryObject.setQdmVersion(MATPropertiesService.get().getQmdVersion());
			// Update QDM Version to latest QDM Version.
			String versionLibraryXml = getCQLLibraryXml(existingLibrary);
			if(versionLibraryXml != null){
				XmlProcessor processor = new XmlProcessor(getCQLLibraryXml(existingLibrary));
				try {
					MeasureUtility.updateLatestQDMVersion(processor);
					SaveUpdateCQLResult saveUpdateCQLResult = cqlService.getCQLLibraryData(versionLibraryXml);
					List<String> usedCodeList = saveUpdateCQLResult.getUsedCQLArtifacts().getUsedCQLcodes();
					processor.removeUnusedDefaultCodes(usedCodeList);
					processor.clearValuesetVersionAttribute();
					versionLibraryXml = processor.transform(processor.getOriginalDoc());
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			}
			
			newLibraryObject.setCQLByteArray(versionLibraryXml.getBytes());
			newLibraryObject.setVersion(existingLibrary.getVersion());
			newLibraryObject.setRevisionNumber("000");
			save(newLibraryObject);
			result.setSuccess(true);
			result.setId(newLibraryObject.getId());
			result.setCqlLibraryName(newLibraryObject.getName());
			String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(newLibraryObject.getVersion(),
					newLibraryObject.getRevisionNumber(), newLibraryObject.isDraft());
			result.setVersionStr(formattedVersion);
			result.setEditable(isDraftable);
			
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveCQLLibraryResult.INVALID_DATA);
		}
		return result;
	}
	
	@Override
	public SaveCQLLibraryResult saveFinalizedVersion(String libraryId,  boolean isMajor,
			 String version, boolean ignoreUnusedLibraries){
		logger.info("Inside saveFinalizedVersion: Start");
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		
		boolean isVersionable = MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId);
		
		if(!isVersionable){
			result.setSuccess(false);
			result.setFailureReason(ConstantMessages.INVALID_DATA);
			return result;
		}
		
		SaveUpdateCQLResult cqlResult  = getCQLData(libraryId);
		if(cqlResult.getCqlErrors().size() >0 || !cqlResult.isDatatypeUsedCorrectly()){
			result.setSuccess(false);
			result.setFailureReason(ConstantMessages.INVALID_CQL_DATA);
			return result;
		}
		
		List<String> usedLibraries = cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlLibraryXml = getCQLLibraryXml(cqlLibrary);
		if(CQLUtil.checkForUnusedIncludes(cqlLibraryXml, usedLibraries)){
			if(!ignoreUnusedLibraries) {
			    result.setSuccess(false);
			    result.setFailureReason(ConstantMessages.INVALID_CQL_LIBRARIES);
			    return result;
			} else {
		    	removeUnusedLibraries(cqlLibrary, cqlResult);
			}
	    }
		
		CQLLibrary library = cqlLibraryDAO.find(libraryId);
		if(library != null){
			String versionNumber = null;
			if (isMajor) {
				versionNumber = cqlLibraryDAO.findMaxVersion(library.getSet_id(), null);
				if (versionNumber == null) {
					versionNumber = "0.000";
				}
				logger.info("Max Version Number loaded from DB: " + versionNumber);
			} else {
				int versionIndex = version.indexOf('v');
				logger.info("Min Version number passed from Page Model: " + versionIndex);
				String selectedVersion = version.substring(versionIndex + 1);
				logger.info("Min Version number after trim: " + selectedVersion);
				versionNumber = cqlLibraryDAO.findMaxOfMinVersion(library.getSet_id(), selectedVersion);
			}
			
			int endIndex = versionNumber.indexOf('.');
			String majorVersionNumber = versionNumber.substring(0, endIndex);
			if (!versionNumber.equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_VERSION)) {
				String[] versionArr = versionNumber.split("\\.");
				if (isMajor) {
					if (!versionArr[0].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MAJOR_VERSION)) {
						logger.info("Inside saveFinalizedVersion: incrementVersionNumberAndSave Start");
						return incrementVersionNumberAndSave(majorVersionNumber, "1", library);
					} else {
						logger.info("Inside saveFinalizedVersion: returnFailureReason  isMajor Start");
						result.setFailureReason(SaveCQLLibraryResult.REACHED_MAXIMUM_MAJOR_VERSION);
						result.setSuccess(false);
						return result;
					}
				} else {
					if (!versionArr[1].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MINOR_VERSION)) {
						versionNumber = versionArr[0] + "." + versionArr[1];
						logger.info("Inside saveFinalizedVersion: incrementVersionNumberAndSave Start");
						return incrementVersionNumberAndSave(versionNumber, "0.001", library);
					} else {
						logger.info("Inside saveFinalizedVersion: returnFailureReason NOT isMajor Start");
						result.setFailureReason(SaveCQLLibraryResult.REACHED_MAXIMUM_MINOR_VERSION);
						result.setSuccess(false);
						return result;
					}
				}
			} else {
				logger.info("Inside saveFinalizedVersion: returnFailureReason MAX Major Minor Reached");
				result.setFailureReason(SaveCQLLibraryResult.REACHED_MAXIMUM_VERSION);
				result.setSuccess(false);
				return result;
				
			}
		} else {
			result.setFailureReason(SaveCQLLibraryResult.INVALID_DATA);
			result.setSuccess(false);
			return result;
		}	
	}
	
	public void saveCQLLibraryExport(CQLLibrary cqlLibrary, String cqlXML) {
		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLModelFromXML(cqlXML);
		HashMap<String, LibHolderObject> cqlLibNameMap =  new HashMap<>();
		Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap = new HashMap<CQLIncludeLibrary, CQLModel>();
		CQLUtil.getCQLIncludeMaps(cqlModel, cqlLibNameMap, cqlIncludeModelMap, cqlLibraryDAO);
		cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);
		cqlModel.setIncludedLibrarys(cqlIncludeModelMap);
		SaveUpdateCQLResult latestCQLResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, cqlModel.getExpressionListFromCqlModel(),true);
		CQLLibraryExport cqlLibraryExport = new CQLLibraryExport();
		cqlLibraryExport.setCqlLibrary(cqlLibrary);
		cqlLibraryExport.setCql(CQLUtilityClass.getCqlString(cqlModel, ""));
		cqlLibraryExport.setElm(latestCQLResult.getElmString());
		cqlLibraryExport.setJson(latestCQLResult.getJsonString());
		cqlLibraryExportDAO.save(cqlLibraryExport);
	}
	
	private void removeUnusedLibraries(CQLLibrary cqlLibrary, SaveUpdateCQLResult cqlResult) {
		String cqlLibraryXml = getCQLLibraryXml(cqlLibrary);
		XmlProcessor xmlProcessor = new XmlProcessor(cqlLibraryXml);

		try {
			CQLUtil.removeUnusedIncludes(xmlProcessor.getOriginalDoc(),
					cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries(), cqlResult.getCqlModel());
		} catch (XPathExpressionException e) {
			logger.error(e.getStackTrace());
		}

		cqlLibrary.setCQLByteArray(xmlProcessor.transform(xmlProcessor.getOriginalDoc()).getBytes());
		save(cqlLibrary);
		cqlLibraryDAO.refresh(cqlLibrary);
	}
	
	private SaveCQLLibraryResult incrementVersionNumberAndSave(String maximumVersionNumber, String incrementBy,
			 CQLLibrary library) {
		BigDecimal mVersion = new BigDecimal(maximumVersionNumber);
		mVersion = mVersion.add(new BigDecimal(incrementBy));
		library.setVersion(mVersion.toString());
		Date currentDate = new Date();
		long time = currentDate.getTime();
		Timestamp timestamp = new Timestamp(time);
		library.setFinalizedDate(timestamp);
		library.setDraft(false);
		
		String versionStr = mVersion.toString();
		// Divide the number by 1 and check for a remainder.
		// Any whole number should always have a remainder of 0 when divided by
		// 1.
		// For major versions, there may be case the minor version value is
		// zero.
		// THis makes the BigDecimal as Integer value causing issue while
		// formatVersionText method.
		// To fix that we are explicitly appending .0 in versionString.
		if (mVersion.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
			versionStr = versionStr.concat(".0");
		}
		// method to update version node to newly requested version value.
		XmlProcessor xmlProcessor = new XmlProcessor(getCQLLibraryXml(library));
		updateCQLVersion(xmlProcessor, library.getRevisionNumber(),versionStr);
		library.setCQLByteArray(xmlProcessor.transform(xmlProcessor.getOriginalDoc()).getBytes());
		cqlLibraryDAO.save(library);
		saveCQLLibraryExport(library,getCQLLibraryXml(library));
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setSuccess(true);
		result.setId(library.getId());
        versionStr = MeasureUtility.formatVersionText(versionStr);
		result.setVersionStr(versionStr);
		logger.info("Result passed for Version Number " + versionStr);
		return result;
	}
	
	
	private void updateCQLVersion(XmlProcessor processor,String revisionNumber ,String version) {
		String cqlVersionXPath = "//cqlLookUp/version";
		try {
			Node node = (Node) xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(),
					XPathConstants.NODE);
			if (node != null) {
				node.setTextContent(MeasureUtility.formatVersionText(revisionNumber, version));
			}
		} catch (XPathExpressionException e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlLibraryDataSetObject) {

		if (cqlLibraryDataSetObject != null) {
			cqlLibraryDataSetObject.scrubForMarkUp();
		}

		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		List<String> message = isValidCQLLibrary(cqlLibraryDataSetObject);
		if (message.size() == 0) {
			CQLLibrary library = new CQLLibrary();
			library.setDraft(true);
			library.setName(cqlLibraryDataSetObject.getCqlName());
			library.setSet_id(UUID.randomUUID().toString());
			library.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
			library.setQdmVersion(MATPropertiesService.get().getQmdVersion());
			library.setRevisionNumber("000");
			library.setVersion("0.0");
			if (LoggedInUserUtil.getLoggedInUser() != null) {
				User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
				library.setOwnerId(currentUser);
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveCQLLibraryResult.INVALID_USER);
				return result;
			}
			String cqlLookUpString = createCQLLookUpTag(cqlLibraryDataSetObject.getCqlName(),library.getVersion()+"."+library.getRevisionNumber());
			if (cqlLookUpString != null && !cqlLookUpString.isEmpty()) {
				byte[] cqlByteArray = cqlLookUpString.getBytes();
				library.setCQLByteArray(cqlByteArray);
				cqlLibraryDAO.save(library);
				result.setSuccess(true);
				result.setId(library.getId());
				result.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
				result.setVersionStr(library.getVersion()+"."+library.getRevisionNumber());
				result.setEditable(MatContextServiceUtil.get()
						.isCurrentCQLLibraryEditable(cqlLibraryDAO, library.getId()));
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveCQLLibraryResult.INVALID_CQL);
				return result;
			}
			return result;
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveCQLLibraryResult.INVALID_DATA);
			return result;
		}
	}

	private List<String> isValidCQLLibrary(CQLLibraryDataSetObject model) {

		List<String> message = new ArrayList<String>();

		if ((model.getCqlName() == null) || "".equals(model.getCqlName().trim())) {
			message.add(MessageDelegate.getInstance().getLibraryNameRequired());
		} else {
			CQLModelValidator cqlLibraryModel = new CQLModelValidator();
			boolean isValid = cqlLibraryModel.doesAliasNameFollowCQLAliasNamingConvention(model.getCqlName());
			if(!isValid){
				message.add(MessageDelegate.getInstance().getCqlStandAloneLibraryNameError());
			}
			
		}

		return message;
	}

	@Override
	public String createCQLLookUpTag(String libraryName, String version){
		XmlProcessor xmlProcessor = loadCQLXmlTemplateFile();
		String cqlLookUpString = getCQLLookUpXml(libraryName, version,xmlProcessor,"//standAlone");
		return cqlLookUpString;
	}

	@Override
	public String getCQLLookUpXml(String libraryName, String versionText, XmlProcessor xmlProcessor,String mainXPath) {
		String cqlLookUp = null;
		try {
			Node cqlTemplateNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/cqlTemplate");
			Node cqlLookUpNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), mainXPath+"/cqlLookUp");
			String xPath_ID = mainXPath+"/cqlLookUp/child::node()/*[@id]";
			String xPath_UUID = mainXPath+"/cqlLookUp/child::node()/*[@uuid]";
			if (cqlTemplateNode != null) {

				if (cqlTemplateNode.getAttributes().getNamedItem("changeAttribute") != null) {
					String[] attributeToBeModified = cqlTemplateNode.getAttributes().getNamedItem("changeAttribute")
							.getNodeValue().split(",");
					for (String changeAttribute : attributeToBeModified) {
						if (changeAttribute.equalsIgnoreCase("id")) {
							NodeList nodesForId = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPath_ID);
							for (int i = 0; i < nodesForId.getLength(); i++) {
								Node node = nodesForId.item(i);
								node.getAttributes().getNamedItem("id")
										.setNodeValue(UUID.randomUUID().toString());
							}
						} else if (changeAttribute.equalsIgnoreCase("uuid")) {
							NodeList nodesForUUId = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
									xPath_UUID);
							for (int i = 0; i < nodesForUUId.getLength(); i++) {
								Node node = nodesForUUId.item(i);
								node.getAttributes().getNamedItem("uuid")
										.setNodeValue(UUID.randomUUID().toString());
							}
						}
					}
				}

				if (cqlTemplateNode.getAttributes().getNamedItem("changeNodeTextContent") != null) {
					String[] nodeTextToBeModified = cqlTemplateNode.getAttributes()
							.getNamedItem("changeNodeTextContent").getNodeValue().split(",");
					for (String nodeTextToChange : nodeTextToBeModified) {
						if (nodeTextToChange.equalsIgnoreCase("library")) {
							Node libraryNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									mainXPath+"//" + nodeTextToChange);
							if (libraryNode != null) {
								libraryNode.setTextContent(libraryName);
							}
						} else if (nodeTextToChange.equalsIgnoreCase("version")) {
							Node versionNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									mainXPath+"//" + nodeTextToChange);
							if (versionNode != null) {
								versionNode.setTextContent(versionText);
							}
						} else if (nodeTextToChange.equalsIgnoreCase("usingModelVersion")) {
							Node usingModelVersionNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									mainXPath+"//" + nodeTextToChange);
							if (usingModelVersionNode != null) {
								usingModelVersionNode.setTextContent(MATPropertiesService.get().getQmdVersion());
							}
						}
					}
				}

			}
			System.out.println(xmlProcessor.transform(cqlLookUpNode));
			cqlLookUp = xmlProcessor.transform(cqlLookUpNode);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return cqlLookUp;
	}

	@Override
	public  XmlProcessor loadCQLXmlTemplateFile() {
		String fileName = "CQLXmlTemplate.xml";
		URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
		File templateFile = null;
		try {
			templateFile = new File(templateFileUrl.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		XmlProcessor templateXMLProcessor = new XmlProcessor(templateFile);
		return templateXMLProcessor;
	}
	
	private String getCQLLibraryData(CQLLibrary cqlLibrary){
		CQLModel cqlModel = new CQLModel();
		byte[] bdata;
		String cqlFileString = null;
		try {
			bdata = cqlLibrary.getCqlXML().getBytes(1, (int) cqlLibrary.getCqlXML().length());
			String data = new String(bdata);

			cqlModel = CQLUtilityClass.getCQLModelFromXML(data);

			cqlFileString = CQLUtilityClass.getCqlString(cqlModel,"").toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cqlFileString;
	}

	@Override
	public SaveUpdateCQLResult getCQLData(String id) {
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(id);
		String cqlLibraryXml = getCQLLibraryXml(cqlLibrary);
		
		if(cqlLibraryXml != null) {
			cqlResult = cqlService.getCQLData(cqlLibraryXml);
			cqlResult.setSetId(cqlLibrary.getSet_id());
			cqlResult.setSuccess(true);
		}
		
		return cqlResult;
	}
	
	@Override
	public SaveUpdateCQLResult getCQLDataForLoad(String id) {
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(id);
		String cqlLibraryXml = getCQLLibraryXml(cqlLibrary);
		
		if(cqlLibraryXml != null) {
			cqlResult = cqlService.getCQLDataForLoad(cqlLibraryXml);
			cqlResult.setSetId(cqlLibrary.getSet_id());
			cqlResult.setSuccess(true);
		}
		
		return cqlResult;
	}

	private String getCQLLibraryXml(CQLLibrary library){
		String xmlString = null;
		if(library != null ){
			try {
				xmlString = new String(library.getCqlXML().getBytes(1l, (int) library.getCqlXML().length()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return xmlString;
	}

	@Override
	public boolean isLibraryLocked(String id) {
		boolean isLocked = cqlLibraryDAO.isLibraryLocked(id);
		return isLocked;
	}

	@Override
	public SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId) {
		CQLLibrary existingLibrary = null;
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		if ((currentLibraryId != null) && (userId != null) && !currentLibraryId.isEmpty()) {
			existingLibrary = cqlLibraryDAO.find(currentLibraryId);
			if (existingLibrary != null) {
				if ((existingLibrary.getLockedUserId() != null) && existingLibrary.getLockedUserId().getId().equalsIgnoreCase(userId)) {
					// Only if the lockedUser and loggedIn User are same we can
					// allow the user to unlock the measure.
					if (existingLibrary.getLockedOutDate() != null) {
						// if it is not null then set it to null and save it.
						existingLibrary.setLockedOutDate(null);
						existingLibrary.setLockedUserId(null);
						cqlLibraryDAO.updateLockedOutDate(existingLibrary);
						result.setSuccess(true);
					}
				}
			}
			result.setId(existingLibrary.getId());
		}
		
		return result;
	}

	@Override
	public SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId) {
		CQLLibrary cqlLib = null;
		User user = null;
		
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		if ((currentLibraryId != null) && (userId != null)) {
			cqlLib = cqlLibraryDAO.find(currentLibraryId);
			if (cqlLib != null) {
				if (!cqlLibraryDAO.isLibraryLocked(cqlLib.getId())) {
					user = userService.getById(userId);
					cqlLib.setLockedUserId(user);
					cqlLib.setLockedOutDate(new Timestamp(new Date().getTime()));
					cqlLibraryDAO.save(cqlLib);
					result.setSuccess(true);
				}
			}
		}
		
		result.setId(cqlLib.getId());
		return result;
	}
	
	
	@Override
	public SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId) {
		
		ArrayList<RecentCQLActivityLog> recentLibActivityList = (ArrayList<RecentCQLActivityLog>) recentCQLActivityLogDAO.getRecentCQLLibraryActivityLog(userId);
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		List<CQLLibraryDataSetObject> cqlLibraryDataSetObjects = new ArrayList<CQLLibraryDataSetObject> ();
		for (RecentCQLActivityLog activityLog : recentLibActivityList) {
			CQLLibraryDataSetObject object = findCQLLibraryByID(activityLog.getCqlId());
			cqlLibraryDataSetObjects.add(object);
		}
		result.setCqlLibraryDataSetObjects(cqlLibraryDataSetObjects);
		result.setResultsTotal(cqlLibraryDataSetObjects.size());
		
		return result;
	}
	
	@Override
	public void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId){
		CQLLibrary library = cqlLibraryDAO.find(libraryid);
		if(library != null){
			recentCQLActivityLogDAO.recordRecentCQLLibraryActivity(libraryid, userId);
		}
	}

	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj,
			CQLParameter currentObj, List<CQLParameter> parameterList, boolean isFormatable) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.saveAndModifyParameters(cqlXml, toBeModifiedObj, currentObj, parameterList, isFormatable);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		return result;
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {

				result = cqlService.saveAndModifyDefinitions(cqlXml, toBeModifiedObj, currentObj, definitionList, isFormatable);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj,
			CQLFunctions currentObj, List<CQLFunctions> functionsList, boolean isFormatable) {
		
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.saveAndModifyFunctions(cqlXml, toBeModifiedObj, currentObj, functionsList, isFormatable);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		return result;
		
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryName, String libraryComment) {
		SaveUpdateCQLResult result = null;
		if(MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)){
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);
			if (cqlXml != null) {
				result = cqlService.saveAndModifyCQLGeneralInfo(cqlXml, libraryName, libraryComment);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setName(libraryName);
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		
		return result;
	}
	
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			if (cqlLibrary != null) {
				int associationCount = cqlService.countNumberOfAssociation(libraryId);
				if (associationCount < CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					String cqlXml = getCQLLibraryXml(cqlLibrary);
					if (cqlXml != null) {
						result = cqlService.saveAndModifyIncludeLibrayInCQLLookUp(cqlXml, toBeModifiedObj, currentObj, incLibraryList);
						if (result != null && result.isSuccess()) {
							cqlLibrary.setCQLByteArray(result.getXml().getBytes());
							cqlLibraryDAO.save(cqlLibrary);
							cqlService.saveCQLAssociation(currentObj, libraryId);
							if(toBeModifiedObj != null){
								cqlService.deleteCQLAssociation(toBeModifiedObj, cqlLibrary.getId());
							}
						}
					}
				}

			}
		}

		return result;
	}
	
	@Override
	public int countNumberOfAssociation(String Id){
		return cqlService.countNumberOfAssociation(Id);
	}
	
	@Override
	public List<CQLLibraryAssociation> getAssociations(String Id){
		return cqlService.getAssociations(Id);
	}

	@Override
	public SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj,
			List<CQLDefinition> definitionList) {
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {

			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.deleteDefinition(cqlXml, toBeDeletedObj, definitionList);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		return result;
	
	}
	
	@Override
	public SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, 
			List<CQLFunctions> functionsList) {
		
		SaveUpdateCQLResult result = null;
		
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.deleteFunctions(cqlXml, toBeDeletedObj, functionsList);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj, 
			List<CQLParameter> parameterList) {
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.deleteParameter(cqlXml, toBeDeletedObj, parameterList);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		return result;
	}

    @Override 
	public SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys) {
    	SaveUpdateCQLResult result = null;
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			if (cqlXml != null) {
				result = cqlService.deleteInclude(cqlXml, toBeModifiedIncludeObj, viewIncludeLibrarys);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);

					cqlService.deleteCQLAssociation(toBeModifiedIncludeObj, cqlLibrary.getId());
				}
			}
		}
		return result;
	}

    @Override
	public CQLKeywords getCQLKeywordsLists() {
		return cqlService.getCQLKeyWords();
	}

	@Override
	public GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId) {
		CQLLibrary library = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(library);
		return cqlService.getUsedCQlArtifacts(cqlXml);
	}

	@Override
	public SaveUpdateCQLResult getLibraryCQLFileData(String libraryId){
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		
		if (cqlXml != null && !StringUtils.isEmpty(cqlXml)) {
			result = cqlService.getCQLFileData(cqlXml);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@Override
	public SaveUpdateCQLResult getCQLLibraryFileData(String libraryId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		
		if (cqlXml != null && !StringUtils.isEmpty(cqlXml)) {
			result = cqlService.getCQLLibraryData(cqlXml);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		
		return result;		
	}

	public void updateCQLLibraryFamily(List<CQLLibraryDataSetObject> detailModelList) {
		boolean isFamily = false;
		if ((detailModelList != null) & (detailModelList.size() > 0)) {
			for (int i = 0; i < detailModelList.size(); i++) {
				if (i > 0) {
					if (detailModelList.get(i).getCqlSetId()
							.equalsIgnoreCase(detailModelList.get(i - 1).getCqlSetId())) {
						detailModelList.get(i).setFamily(!isFamily);
					} else {
						detailModelList.get(i).setFamily(isFamily);
					}
				} else {
					detailModelList.get(i).setFamily(isFamily);
				}
			}
		}
	}

	@Override
	public SaveCQLLibraryResult getUserShareInfo(String cqlId, String searchText){
		SaveCQLLibraryResult result =  new SaveCQLLibraryResult();
		List<CQLLibraryShareDTO> shareDTOList = cqlLibraryDAO
				.getLibraryShareInfoForLibrary(cqlId, searchText);
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(cqlId);
		result.setCqlLibraryShareDTOs(shareDTOList);
		result.setId(cqlId);
		result.setCqlLibraryName(cqlLibrary.getName());
		result.setResultsTotal(shareDTOList.size());
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, valueSetTransferObject.getCqlLibraryId())) {
			CQLLibrary library = cqlLibraryDAO.find(valueSetTransferObject.getCqlLibraryId());
			if (library != null) {
				result = cqlService.saveCQLValueset(valueSetTransferObject);
				if (result != null && result.isSuccess()) {
					String nodeName = "valueset";
					String parentNode = "//cqlLookUp/valuesets";
					appendAndSaveNode(library, nodeName, result.getXml(), parentNode);
				}
			}
		}
		return result;
	}
	
	private SaveUpdateCQLResult updateCodeSystem(String xml, CQLCode cqlCode) {
		CQLCodeSystem codeSystem = new CQLCodeSystem();
		codeSystem.setCodeSystem(cqlCode.getCodeSystemOID());
		codeSystem.setCodeSystemName(cqlCode.getCodeSystemName());
		codeSystem.setCodeSystemVersion(cqlCode.getCodeSystemVersion());
		SaveUpdateCQLResult result = cqlService.saveCQLCodeSystem(xml, codeSystem);
		return result;
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLCodestoCQLLibrary(MatCodeTransferObject transferObject) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, transferObject.getId())) {
			
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(transferObject.getId());
			if (cqlLibrary != null) {
				String cqlXml = getCQLLibraryXml(cqlLibrary);
				if(!cqlXml.isEmpty()){
					result = cqlService.saveCQLCodes(cqlXml,transferObject);
					if(result != null && result.isSuccess()) {
						String nodeName = "code";
						String parentNode = "//cqlLookUp/codes";
						String newXml= appendAndSaveNode(cqlLibrary, nodeName, result.getXml(), parentNode);
						cqlLibraryDAO.refresh(cqlLibrary);
						System.out.println("newXml ::: " + newXml);
						
						SaveUpdateCQLResult updatedResult = updateCodeSystem(newXml, transferObject.getCqlCode());
						if(updatedResult.isSuccess()) {
							newXml = saveCQLCodeSystemInLibrary(cqlLibrary, updatedResult);
							System.out.println("Updated newXml ::: " + newXml);
						}
						result.setCqlCodeList(getSortedCQLCodes(newXml).getCqlCodeList());
						CQLModel cqlModel = ((SaveUpdateCQLResult)cqlService.getCQLData(newXml)).getCqlModel();
						result.setCqlModel(cqlModel);
					}
				}
			}
		}
		return result;
	}

	
	@Override
	public SaveUpdateCQLResult saveCQLCodeListToCQLLibrary(List<CQLCode> codeList, String libraryId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		result.setSuccess(true);
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			if (cqlLibrary != null) {
				String cqlLibraryXml = getCQLLibraryXml(cqlLibrary);
				if(!cqlLibraryXml.isEmpty()){
					for(CQLCode cqlCode: codeList) {
						MatCodeTransferObject transferObject = new MatCodeTransferObject();
						transferObject.setCqlCode(cqlCode);
						transferObject.setId(libraryId);
						SaveUpdateCQLResult codeResult = cqlService.saveCQLCodes(cqlLibraryXml,transferObject);
						if(codeResult != null && codeResult.isSuccess()) {
							cqlLibraryXml = appendCQLCodeInLibrary(cqlLibraryXml, codeResult);
							SaveUpdateCQLResult updatedResult = updateCodeSystem(cqlLibraryXml, transferObject.getCqlCode());
							if(updatedResult.isSuccess()) {
								cqlLibraryXml = appendCQLCodeSystemInLibrary(cqlLibraryXml, updatedResult);
							}
							result.setXml(cqlLibraryXml);
						} else {
							result.setSuccess(false);
							break;
						}
					}
					
					if(result.isSuccess() && result.getXml() != null && !StringUtils.isEmpty(result.getXml())){
						cqlLibrary.setCQLByteArray(result.getXml().getBytes());
						save(cqlLibrary);
						result.setCqlCodeList(getSortedCQLCodes(result.getXml()).getCqlCodeList());
						CQLModel cqlModel = ((SaveUpdateCQLResult)cqlService.getCQLData(result.getXml())).getCqlModel();
						result.setCqlModel(cqlModel);
					}
				}
			}
		}

		return result;
	}
	
	@Override
	public SaveUpdateCQLResult modifyCQLCodeInCQLLibrary(CQLCode codeToReplace, CQLCode replacementCode,
			String cqlLibraryId) {
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, cqlLibraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(cqlLibraryId);
			if (cqlLibrary != null) {
				String cqlLibraryXml = getCQLLibraryXml(cqlLibrary);
				if(!cqlLibraryXml.isEmpty()){
					cqlResult = cqlService.deleteCode(getCQLLibraryXml(cqlLibrary), codeToReplace.getId());
					if (cqlResult != null && cqlResult.isSuccess()) {
						cqlLibraryXml = cqlResult.getXml();
						
						MatCodeTransferObject transferObject = new MatCodeTransferObject();
						transferObject.setCqlCode(replacementCode);
						transferObject.setId(cqlLibraryId);
						SaveUpdateCQLResult codeResult = cqlService.saveCQLCodes(cqlLibraryXml,transferObject);
						if(codeResult != null && codeResult.isSuccess()) {
							cqlLibraryXml = appendCQLCodeInLibrary(cqlLibraryXml, codeResult);
							SaveUpdateCQLResult updatedResult = updateCodeSystem(cqlLibraryXml, transferObject.getCqlCode());
							if(updatedResult.isSuccess()) {
								cqlLibraryXml = appendCQLCodeSystemInLibrary(cqlLibraryXml, updatedResult);
							}
							cqlResult.setXml(cqlLibraryXml);
							cqlLibrary.setCQLByteArray(cqlResult.getXml().getBytes());
							save(cqlLibrary);
							cqlResult.setCqlCodeList(getSortedCQLCodes(cqlResult.getXml()).getCqlCodeList());
							CQLModel cqlModel = ((SaveUpdateCQLResult)cqlService.getCQLData(cqlResult.getXml())).getCqlModel();
							cqlResult.setCqlModel(cqlModel);
						} else {
							cqlResult.setSuccess(false);
						}
					}
				}

			}
		}
		return cqlResult;
	}
	
	private String appendCQLCodeInLibrary(String cqlLibraryXml, SaveUpdateCQLResult codeResult) {
		String result = cqlLibraryXml;
		String nodeName = "code";
		String parentNode = "//cqlLookUp/codes";
		result = callAppendNode(cqlLibraryXml, codeResult.getXml(), nodeName, parentNode);
		System.out.println("newXml ::: " + result);
		return result;
	}

	private String appendCQLCodeSystemInLibrary(String cqlLibraryXml, SaveUpdateCQLResult updatedResult) {
		String result = cqlLibraryXml;
		String systemNode = "codeSystem";
		String systemParentNode = "//cqlLookUp/codeSystems";
		result = callAppendNode(cqlLibraryXml, updatedResult.getXml(), systemNode, systemParentNode);
		System.out.println("Updated newXml ::: " + result);
		return result;
	}

	private String saveCQLCodeSystemInLibrary(CQLLibrary cqlLibrary, SaveUpdateCQLResult updatedResult) {
		String nodeName = "codeSystem";
		String parentNode = "//cqlLookUp/codeSystems";
		 return appendAndSaveNode(cqlLibrary, nodeName, updatedResult.getXml(), parentNode);
	}
	
	
	private CQLCodeWrapper getSortedCQLCodes(String newXml) {
		CQLCodeWrapper cqlCodeWrapper = new CQLCodeWrapper();
		if(newXml != null && !newXml.isEmpty()){
			cqlCodeWrapper = cqlService.getCQLCodes(newXml);
		}
		
		return cqlCodeWrapper;
	}

	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject) {
		
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO,
				matValueSetTransferObject.getCqlLibraryId())) {
			CQLLibrary library = cqlLibraryDAO.find(matValueSetTransferObject.getCqlLibraryId());
			if (library != null) {
				result = cqlService.saveCQLUserDefinedValueset(matValueSetTransferObject);
				if (result != null && result.isSuccess()) {
					String nodeName = "valueset";
					String parentNode = "//cqlLookUp/valuesets";
					appendAndSaveNode(library, nodeName, result.getXml(), parentNode);
				}
			}
		}
		return result;
	}
	
	@Override
	public CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList , 
			List<CQLQualityDataSetDTO> appliedValueSetList , String cqlLibraryId) {
		
		StringBuilder finalXmlString = new StringBuilder("<valuesets>");
		SaveUpdateCQLResult finalResult = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, cqlLibraryId)) {
			for (CQLValueSetTransferObject  transferObject : transferObjectList) {
				SaveUpdateCQLResult result = null;
				transferObject.setAppliedQDMList(appliedValueSetList);
				if(transferObject.getCqlQualityDataSetDTO().getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
					result = cqlService.saveCQLUserDefinedValueset(transferObject);
				} else {
					result = cqlService.saveCQLValueset(transferObject);
				}
				
				if(result != null && result.isSuccess()) {
					if ((result.getXml() != null) && !StringUtils.isEmpty(result.getXml())) {
						finalXmlString = finalXmlString.append(result.getXml());
					}	
				}
			}
			
			finalXmlString.append("</valuesets>");
			finalResult.setXml(finalXmlString.toString());
			logger.info(finalXmlString);
			CQLLibrary library = cqlLibraryDAO.find(cqlLibraryId);
			if (library != null) {
				String nodeName = "valueset";
				String parentNode = "//cqlLookUp/valuesets";
				appendAndSaveNode(library, nodeName, finalResult.getXml(), parentNode);
				cqlLibraryDAO.refresh(library);
				List<CQLQualityDataSetDTO> cqlQualityDataSetDTOs = CQLUtilityClass
						.sortCQLQualityDataSetDto(getCQLData(cqlLibraryId).getCqlModel().getAllValueSetAndCodeList());
				wrapper.setQualityDataDTO(cqlQualityDataSetDTOs);
				
			}
		}
		
		
		return wrapper;
	}

	@Override
	public SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
		
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO,
				matValueSetTransferObject.getCqlLibraryId())) {
			CQLLibrary library = cqlLibraryDAO.find(matValueSetTransferObject.getCqlLibraryId());
			if (library != null) {
				result = cqlService.modifyCQLValueSets(matValueSetTransferObject);
				if (result != null && result.isSuccess()) {
					result = cqlService.updateCQLLookUpTag(getCQLLibraryXml(library), result.getCqlQualityDataSetDTO(),
							matValueSetTransferObject.getCqlQualityDataSetDTO());
					if (result != null && result.isSuccess()) {
						library.setCQLByteArray(result.getXml().getBytes());
						save(library);
					}
				}
			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId) {
		
		SaveUpdateCQLResult cqlResult = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary library = cqlLibraryDAO.find(libraryId);
			if (library != null) {
				cqlResult = cqlService.deleteValueSet(getCQLLibraryXml(library), toBeDelValueSetId);
				if (cqlResult != null && cqlResult.isSuccess()) {
					library.setCQLByteArray(cqlResult.getXml().getBytes());
					save(library);
				}
			}
		}
		return cqlResult;
	}
	
	
	@Override
	public SaveUpdateCQLResult deleteCode(String toBeDeletedId, String libraryId) {
		
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary library = cqlLibraryDAO.find(libraryId);
			if (library != null) {
				cqlResult = cqlService.deleteCode(getCQLLibraryXml(library), toBeDeletedId);
				if (cqlResult != null && cqlResult.isSuccess()) {
					library.setCQLByteArray(cqlResult.getXml().getBytes());
					save(library);
					cqlResult.setCqlCodeList(getSortedCQLCodes(cqlResult.getXml()).getCqlCodeList());
					
				}
			}
		}
		return cqlResult;
	}

	public final String appendAndSaveNode(CQLLibrary library, final String nodeName, String newXml, String parentNode) {
		String result = new String();
		if ((library != null && !StringUtils.isEmpty(getCQLLibraryXml(library)))
				&& (nodeName != null && StringUtils.isNotBlank(nodeName))) {
			result = callAppendNode(getCQLLibraryXml(library), newXml, nodeName, parentNode);
			library.setCQLByteArray(result.getBytes());
			save(library);
		}
		
		return result;
	}
	
	private String callAppendNode(String xml, String newXml, String nodeName,
			String parentNodeName) {
		XmlProcessor xmlProcessor = new XmlProcessor(xml);
		String result = null;
		try {
			result = xmlProcessor.appendNode(newXml, nodeName, parentNodeName);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private CQLLibraryDataSetObject extractCQLLibraryDataObjectFromShareDTO(final User user, final CQLLibraryShareDTO dto) {
		
		boolean isOwner = user.getId().equals(dto.getOwnerUserId());
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(user.getSecurityRole().getDescription());
		
		CQLLibraryDataSetObject dataObject = new CQLLibraryDataSetObject();
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(dto.getVersion(),
				dto.getRevisionNumber(), dto.isDraft());
		
		dataObject.setId(dto.getCqlLibraryId());
		dataObject.setCqlName(dto.getCqlLibraryName());
		dataObject.setVersion(formattedVersion);
		dataObject.setDraft(dto.isDraft());
		dataObject.setFinalizedDate(dto.getFinalizedDate());
		dataObject.setLocked(dto.isLocked());
		dataObject.setLockedUserInfo(dto.getLockedUserInfo());
		dataObject.setOwnerEmailAddress(user.getEmailAddress());
		dataObject.setOwnerFirstName(user.getFirstName());
		dataObject.setOwnerLastName(user.getLastName());
		dataObject.setSharable(isOwner || isSuperUser);
		dataObject.setDeletable(isOwner && dto.isDraft());
		dataObject.setCqlSetId(dto.getCqlLibrarySetId());
		dataObject.setEditable(MatContextServiceUtil.get().isCurrentCQLLibraryEditable(
				cqlLibraryDAO, dto.getCqlLibraryId()));
		dataObject.setDraftable(dto.isDraftable());
		dataObject.setVersionable(dto.isVersionable());
		return dataObject;
	}

	@Override
	public void updateUsersShare(final SaveCQLLibraryResult result) {
		StringBuilder auditLogAdditionlInfo = new StringBuilder("CQL Library shared with ");
		StringBuilder auditLogForModifyRemove = new StringBuilder("CQL Library shared status revoked with ");
		CQLLibraryShare cqlLibraryShare = null;
		boolean first = true;
		boolean firstRemove = true;
		boolean recordShareEvent = false;
		boolean recordRevokeShareEvent = false;
		List<CQLLibraryShareDTO> libraryShareDTO = result.getCqlLibraryShareDTOs();
		for (int i = 0; i < libraryShareDTO.size(); i++) {
			CQLLibraryShareDTO dto = libraryShareDTO.get(i);
			if ((dto.getShareLevel() != null) && !"".equals(dto.getShareLevel())) {
				User user = userDAO.find(dto.getUserId());
				ShareLevel sLevel = shareLevelDAO.find(dto.getShareLevel());
				cqlLibraryShare = null;
				for (CQLLibraryShare ms : user.getCqlLibraryShares()) {
					if (ms.getCqlLibrary().getId().equals(result.getId())) {
						cqlLibraryShare = ms;
						break;
					}
				}
				
				if ((cqlLibraryShare == null) && ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordShareEvent = true;
					cqlLibraryShare = new CQLLibraryShare();
					cqlLibraryShare.setCqlLibrary(cqlLibraryDAO.find(result.getId()));
					cqlLibraryShare.setShareUser(user);
					User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
					cqlLibraryShare.setOwner(currentUser);
					user.getCqlLibraryShares().add(cqlLibraryShare);
					currentUser.getOwnedCQLLibraryShares().add(cqlLibraryShare);
					logger.info("Sharing " + cqlLibraryShare.getCqlLibrary().getId() + " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					if (!first) { //first time, don't add the comma.
						auditLogAdditionlInfo.append(", ");
					}
					first = false;
					auditLogAdditionlInfo.append(user.getFirstName() +  " " + user.getLastName());
					
					cqlLibraryShare.setShareLevel(sLevel);
					cqlLibraryShareDAO.save(cqlLibraryShare);
				} else if (cqlLibraryShare != null && !ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordRevokeShareEvent = true;
					cqlLibraryShareDAO.delete(cqlLibraryShare.getId());
					logger.info("Removing Sharing " + cqlLibraryShare.getCqlLibrary().getId()
							+ " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					if (!firstRemove) { //first time, don't add the comma.
						auditLogForModifyRemove.append(", ");
					}
					firstRemove = false;
					auditLogForModifyRemove.append(user.getFirstName() +  " " + user.getLastName());
				}
			}
		}
		
		//US 170. Log share event
		if (recordShareEvent || recordRevokeShareEvent) {
			if (recordShareEvent && recordRevokeShareEvent) {
				auditLogAdditionlInfo.append("\n").append(auditLogForModifyRemove);
			} else if (recordRevokeShareEvent) {
				auditLogAdditionlInfo = new StringBuilder(auditLogForModifyRemove);
			}
			if(cqlLibraryShare != null && cqlLibraryShare.getCqlLibrary() != null) {
				cqlLibraryAuditLogDAO.recordCQLLibraryEvent(cqlLibraryShare.getCqlLibrary(),
						"CQL Library Shared", auditLogAdditionlInfo.toString());
			}
		}
	}


	
	public void updateCQLLookUpTagWithModifiedValueSet(CQLQualityDataSetDTO modifyWithDTO, CQLQualityDataSetDTO modifyDTO,
			String libraryId) {
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		if (cqlLibrary != null) {
			SaveUpdateCQLResult result = cqlService.updateCQLLookUpTag(getCQLLibraryXml(cqlLibrary), modifyWithDTO, modifyDTO);
			if(result != null && result.isSuccess()){
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
				cqlLibraryDAO.refresh(cqlLibrary);
			}
		}
		
	}
		
	@Override
	public VsacApiResult updateCQLVSACValueSets(String cqlLibraryId, String expansionId, String sessionId) {
		List<CQLQualityDataSetDTO> appliedQDMList = getCQLData(cqlLibraryId).getCqlModel().getAllValueSetAndCodeList();
		VsacApiResult result = getVsacService().updateCQLVSACValueSets(appliedQDMList, expansionId, sessionId);
		if(result.isSuccess()){
			updateAllCQLInLibraryXml(result.getCqlQualityDataSetMap(), cqlLibraryId);
		}
		return result;
	}

	/**
	 * Method to Iterate through Map of Quality Data set dto(modify With) as key and
	 * Quality Data Set dto (modifiable) as Value and update
	 * 
	 * @param map
	 *            - HaspMap
	 * @param libraryId
	 *            - String
	 */
	private void updateAllCQLInLibraryXml(HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO> map, String libraryId) {
		logger.info("Start VSACAPIServiceImpl updateAllInLibraryXml :");
		Iterator<Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO> entrySet = it.next();
			logger.info("Calling updateLibraryXML for : " + entrySet.getKey().getOid());
			updateCQLLookUpTagWithModifiedValueSet(entrySet.getKey(), entrySet.getValue(), libraryId);
			logger.info("Successfully updated Library XML for  : " + entrySet.getKey().getOid());
		}
		logger.info("End VSACAPIServiceImpl updateAllInLibraryXml :");
	}
	
	@Override
	public void transferLibraryOwnerShipToUser(final List<String> list, final String toEmail) {
		User userTo = userDAO.findByEmail(toEmail);
		
		for (int i = 0; i < list.size(); i++) {
			CQLLibrary library = cqlLibraryDAO.find(list.get(i));
			List<CQLLibrary> libraries = new ArrayList <CQLLibrary>();
			libraries.add(library);
			//Get All Family Libraries for each CQL Library
			List<CQLLibrary> allLibrariesInFamily = cqlLibraryDAO.getAllLibrariesInSet(libraries);
			for (int j = 0; j < allLibrariesInFamily.size(); j++) {
				String additionalInfo = "CQL Library Owner transferred from "
						+ allLibrariesInFamily.get(j).getOwnerId().getEmailAddress() + " to " + toEmail;
				allLibrariesInFamily.get(j).setOwnerId(userTo);
				this.save(allLibrariesInFamily.get(j));
				cqlLibraryAuditLogDAO.recordCQLLibraryEvent(allLibrariesInFamily.get(j), "CQL Library Ownership Changed", additionalInfo);
				additionalInfo = "";
				
			}
			List<CQLLibraryShare> cqlLibShareInfo = cqlLibraryDAO.getLibraryShareInforForLibrary(list.get(i));
			for (int k = 0; k < cqlLibShareInfo.size(); k++) {
				cqlLibShareInfo.get(k).setOwner(userTo);
				cqlLibraryShareDAO.save(cqlLibShareInfo.get(k));
			}
			
		}
		
	}
	
	@Override
    public List<CQLLibraryOwnerReportDTO> getCQLLibrariesForOwner() {
        Map<User, List<CQLLibrary>> map = new HashMap<>();
        List<User> nonAdminUserList = userService.getAllNonAdminActiveUsers();
        for(User user : nonAdminUserList) {
            List<CQLLibrary> libraryList = cqlLibraryDAO.getLibraryListForLibraryOwner(user);
            if((libraryList != null && libraryList.size() > 0)) {
                map.put(user,  libraryList);
            }
        }
 
        List<CQLLibraryOwnerReportDTO> cqlLibraryOwnerReports = populateCQLLibraryOwnerReport(map); 
        return cqlLibraryOwnerReports;
 
    }
	 
    private List<CQLLibraryOwnerReportDTO> populateCQLLibraryOwnerReport(Map<User, List<CQLLibrary>> map) {
        List<CQLLibraryOwnerReportDTO> cqlLibraryOwnerReports = new ArrayList<CQLLibraryOwnerReportDTO>();
        for(Entry<User, List<CQLLibrary>> entry : map.entrySet()) {
            User user = entry.getKey();
            List<CQLLibrary> libraries = entry.getValue();
            for(CQLLibrary cqlLibrary : libraries) {
                String cqlLibraryName = cqlLibrary.getName();
                String type = ""; 
                if(cqlLibrary.getMeasureId() == null) {
                    type = "Stand Alone"; 
                } else {
                    type = "Measure"; 
                }
                String status = ""; 
                if(cqlLibrary.isDraft()) {
                    status = "Draft";
                } else {
                    status = "Versioned"; 
                }
                String versionNumber = "v" + cqlLibrary.getVersionNumber(); 
                String id = cqlLibrary.getId();
                String setId = cqlLibrary.getSet_id();
                String firstName = user.getFirstName();
                String lastName = user.getLastName(); 
                String organization = user.getOrganization().getOrganizationName();
                CQLLibraryOwnerReportDTO cqlLibraryOwnerReportDTO = new CQLLibraryOwnerReportDTO(cqlLibraryName, type, status, versionNumber, id, setId, firstName, lastName, organization);
                cqlLibraryOwnerReports.add(cqlLibraryOwnerReportDTO);
            }
        }
        return cqlLibraryOwnerReports; 
 
    }
    
    @Override
    public final void deleteCQLLibrary(String cqllibId, String logggedInUserId, String password) throws AuthenticationException {
		logger.info("CQLLibraryService: delete cql library start : cqlLibId:: " + cqllibId);
		boolean isAuthenticated = loginService.isValidPassword(logggedInUserId, password);
		if(!isAuthenticated) {
			logger.error("CQL Libraray not deleted. " + MessageDelegate.getInstance().getMeasureDeletionInvalidPwd());
			throw new AuthenticationException(MessageDelegate.getInstance().getMeasureDeletionInvalidPwd());
		}

		CQLLibrary cqlLib = cqlLibraryDAO.find(cqllibId);
		MatUserDetails details = (MatUserDetails)  SecurityContextHolder.getContext().getAuthentication().getDetails();
		if (cqlLib.getOwnerId().getId().equalsIgnoreCase(details.getId())) {
			cqlLibraryDAO.delete(cqlLib);
		}

		logger.info("CQL Library Deleted Successfully :: " + cqllibId);
	}
}