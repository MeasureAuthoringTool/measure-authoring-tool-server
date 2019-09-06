package mat.service.audit.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mat.dto.SearchHistoryDTO;
import mat.dao.code.CodeListAuditLogDAO;
import mat.dao.measure.MeasureAuditLogDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.measure.MeasureDAO;
import mat.entity.ListObject;
import mat.entity.QualityDataSet;
import mat.model.measure.Measure;
import mat.service.measure.MeasureAuditService;
import org.springframework.stereotype.Service;

@Service
public class MeasureAuditServiceImpl implements MeasureAuditService{
	
	/** The measure dao. */
	@Autowired
	private MeasureDAO measureDAO;

	/** The measure audit log dao. */
	@Autowired
	private MeasureAuditLogDAO measureAuditLogDAO;
	
	/** The code list audit log dao. */
	@Autowired
	private CodeListAuditLogDAO codeListAuditLogDAO; 
	
	/** The quality data set dao. */
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO; 
	
	/* Records the custom measure event to the MeasureAuditLog table.
	 * @see mat.service.measure.MeasureAuditService#recordMeasureEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see mat.service.measure.MeasureAuditService#recordMeasureEvent(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired){
		boolean result = false;
		Measure measure = measureDAO.find(measureId);
		result = measureAuditLogDAO.recordMeasureEvent(measure, event, additionalInfo);
		
		//check if the event 
		if(result && isChildLogRequired){
			//find out all the code list applied to this measure.
			List<QualityDataSet> qdmList = qualityDataSetDAO.getForMeasure(measureId);
			for(QualityDataSet qdm: qdmList){
				ListObject codeList = qdm.getListObject();
				result = result && codeListAuditLogDAO.recordCodeListEvent(codeList, event, codeList.getName());
			}
		}

		return result;
	}

	/* Search and returns the list of events starts with the start index and the given number of rows
	 * @see mat.service.measure.MeasureAuditService#executeSearch(java.lang.String, int, int)
	 */
	/* (non-Javadoc)
	 * @see mat.service.measure.MeasureAuditService#executeSearch(java.lang.String, int, int, java.util.List)
	 */
	@Override
	public SearchHistoryDTO executeSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList){
		return measureAuditLogDAO.searchHistory(measureId, startIndex, numberOfRows,filterList);
	}	
}
