package mat.dao.code.impl;

import mat.dao.code.CodeListDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.entity.CodeList;


@Repository("codeListDAO")
public class CodeListDAOImpl extends GenericDAO<CodeList, String> implements CodeListDAO {
	
	public CodeListDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
