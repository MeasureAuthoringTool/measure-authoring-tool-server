package mat.dao.authentication;

import java.util.List;

import mat.dao.IDAO;
import mat.entity.SecurityQuestions;

/**
 * The Interface SecurityQuestionsDAO.
 */
public interface SecurityQuestionsDAO extends IDAO<SecurityQuestions, String> {
	
	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	public List<SecurityQuestions> getSecurityQuestions();
	
	/**
	 * Gets the security question obj.
	 * 
	 * @param question
	 *            the question
	 * @return the security question obj
	 */
	public SecurityQuestions getSecurityQuestionObj(String question);
}
