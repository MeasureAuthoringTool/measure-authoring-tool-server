package mat.dao.library;

import java.util.List;

import mat.dao.IDAO;
import mat.model.cql.CQLLibraryAssociation;

public interface CQLLibraryAssociationDAO extends IDAO<CQLLibraryAssociation, String> {

	void deleteAssociation(CQLLibraryAssociation cqlLibraryAssociation);
	
	int findAssociationCount(String associatedWithId);

	List<CQLLibraryAssociation> getAssociations(String associatedWithId);

}
