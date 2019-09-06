package mat.service.audit.impl;

import java.util.List;

import mat.dto.SearchHistoryDTO;
import mat.dto.UserAuditLogDTO;
import mat.service.audit.AuditService;
import mat.service.audit.CodeListAuditService;
import mat.service.audit.TransactionAuditService;
import mat.service.audit.UserAuditService;
import mat.service.library.CQLLibraryAuditService;
import mat.service.measure.MeasureAuditService;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class AuditServiceImpl implements AuditService {
	@Autowired MeasureAuditService measureAuditService;
	@Autowired
	CodeListAuditService codeListAuditService;
	@Autowired
	CQLLibraryAuditService cqlLibraryAuditService;
	@Autowired TransactionAuditService transactionAuditService;
	@Autowired UserAuditService userAuditService;
	/**
	 * Returns the spring bean MeasureAuditService.
	 * 
	 * @return MeasureAuditService
	 */
	private MeasureAuditService getMeasureAuditService(){
		return measureAuditService;
	}

	/**
	 * Returns the spring bean CodeListAuditService.
	 * 
	 * @return CodeListAuditService
	 */
	private CodeListAuditService getCodeListAuditService(){
		return codeListAuditService;
	}
	
	private CQLLibraryAuditService getCQLAuditService(){
		return cqlLibraryAuditService;
	}
	@Override
	public boolean recordCQLLibraryEvent(String cqlId, String event, String additionalInfo, boolean isChildLogRequired) {
		return getCQLAuditService().recordCQLLibraryEvent(cqlId, event, additionalInfo, isChildLogRequired);
	}
	@Override
	public SearchHistoryDTO executeSearch(String cqlId, int startIndex, int numberOfRows,List<String> filterList){
		return getCQLAuditService().executeSearch(cqlId, startIndex, numberOfRows, filterList);
	}
	
	/* Records an measure event
	 * @see mat.client.audit.service.AuditService#recordMeasureEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see mat.client.audit.service.AuditService#recordMeasureEvent(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired) {
		return getMeasureAuditService().recordMeasureEvent(measureId, event, additionalInfo, isChildLogRequired);
	}
	
	/* Records an code list event
	 * @see mat.client.audit.service.AuditService#recordCodeListEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see mat.client.audit.service.AuditService#recordCodeListEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo) {
		return getCodeListAuditService().recordCodeListEvent(codeListId, event, additionalInfo);
	}
	
	/* Returns the code list log search result for a given code list id
	 * @see mat.client.audit.service.AuditService#executeCodeListLogSearch(java.lang.String, int, int)
	 */
	/* (non-Javadoc)
	 * @see mat.client.audit.service.AuditService#executeCodeListLogSearch(java.lang.String, int, int, java.util.List)
	 */
	@Override
	public SearchHistoryDTO executeCodeListLogSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList){
		return getCodeListAuditService().executeSearch(codeListId, startIndex, numberOfRows,filterList);
	}
	
	
	
	/* Returns the measure log search result for a given code list id
	 * @see mat.client.audit.service.AuditService#executeMeasureLogSearch(java.lang.String, int, int)
	 */
	/* (non-Javadoc)
	 * @see mat.client.audit.service.AuditService#executeMeasureLogSearch(java.lang.String, int, int, java.util.List)
	 */
	@Override
	public SearchHistoryDTO executeMeasureLogSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList){
		return getMeasureAuditService().executeSearch(measureId, startIndex, numberOfRows,filterList);
	}

	/**
	 * Returns the spring bean TransactionAuditService.
	 * 
	 * @return TransactionService
	 */
	private TransactionAuditService getTransactionAuditService(){
		return transactionAuditService;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.audit.service.AuditService#recordTransactionEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean recordTransactionEvent(String primaryId, String secondaryId,
			String activityType, String userId, String additionalInfo, int logLevel) {
		return getTransactionAuditService().recordTransactionEvent(primaryId, secondaryId, activityType, userId, additionalInfo, logLevel);
	}

	/* (non-Javadoc)
	 * @see mat.client.audit.service.AuditService#recordMeasureEvent(java.util.List, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void recordMeasureEvent(List<String> measureIds, String event, String additionalInfo, boolean isChildLogRequired) {
		for(String measureId : measureIds) {
			recordMeasureEvent(measureId, event, additionalInfo, isChildLogRequired);
		}
	}
	
	/**
	 * Gets the user audit service.
	 *
	 * @return the user audit service
	 */
	public UserAuditService getUserAuditService(){
		return userAuditService;
	}
	
	@Override
	public boolean recordUserEvent(String userId, List<String> event, String additionalInfo, boolean isChildLogRequired) {
		return getUserAuditService().recordUserEvent(userId, event, additionalInfo, isChildLogRequired);
	}
	
	@Override
	public List<UserAuditLogDTO> executeUserLogSearch(String userId){
		return getUserAuditService().searchHistory(userId);
	}

}
