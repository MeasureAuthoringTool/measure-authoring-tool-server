package mat.dao.measure;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.SearchHistoryDTO;
import mat.entity.MeasureAuditLog;
import mat.model.measure.Measure;

/**
 * DAO Interface for MeasureAuditLog.
 */
public interface MeasureAuditLogDAO extends IDAO<MeasureAuditLog, String> {
	
	/**
	 * Record measure event.
	 * 
	 * @param measure
	 *            the measure
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @return true, if successful
	 */
	public boolean recordMeasureEvent(Measure measure, String event, String additionalInfo);
	
	/**
	 * Search history.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param numberOfRows
	 *            the number of rows
	 * @param filterList
	 *            the filter list
	 * @return the search history dto
	 */
	public SearchHistoryDTO searchHistory(String measureId, int startIndex, int numberOfRows,List<String> filterList);
}
