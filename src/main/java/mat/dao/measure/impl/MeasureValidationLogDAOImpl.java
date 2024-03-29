package mat.dao.measure.impl;

import java.util.Date;

import mat.dao.measure.MeasureValidationLogDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.entity.MeasureValidationLog;
import mat.model.measure.Measure;
import mat.authentication.LoggedInUserUtil;

@Repository("measureValidationLogDAO")
public class MeasureValidationLogDAOImpl extends GenericDAO<MeasureValidationLog, String> implements MeasureValidationLogDAO {

	public MeasureValidationLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public boolean recordMeasureValidationEvent(Measure measure, String event, byte[] interimBarr) {
		Session session = null;
		boolean result = false;
		try {
			MeasureValidationLog measureValidationLog = new MeasureValidationLog();
			measureValidationLog.setActivityType(event);
			measureValidationLog.setTime(new Date());
			measureValidationLog.setMeasure(measure);
			measureValidationLog.setUserId(LoggedInUserUtil.getLoggedInUserEmailAddress());
			measureValidationLog.setInterimBarr(interimBarr);
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(measureValidationLog);			
			result = true;
		}
		catch (Exception e) { //TODO: handle application exception
			e.printStackTrace();
		}
    	return result;
	}

}
