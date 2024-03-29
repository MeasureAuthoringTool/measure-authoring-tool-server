package mat.dao.code;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.SearchHistoryDTO;
import mat.entity.CodeListAuditLog;
import mat.entity.ListObject;

/**
 * DAO Interface for CodeListAuditLog.
 */
public interface CodeListAuditLogDAO extends IDAO<CodeListAuditLog, String> {
	
	/**
	 * Record code list event.
	 * 
	 * @param codeList
	 *            the code list
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @return true, if successful
	 */
	public boolean recordCodeListEvent(ListObject codeList, String event, String additionalInfo);
	
	/**
	 * Search history.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param startIndex
	 *            the start index
	 * @param numberOfRows
	 *            the number of rows
	 * @param filterList
	 *            the filter list
	 * @return the search history dto
	 */
	public SearchHistoryDTO searchHistory(String codeListId, int startIndex, int numberOfRows, List<String> filterList);
}
