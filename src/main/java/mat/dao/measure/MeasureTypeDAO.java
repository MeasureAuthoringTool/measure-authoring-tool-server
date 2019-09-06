package mat.dao.measure;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.MeasureTypeDTO;
import mat.entity.MeasureType;

/**
 * The Interface MeasureTypeDAO.
 */
public interface MeasureTypeDAO extends IDAO<MeasureType, String> {
	
	/**
	 * Gets the all measure types.
	 * 
	 * @return the all measure types
	 */
	public List<MeasureTypeDTO> getAllMeasureTypes();
}
