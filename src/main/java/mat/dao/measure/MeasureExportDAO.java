package mat.dao.measure;

import mat.dao.IDAO;
import mat.model.measure.MeasureExport;

/**
 * The Interface MeasureExportDAO.
 */
public interface MeasureExportDAO extends IDAO<MeasureExport, String> {
	
	/**
	 * Find for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure export
	 */
	public MeasureExport findByMeasureId(String measureId);
}
