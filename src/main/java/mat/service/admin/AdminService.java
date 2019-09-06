package mat.service.admin;

import mat.error.InCorrectUserRoleException;
import mat.model.admin.ManageOrganizationDetailModel;
import mat.model.admin.ManageOrganizationSearchModel;
import mat.model.admin.ManageOrganizationSearchModel.Result;
import mat.model.admin.ManageUsersDetailModel;
import mat.model.admin.ManageUsersSearchModel;

public interface AdminService {
	
	/**
	 * Delete user.
	 * 
	 * @param userId
	 *            the user id
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	public void deleteUser(String userId) throws InCorrectUserRoleException;
	
	/** Gets the organization.
	 * 
	 * @param key the key
	 * @return the organization */
	ManageOrganizationDetailModel getOrganization(String key);
	
	/**
	 * Gets the user.
	 * 
	 * @param key
	 *            the key
	 * @return the user
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	public ManageUsersDetailModel getUser(String key) throws InCorrectUserRoleException;
	
	/**
	 * Reset user password.
	 * 
	 * @param userid
	 *            the userid
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	public void resetUserPassword(String userid) throws InCorrectUserRoleException;
	
	/**update organization.
	 * 
	 * @param updatedModel the updated model
	 * @return the save update organization result */
	SaveUpdateOrganizationResult saveOrganization(ManageOrganizationDetailModel updatedOrganizationDetailModel);
	
	/** update organization.
	 * 
	 * @param currentModel the current model
	 * @param updatedModel the updated model
	 * @return the save update organization result */
	SaveUpdateOrganizationResult updateOrganization(Long currentOrganizationDetailId,
			ManageOrganizationDetailModel updatedOrganizationDetailModel);
	
	/**
	 * Save update user.
	 * 
	 * @param model
	 *            the model
	 * @return the save update user result
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model) throws InCorrectUserRoleException;
	
	/** Search organization.
	 * 
	 * @param key the key
	 * @return the manage organization search model */
	ManageOrganizationSearchModel searchOrganization(String key);
	
	/**
	 * Search users.
	 * 
	 * @param key
	 *            the key
	 * @return the manage users search model
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	public ManageUsersSearchModel searchUsers(String key) throws InCorrectUserRoleException;
	
	public ManageUsersSearchModel searchUsersWithActiveBonnie(String key) throws InCorrectUserRoleException;
	
	/** Gets the all organizations.
	 * 
	 * @return the all organizations */
	ManageOrganizationSearchModel getAllOrganizations();
	
	void deleteOrganization(Result organization) throws InCorrectUserRoleException;

	ManageUsersDetailModel getUserByEmail(String emailId)
			throws InCorrectUserRoleException;
}
