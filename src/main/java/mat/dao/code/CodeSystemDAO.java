package mat.dao.code;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.CodeSystemDTO;
import mat.entity.CodeSystem;

/**
 * The Interface CodeSystemDAO.
 */
public interface CodeSystemDAO extends IDAO<CodeSystem, String> {
	
	/**
	 * Gets the all code system.
	 * 
	 * @return the all code system
	 */
	public List<CodeSystemDTO> getAllCodeSystem();
}
