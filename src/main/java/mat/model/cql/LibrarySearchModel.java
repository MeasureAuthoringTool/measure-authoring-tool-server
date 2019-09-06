package mat.model.cql;

import mat.model.SearchModel;

public class LibrarySearchModel  extends SearchModel {
	public LibrarySearchModel() {
		this.searchTerm = "";
		this.versionType = VersionType.ALL;
		this.modifiedDate = 0;
		this.modifiedOwner = "";
		this.owner = "";
		this.startIndex = 1;
		this.pageSize = Integer.MAX_VALUE;
		this.isMyMeasureSearch = SearchModel.MY;
	}

	public LibrarySearchModel(int myMeasureSearch, int startIndex, int pageSize, String searchTerm) {
		this.isMyMeasureSearch = myMeasureSearch;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.searchTerm = searchTerm;
	}
}
