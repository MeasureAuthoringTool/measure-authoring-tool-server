package mat.service.packager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.dao.measure.impl.MeasureDAOImpl;
import mat.dao.measure.impl.MeasureXMLDAOImpl;
import mat.entity.DataType;
import mat.entity.QualityDataSet;
import mat.entity.User;
import mat.measure.MeasureUtil;
import mat.model.measure.ManageMeasureShareModel;
import mat.model.measure.MeasureSearchModel;
import mat.model.measure.MeasureXmlModel;
import mat.model.valueset.MatValueSet;
import mat.result.CompositeMeasurePackageValidationResult;
import mat.result.ValidateMeasureResult;
import mat.validation.ValidationUtility;
import mat.validation.measure.CompositeMeasurePackageValidator;
import mat.xml.ExportSimpleXML;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mat.dao.audit.CQLLibraryAuditLogDAO;
import mat.dao.DataTypeDAO;
import mat.dao.measure.MeasureAuditLogDAO;
import mat.dao.OrganizationDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.StewardDAO;
import mat.dao.user.UserDAO;
import mat.dao.library.CQLLibraryDAO;
import mat.dao.library.CQLLibraryShareDAO;
import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.measure.MeasureDAO;
import mat.dao.measure.MeasureExportDAO;
import mat.dao.measure.MeasureSetDAO;
import mat.dao.measure.MeasureShareDAO;
import mat.dao.measure.MeasureXMLDAO;
import mat.dao.clause.PackagerDAO;
import mat.dao.clause.ShareLevelDAO;
import mat.model.library.CQLLibrary;
import mat.model.component.ComponentMeasure;
import mat.model.measure.Measure;
import mat.model.measure.MeasureExport;
import mat.model.measure.MeasureSet;
import mat.model.measure.MeasureShare;
import mat.model.measure.MeasureShareDTO;
import mat.model.measure.MeasureXML;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLModel;
import mat.cql.CQLUtilityClass;
import mat.authentication.LoggedInUserUtil;
import mat.export.ExportResult;
import mat.export.MeasureArtifactGenerator;
import mat.service.measure.MeasurePackageService;
import mat.service.SimpleEMeasureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeasurePackageServiceImpl implements MeasurePackageService {
	
	private static final Logger logger = LogManager.getLogger(MeasurePackageServiceImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DataTypeDAO dataTypeDAO;
	
	@Autowired
	private SimpleEMeasureService eMeasureService;
	
	@Autowired
	private MeasureAuditLogDAO measureAuditLogDAO;
	
	@Autowired
	private MeasureDAOImpl measureDAO;
	
	@Autowired
	private OrganizationDAO organizationDAO;
	
	@Autowired
	private MeasureExportDAO measureExportDAO;
	
	@Autowired
	private MeasureSetDAO measureSetDAO;
	
	@Autowired
	private MeasureShareDAO measureShareDAO;
	
	@Autowired
	private MeasureXMLDAOImpl measureXMLDAO;
	
	@Autowired
	private PackagerDAO packagerDAO;
	
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO;
	
	@Autowired
	private ShareLevelDAO shareLevelDAO;
	
	@Autowired
	private StewardDAO stewardDAO;
	
	@Autowired
	private UserDAO userDAO;
		
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	
	@Autowired
	private CQLLibraryShareDAO cqlLibraryShareDAO;

	@Autowired
	private CQLLibraryAuditLogDAO cqlLibraryAuditLogDAO;
	
	@Autowired
	private ComponentMeasuresDAO componentMeasuresDAO;
	
	@Autowired
	private CompositeMeasurePackageValidator compositeMeasurePackageValidator;
	
	private String currentReleaseVersion;
	
	private ValidationUtility validator = new ValidationUtility();

	@Override
	public int countUsersForMeasureShare() {
		return measureDAO.countUsersForMeasureShare();
	}

	@Override
	public void deleteExistingPackages(final String measureId) {
		packagerDAO.deleteAllPackages(measureId);
		
	}

	@Override
	public DataType findDataTypeForSupplimentalCodeList(final String dataTypeName , final String categoryId){
		return dataTypeDAO.findDataTypeForSupplimentalCodeList(dataTypeName, categoryId);
	}

	@Override
	public MeasureSet findMeasureSet(final String id) {
		return measureSetDAO.findMeasureSet(id);
	}

	@Override
	public String findOutMaximumVersionNumber(final String measureSetId) {
		return measureDAO.findMaxVersion(measureSetId);
	}

	@Override
	public String findOutVersionNumber(final String measureId, final String measureSetId) {
		return measureDAO.findMaxOfMinVersion(measureId, measureSetId);
	}
	
	private String generateSimpleXML(final Measure measure, MeasureXML measureXML, final List<MatValueSet> matValueSetList) throws Exception {
		String exportedXML = "";

		if (measure.getReleaseVersion() != null && MeasureUtil.getInstance().isCQLMeasure(measure.getReleaseVersion())) {
			final CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureXML.getMeasureXMLAsString());
			exportedXML = ExportSimpleXML.export(measureXML, measureDAO, organizationDAO, cqlLibraryDAO, cqlModel);
		} else {
			exportedXML = ExportSimpleXML.export(measureXML, measureDAO, organizationDAO);
		}

		return exportedXML; 
	}
	
	private MeasureExport generateExport(final String measureId, final List<MatValueSet> matValueSetList) throws Exception {
		final MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		final Measure measure = measureDAO.find(measureId);
		final String simpleXML = generateSimpleXML(measure, measureXML, matValueSetList);
		final ExportResult exportResult = eMeasureService.exportMeasureIntoSimpleXML(measure.getId(), simpleXML, matValueSetList);		

		MeasureExport export = measureExportDAO.findByMeasureId(measureId);
		if (export == null) {
			export = new MeasureExport();
			export.setMeasure(measure);
		}
		
		export.setSimpleXML(simpleXML);
		export.setCodeListBarr(exportResult.wkbkbarr);
		return export; 
	}
	
	@Override
	public void createPackageArtifacts(final String measureId, String releaseVersion, MeasureExport export) {
		export.setHqmf(MeasureArtifactGenerator.getHQMFArtifact(measureId, releaseVersion));
		export.setHumanReadable(MeasureArtifactGenerator.getHumanReadableArtifact(measureId, releaseVersion));
		export.setCql(MeasureArtifactGenerator.getCQLArtifact(measureId));
		export.setElm(MeasureArtifactGenerator.getELMArtifact(measureId));
		export.setJson(MeasureArtifactGenerator.getJSONArtifact(measureId));
		
		measureExportDAO.save(export);
	}

	@Override
	public Measure getById(final String id) {
		return measureDAO.find(id);
	}

	@Override
	public int getMaxEMeasureId() {
		return measureDAO.getMaxEMeasureId();
	}

	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(final String measureId) {
		final MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		if (measureXML != null) {
			final MeasureXmlModel exportModal = new MeasureXmlModel();
			exportModal.setMeasureId(measureXML.getMeasureId());
			exportModal.setMeausreExportId(measureXML.getId());
			exportModal.setXml(measureXML.getMeasureXMLAsString());
			return exportModal;
		}
		return null;
	}

	@Override
	public String getUniqueOid() {
		return qualityDataSetDAO.generateUniqueOid();
	}

	@Override
	public List<MeasureShareDTO> getUsersForShare(final String userName, final String measureId, final int startIndex, final int pageSize) {
		return measureDAO.getMeasureShareInfoForMeasure(userName, measureId, startIndex - 1, pageSize);
	}
	
	public ValidationUtility getValidator() {
		return validator;
	}

	@Override
	public boolean isMeasureLocked(final String id) {
		final boolean isLocked = measureDAO.isMeasureLocked(id);
		return isLocked;
	}

	@Override
	public void save(final Measure measurePackage) {
		if (measurePackage.getOwner() == null) {
			if (LoggedInUserUtil.getLoggedInUser() != null) {
				final User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
				measurePackage.setOwner(currentUser);
			}
		}
		
		measureDAO.save(measurePackage);
	}

	@Override
	public void save(final MeasureSet measureSet) {
		measureSetDAO.save(measureSet);
	}
	
	@Override
	public int saveAndReturnMaxEMeasureId(final Measure measure) {
		return measureDAO.saveandReturnMaxEMeasureId(measure);
	}

	@Override
	public void saveMeasureXml(final MeasureXmlModel measureXmlModel) {
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureXmlModel.getMeasureId());
		if (measureXML != null) {
			measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
		} else {
			measureXML = new MeasureXML();
			measureXML.setMeasureId(measureXmlModel.getMeasureId());
			measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
		}
				
		measureXMLDAO.save(measureXML);
	}

	@Override
	public void saveSupplimentalQDM(final QualityDataSet qds) {
		qualityDataSetDAO.save(qds);
	}

	@Override
	public List<MeasureShareDTO> searchForAdminWithFilter(String searchText,
			int startIndex, int numResults, int filter) {
		return measureDAO.getMeasureShareInfoForUserWithFilter(searchText, startIndex - 1, numResults, filter);
	}
	
	@Override
	public List<MeasureShareDTO> searchWithFilter(MeasureSearchModel measureSearchModel) {
		final User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measureDAO.getMeasureShareInfoForUserWithFilter(measureSearchModel, user);
	}

	@Override
	public List<Measure> getComponentMeasuresInfo(String measureId){
		final List<Measure> componentMeasures = new ArrayList<>();
		List<ComponentMeasure> components = new ArrayList<>();
		final Measure copositeMeasure = measureDAO.find(measureId);
		if(copositeMeasure != null) {
			components = copositeMeasure.getComponentMeasures();
		}
		
		for(final ComponentMeasure cm : components) {
			componentMeasures.add(measureDAO.find(cm.getComponentMeasure().getId()));
		}
		return componentMeasures;
	}

	public void setValidator(ValidationUtility validator) {
		this.validator = validator;
	}

	@Override
	public void transferMeasureOwnerShipToUser(final List<String> list, final String toEmail) {
		final User userTo = userDAO.findByEmail(toEmail);
		
		for (int i = 0; i < list.size(); i++) {
			final Measure measure = measureDAO.find(list.get(i));
			final List<Measure> ms = new ArrayList <>();
			ms.add(measure);
			//Get All Family Measures for each Measure
			final List<Measure> allMeasures = measureDAO.getAllMeasuresInSet(ms);
			for (int j = 0; j < allMeasures.size(); j++) {
				String additionalInfo = "Measure Owner transferred from "
						+ allMeasures.get(j).getOwner().getEmailAddress() + " to " + toEmail;
				transferAssociatedCQLLibraryOnwnerShipToUser(allMeasures.get(j).getId(), userTo, 
						allMeasures.get(j).getOwner().getEmailAddress());
				allMeasures.get(j).setOwner(userTo);
				measureDAO.saveMeasure(allMeasures.get(j));
				measureAuditLogDAO.recordMeasureEvent(allMeasures.get(j), "Measure Ownership Changed", additionalInfo);
				additionalInfo = "";
				
				
			}
			final List<MeasureShare> measureShareInfo = measureDAO.getMeasureShareForMeasure(list.get(i));
			for (int k = 0; k < measureShareInfo.size(); k++) {
				measureShareInfo.get(k).setOwner(userTo);
				measureShareDAO.save(measureShareInfo.get(k));
			}
			
		}
		
	}

	private void transferAssociatedCQLLibraryOnwnerShipToUser(String measureId, User user, String emailUser){
		final CQLLibrary cqlLibrary = cqlLibraryDAO.getLibraryByMeasureId(measureId);
		if(cqlLibrary != null){
			final String additionalInfo = "CQL Library Owner transferred from "
					+ emailUser + " to " + user.getEmailAddress();
			cqlLibrary.setOwnerId(user);
			cqlLibraryDAO.save(cqlLibrary);
			cqlLibraryAuditLogDAO.recordCQLLibraryEvent(cqlLibrary, "CQL Library Ownership Changed", additionalInfo);
			
			final List<CQLLibraryShare> cqlLibShareInfo = cqlLibraryDAO.getLibraryShareInforForLibrary(cqlLibrary.getId());
			for (int k = 0; k < cqlLibShareInfo.size(); k++) {
				cqlLibShareInfo.get(k).setOwner(user);
				cqlLibraryShareDAO.save(cqlLibShareInfo.get(k));
			}
		}
		
	}

	@Override
	public void updateLockedOutDate(final Measure m) {
		measureDAO.resetLockDate(m);
	}

	@Override
	public void updatePrivateColumnInMeasure(final String measureId, final boolean isPrivate){
		measureDAO.updatePrivateColumnInMeasure(measureId, isPrivate);
		measureAuditLogDAO.recordMeasureEvent(getById(measureId), isPrivate
				? "Measure Private " : "Measure Public", "");
	}

	@Override
	public void updateUsersShare(final ManageMeasureShareModel model) {
		StringBuilder auditLogAdditionlInfo = new StringBuilder("Measure shared with ");
		final StringBuilder auditLogForModifyRemove = new StringBuilder("Measure shared status revoked with ");
		MeasureShare measureShare = null;
		boolean first = true;
		boolean firstRemove = true;
		boolean recordShareEvent = false;
		boolean recordRevokeShareEvent = false;
		for (int i = 0; i < model.getNumberOfRows(); i++) {
			final MeasureShareDTO dto = model.get(i);
			if ((dto.getShareLevel() != null) && !"".equals(dto.getShareLevel())) {
				final User user = userDAO.find(dto.getUserId());
				final ShareLevel sLevel = shareLevelDAO.find(dto.getShareLevel());
				measureShare = null;
				for (final MeasureShare ms : user.getMeasureShares()) {
					if (ms.getMeasure().getId().equals(model.getMeasureId())) {
						measureShare = ms;
						break;
					}
				}
				
				if ((measureShare == null) && ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordShareEvent = true;
					measureShare = new MeasureShare();
					measureShare.setMeasure(measureDAO.find(model.getMeasureId()));
					measureShare.setShareUser(user);
					final User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
					measureShare.setOwner(currentUser);
					user.getMeasureShares().add(measureShare);
					currentUser.getOwnedMeasureShares().add(measureShare);
					logger.info("Sharing " + measureShare.getMeasure().getId() + " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					if (!first) { //first time, don't add the comma.
						auditLogAdditionlInfo.append(", ");
					}
					first = false;
					auditLogAdditionlInfo.append(user.getEmailAddress());
					
					measureShare.setShareLevel(sLevel);
					measureShareDAO.save(measureShare);
				} else if (measureShare != null && !ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordRevokeShareEvent = true;
					measureShareDAO.delete(measureShare.getId());
					logger.info("Removing Sharing " + measureShare.getMeasure().getId()
							+ " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					if (!firstRemove) { //first time, don't add the comma.
						auditLogForModifyRemove.append(", ");
					}
					firstRemove = false;
					auditLogForModifyRemove.append(user.getEmailAddress());
				}
			}
		}
		
		if (recordShareEvent || recordRevokeShareEvent) {
			if (recordShareEvent && recordRevokeShareEvent) {
				auditLogAdditionlInfo.append("\n").append(auditLogForModifyRemove);
			} else if (recordRevokeShareEvent) {
				auditLogAdditionlInfo = new StringBuilder(auditLogForModifyRemove);
			}
			if(measureShare != null && measureShare.getMeasure() != null) {
				measureAuditLogDAO.recordMeasureEvent(measureShare.getMeasure(),
						"Measure Shared", auditLogAdditionlInfo.toString());
			}
		}
	}
	
	@Override
	public ValidateMeasureResult validateExportsForCompositeMeasures(final String measureId) throws Exception {
		final MeasureExport export = generateExport(measureId, null);
		final ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(true);
		
		if(BooleanUtils.isTrue(export.getMeasure().getIsCompositeMeasure())) {
			final CompositeMeasurePackageValidationResult validationResult = compositeMeasurePackageValidator.validate(export.getSimpleXML());
			result.setValid(validationResult.getMessages().isEmpty());
			result.setMessages(validationResult.getMessages());
			result.setValidationMessages(validationResult.getMessages());
		}
		
		return result;
	}
	
	@Override
	public ValidateMeasureResult createExports(final String key, final List<MatValueSet> matValueSetsList, boolean shouldCreateArtifacts) throws Exception {
		final MeasureExport export = generateExport(key, matValueSetsList);
		final ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(true);
		createAndSaveExportsAndArtifacts(export, shouldCreateArtifacts);
		return result;
	}
	
	private void createAndSaveExportsAndArtifacts(MeasureExport export, boolean shouldCreateArtifacts) {
		final Measure measure = export.getMeasure();
		measure.setReleaseVersion(getCurrentReleaseVersion());
		measure.setExportedDate(new Date());
		measureDAO.save(measure);
		measureExportDAO.save(export);
		if (shouldCreateArtifacts) {
			createPackageArtifacts(measure.getId(), measure.getReleaseVersion(), export);
		}
	}
	

	@Override
	public String getHumanReadableForNode(final String measureId, final String populationSubXML){
		String humanReadableHTML = "";
		try {
			final ExportResult exportResult = eMeasureService.getHumanReadableForNode(measureId, populationSubXML);
			humanReadableHTML = exportResult.export;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return humanReadableHTML;
	}

	@Override
	public boolean getMeasure(String measureId) {
		return measureDAO.getMeasure(measureId);
	}

	public String getCurrentReleaseVersion() {
		return currentReleaseVersion;
	} 

	public void setCurrentReleaseVersion(String currentReleaseVersion) {
		this.currentReleaseVersion = currentReleaseVersion;
	}

	public CQLLibraryDAO getCqlLibraryDAO() {
		return cqlLibraryDAO;
	}

	public void setCqlLibraryDAO(CQLLibraryDAO cqlLibraryDAO) {
		this.cqlLibraryDAO = cqlLibraryDAO;
	}

	@Override
	public List<MeasureShareDTO> searchComponentMeasuresWithFilter(MeasureSearchModel measureSearchModel) {
		final User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measureDAO.getComponentMeasureShareInfoForUserWithFilter(measureSearchModel, user);
	}

	@Override
	public void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList) {
		componentMeasuresDAO.saveComponentMeasures(componentMeasuresList);
	}

	@Override
	public void updateComponentMeasures(String compositeMeasureId, List<ComponentMeasure> componentMeasuresList) {
		componentMeasuresDAO.updateComponentMeasures(compositeMeasureId, componentMeasuresList);		
	}
	
}