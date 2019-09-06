package mat.service.library.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mat.properties.MATPropertiesService;
import mat.service.CQLConstantService;
import mat.cql.CQLConstantContainer;
import mat.service.code.impl.CodeListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.dto.DataTypeDTO;
import mat.dto.UnitDTO;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.cql.CQLKeywords;
import mat.service.measure.MeasureLibraryService;

@Service
public class CQLConstantServiceImpl implements CQLConstantService {
	@Autowired
	private CodeListServiceImpl codeListService;
	
	@Autowired
	private QDSAttributesDAO qDSAttributesDAO;
	
	@Autowired
	private MeasureLibraryService measureLibraryService;
	private final static String PLEASE_SELECT = "--Select--";

	@Override
	public CQLConstantContainer getAllCQLConstants() {
		final CQLConstantContainer cqlConstantContainer = new CQLConstantContainer(); 
		
		// get the unit dto list
		final List<UnitDTO> unitDTOList = codeListService.getAllUnits();
		cqlConstantContainer.setCqlUnitDTOList(unitDTOList);
		
		// get the unit map in the form of <UnitName, CQLUnit>
		final Map<String, String> unitMap = new LinkedHashMap<>(); 
		unitMap.put(PLEASE_SELECT, PLEASE_SELECT);
		for(final UnitDTO unit : unitDTOList) {
			unitMap.put(unit.getUnit(), unit.getCqlunit());
		}
		cqlConstantContainer.setCqlUnitMap(unitMap);
		
		final List<String> cqlAttributesList = qDSAttributesDAO.getAllAttributes();
		cqlConstantContainer.setCqlAttributeList(cqlAttributesList);
		
		// get the datatypes
		final List<DataTypeDTO> dataTypeListBoxList = (List<DataTypeDTO>) codeListService.getAllDataTypes();
		final List<String> datatypeList = new ArrayList<>();
		for(int i = 0; i < dataTypeListBoxList.size(); i++) {
			datatypeList.add(dataTypeListBoxList.get(i).getItem());
		}
				
		final List<String> qdmDatatypeList = new ArrayList<>(); 
		Collections.sort(datatypeList);
		qdmDatatypeList.addAll(datatypeList);
		datatypeList.remove("attribute");
		
		cqlConstantContainer.setCqlDatatypeList(datatypeList);
		cqlConstantContainer.setQdmDatatypeList(qdmDatatypeList);
		
		// get keywords
		final CQLKeywords keywordList = measureLibraryService.getCQLKeywordsLists(); 
		cqlConstantContainer.setCqlKeywordList(keywordList);
		
		// get timings
		final List<String> timings = keywordList.getCqlTimingList();
		Collections.sort(timings);
		cqlConstantContainer.setCqlTimingList(timings);
		
		cqlConstantContainer.setCurrentQDMVersion(MATPropertiesService.get().getQmdVersion());
		cqlConstantContainer.setCurrentReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
		
		return cqlConstantContainer;
		
	}

}
