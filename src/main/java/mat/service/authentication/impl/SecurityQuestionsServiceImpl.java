package mat.service.authentication.impl;

import java.util.List;

import mat.dao.authentication.SecurityQuestionsDAO;
import mat.entity.SecurityQuestions;
import mat.service.authentication.SecurityQuestionsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SecurityQuestionsServiceImpl implements SecurityQuestionsService {

	/** The security questions dao. */
	@Autowired
	private SecurityQuestionsDAO securityQuestionsDAO;
	
	/* (non-Javadoc)
	 * @see mat.service.authentication.SecurityQuestionsService#getSecurityQuestions()
	 */
	@Override
	public List<SecurityQuestions> getSecurityQuestions() {
		return securityQuestionsDAO.getSecurityQuestions();
	}
	
	/* (non-Javadoc)
	 * @see mat.service.authentication.SecurityQuestionsService#getSecurityQuestionObj(java.lang.String)
	 */
	@Override
	public SecurityQuestions getSecurityQuestionObj(String question){
		SecurityQuestions secQuesObj = securityQuestionsDAO.getSecurityQuestionObj(question);
		return secQuesObj;
	}
}	