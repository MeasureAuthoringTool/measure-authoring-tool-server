package mat.dao;

import java.util.List;

import mat.entity.ListObject;

/**
 * The Interface ListObjectDAO.
 */
public interface ListObjectDAO extends IDAO<ListObject, String> {

	List<ListObject> getSupplimentalCodeList();

	List<ListObject> getElementCodeListByOID(List<String> elementOIDList);
}
