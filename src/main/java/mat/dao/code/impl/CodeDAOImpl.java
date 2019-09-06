package mat.dao.code.impl;

import mat.dao.code.CodeDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.entity.Code;

@Repository("codeDAO")
public class CodeDAOImpl extends GenericDAO<Code, String> implements CodeDAO {
	
	public CodeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

}
	
