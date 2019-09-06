package mat.dao.library.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.library.CQLLibraryShareDAO;
import mat.dao.search.GenericDAO;
import mat.model.cql.CQLLibraryShare;

@Repository("cqlLibraryShareDAO")
public class CQLLibraryShareDAOImpl extends GenericDAO<CQLLibraryShare, String> implements CQLLibraryShareDAO{

	public CQLLibraryShareDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
