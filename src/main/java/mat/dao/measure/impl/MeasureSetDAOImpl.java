package mat.dao.measure.impl;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.measure.MeasureSetDAO;
import mat.dao.search.GenericDAO;
import mat.model.measure.MeasureSet;

@Repository("measureSetDAO")
public class MeasureSetDAOImpl extends GenericDAO<MeasureSet, String> implements MeasureSetDAO {
	
	public MeasureSetDAOImpl (@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void saveMeasureSet(MeasureSet measureSet) {
		super.save(measureSet);
	}
	
	@Override
	public MeasureSet findMeasureSet(String measureSetId) {
		return getSessionFactory().getCurrentSession().find(MeasureSet.class, measureSetId);
	}

	
}
