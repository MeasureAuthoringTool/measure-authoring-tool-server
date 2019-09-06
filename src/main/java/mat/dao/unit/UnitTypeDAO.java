package mat.dao.unit;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.UnitTypeDTO;
import mat.entity.UnitType;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation.
 */
public interface UnitTypeDAO extends IDAO<UnitType, String> {
	
	/**
	 * Gets the all unit types.
	 * 
	 * @return the all unit types
	 */
	public List<UnitTypeDTO> getAllUnitTypes();
}
