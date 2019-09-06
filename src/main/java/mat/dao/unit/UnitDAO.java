package mat.dao.unit;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.UnitDTO;
import mat.entity.Unit;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation.
 */
public interface UnitDAO extends IDAO<Unit, String> {
	
	/**
	 * Gets the all units.
	 * 
	 * @return the all units
	 */
	public List<UnitDTO> getAllUnits();
}
