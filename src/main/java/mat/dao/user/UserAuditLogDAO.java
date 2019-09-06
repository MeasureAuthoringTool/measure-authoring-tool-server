package mat.dao.user;

import java.util.List;

import mat.dao.IDAO;
import mat.dto.UserAuditLogDTO;
import mat.entity.User;
import mat.entity.UserAuditLog;

public interface UserAuditLogDAO extends IDAO<UserAuditLog, String> {

	
	public boolean recordUserEvent(User user, List<String> event, String additionalInfo);
	
	public List<UserAuditLogDTO> searchHistory(String userId);
	
}
