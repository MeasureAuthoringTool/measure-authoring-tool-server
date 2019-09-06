package mat.dao.measure.impl;

import java.util.ArrayList;
import java.util.List;

import mat.dao.measure.MeasureScoreDAO;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dto.MeasureScoreDTO;
import mat.dao.search.GenericDAO;
import mat.entity.MeasureScore;


@Repository("measureScoreDAO")
public class MeasureScoreDAOImpl extends GenericDAO<MeasureScore, String> implements MeasureScoreDAO {
	
	private static final Logger logger = LogManager.getLogger(MeasureScoreDAOImpl.class);
	
	public MeasureScoreDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public List<MeasureScoreDTO> getAllMeasureScores(){
		
		List<MeasureScoreDTO> scoresList = new ArrayList<MeasureScoreDTO>();
		logger.info("Getting all the rows from the Measure Score table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<MeasureScore> msrScoreList = session.createCriteria(MeasureScore.class).list();
		for(MeasureScore msrScore: msrScoreList){
			MeasureScoreDTO scoreDTO =  new MeasureScoreDTO();			
			scoreDTO.setScore(msrScore.getScore());
			scoreDTO.setId(msrScore.getId());
			scoresList.add(scoreDTO);
		}
		return scoresList;
	}
}
