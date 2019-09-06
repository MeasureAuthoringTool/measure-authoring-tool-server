package mat.service.audit.impl;

import mat.dao.audit.TransactionAuditLogDAO;
import mat.entity.TransactionAuditLog;
import mat.service.audit.TransactionAuditService;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionAuditServiceImpl implements TransactionAuditService{

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TransactionAuditServiceImpl.class);
	
	/** The transaction audit log dao. */
	@Autowired
	private TransactionAuditLogDAO transactionAuditLogDAO;

	/* (non-Javadoc)
	 * @see mat.service.audit.TransactionAuditService#recordTransactionEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean recordTransactionEvent(String primaryId, String secondaryId,
			String activityType, String userId, String additionalInfo, int logLevel) {
		
		if(logLevel ==0){
			logger.info(additionalInfo);
			return true;
		}else{
			TransactionAuditLog tal = new TransactionAuditLog();
			
			//cannot be null
			String activityTypeStr = activityType == null ? "DEFAULT_TRANSACTION_EVENT" : activityType;
			tal.setActivityType(activityTypeStr);
			
			tal.setAdditionalInfo(additionalInfo);
			tal.setPrimaryId(primaryId);
			tal.setSecondaryId(secondaryId);
			
			//cannot be null
			String userIdStr = userId == null ? "UNKNOWN_USER" : userId;
			tal.setUserId(userIdStr);
			try{
				transactionAuditLogDAO.save(tal);
				return true;
			}catch (Exception e) {
				return false;
			}
		}
	}
	
}
