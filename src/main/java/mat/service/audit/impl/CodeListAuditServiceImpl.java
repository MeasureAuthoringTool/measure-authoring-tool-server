package mat.service.audit.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mat.dto.SearchHistoryDTO;
import mat.dao.code.CodeListAuditLogDAO;
import mat.dao.ListObjectDAO;
import mat.entity.ListObject;
import mat.service.audit.CodeListAuditService;

/**
 * Service implementation for Code List Audit Service.
 */
public class CodeListAuditServiceImpl implements CodeListAuditService{
	
	/** The list object dao. */
	@Autowired
	private ListObjectDAO listObjectDAO;

	/** The code list audit log dao. */
	@Autowired
	private CodeListAuditLogDAO codeListAuditLogDAO;
	
	
	/* (non-Javadoc)Records the custom code list event to the CodeListAuditLog table.
	 * @see mat.service.audit.CodeListAuditService#recordCodeListEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo){
		ListObject codeList = listObjectDAO.find(codeListId);
		return codeListAuditLogDAO.recordCodeListEvent(codeList, event, additionalInfo);
	}
	
	
	/* Search and returns the list of events starts with the start index and the given number of rows
	 * @see mat.service.audit.CodeListAuditService#executeSearch(java.lang.String, int, int)
	 */
	/* (non-Javadoc)
	 * @see mat.service.audit.CodeListAuditService#executeSearch(java.lang.String, int, int, java.util.List)
	 */
	@Override
	public SearchHistoryDTO executeSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList){
		return codeListAuditLogDAO.searchHistory(codeListId, startIndex, numberOfRows,filterList);
	}
}
