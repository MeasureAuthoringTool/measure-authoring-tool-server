package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dto.StewardDTO;
import mat.dao.StewardDAO;
import mat.dao.search.GenericDAO;
import mat.entity.MeasureSteward;


@Repository("stewardDAO")
public class StewardDAOImpl extends GenericDAO<MeasureSteward, String> implements StewardDAO {
	
	private static final Logger logger = LogManager.getLogger(StewardDAOImpl.class);
	
	public StewardDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public List<StewardDTO> getAllStewardOrg(){
		
		List<StewardDTO> StewardDTOList = new ArrayList<StewardDTO>();
		logger.info("Getting all the rows from the Steward table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<MeasureSteward> StewardList = session.createCriteria(MeasureSteward.class).list();
		for(MeasureSteward Steward: StewardList){
			StewardDTO StewardDTO =  new StewardDTO();
			StewardDTO.setOrgName(Steward.getOrgName());
			StewardDTO.setId(Steward.getId());
			StewardDTOList.add(StewardDTO);
		}
		return StewardDTOList;
	}
}
