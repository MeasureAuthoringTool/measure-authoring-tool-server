package mat.api.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mat.api.organizations.OrganizationDTO;
import mat.authentication.LoggedInUserUtil;
import mat.dao.user.UserDAO;
import mat.entity.User;
import mat.service.impl.UserServiceImpl;

@RestController
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private UserDAO userDAO;

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@GetMapping(value = "/api/user", produces = "application/json")
	public ResponseEntity<UserDTO> getCurrentLoggedInUserInfo() {
		final String userId = LoggedInUserUtil.getLoggedInUser();

		if(userId == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		logger.debug("USER ID:" + userId);
		final User user = userService.getById(userId);

		final UserDTO userDTO = new UserDTO(user.getId(), user.getLoginId(), user.getSecurityRole().getDescription(),
				user.getFirstName(), user.getLastName(), user.getMiddleInit(), user.getTitle(), user.getEmailAddress(),
				user.getPhoneNumber(), new OrganizationDTO(user.getOrganizationId(), user.getOrganizationName(), user.getOrgOID()));
		return ResponseEntity.ok(userDTO);
	}

	/**
	 * This method is used to search for users to share a measure or a library or get all users.
	 * @param name search by name
	 * @param share search users for sharing
	 * @return list of users
	 */
	@GetMapping(path = "/api/users")
	public ResponseEntity<List<UserDTO>> getUserListForSharing(@RequestParam(required = false) final String name,
			@RequestParam(required = false) final boolean share) {
		final String userId = LoggedInUserUtil.getLoggedInUser();

		if(userId == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<User> userList;
		
		if (share) {
			userList = userDAO.getUsersListForSharingMeasureOrLibrary(name);
		} else {
			userList = userDAO.getAllUsers();
		}
		
		final List<UserDTO> activeUserList = new ArrayList<>();

		userList.forEach(user -> activeUserList.add(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleInit(),
				new OrganizationDTO(user.getOrganizationId(), user.getOrganizationName(), user.getOrgOID()))));

		return ResponseEntity.ok(activeUserList);
	}
}
