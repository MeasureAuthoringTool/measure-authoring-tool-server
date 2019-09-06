package mat.dao.library;

import mat.dao.IDAO;
import mat.model.library.CQLLibraryExport;

public interface CQLLibraryExportDAO extends IDAO<CQLLibraryExport, String>{

	CQLLibraryExport findByLibraryId(String libraryId);
	
}
