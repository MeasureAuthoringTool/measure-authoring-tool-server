package mat.dao.measure.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.measure.MeasureShareDAO;
import mat.dao.search.GenericDAO;
import mat.model.measure.MeasureShare;

@Repository("measureShareDAO")
public class MeasureShareDAOImpl extends GenericDAO<MeasureShare, String> implements MeasureShareDAO{
	public MeasureShareDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
