package mat.dao.measure;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.MeasureScoreDTO;
import mat.entity.MeasureScore;

/**
 * DAO interface for Measure Score table operation.
 */
public interface MeasureScoreDAO extends IDAO<MeasureScore, String> {
	
	/**
	 * Gets the all measure scores.
	 * 
	 * @return the all measure scores
	 */
	public List<MeasureScoreDTO> getAllMeasureScores();
}
