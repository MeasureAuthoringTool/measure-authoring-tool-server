package mat.cql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CQLModelValidator {
	private final String REGEX_EXPRESSION = "[\"<>'/]";
	private final String CODE_REGEX_EXPRESSION = "^(CODE:/CodeSystem/)([^/]*)" +
			"(/Version/)([^/]*)(/Code/)([^/]*)(/Info)$";
	private final String COMMENT_REGEX_EXPRESSION = "(\\*/)|(/\\*)";

	private Pattern regularExpressionPattern = Pattern.compile(REGEX_EXPRESSION);
	private Pattern codeRegularExpressionPattern = Pattern.compile(CODE_REGEX_EXPRESSION);
	private Pattern commentRegularExpressionPattern = Pattern.compile(COMMENT_REGEX_EXPRESSION);
	
	/**
	 * Validate for special character in CQL 
	 * Identifier Names.
	 *
	 * @param identifierName the identifier name
	 * @return true, if successful
	 */
	public boolean hasSpecialCharacter(String identifierName) {
		Matcher matcher = regularExpressionPattern.matcher(identifierName);
		boolean isValidSpecialChar = matcher.find();
		
		if ((identifierName == null) || "".equals(identifierName) || isValidSpecialChar) {
			return true;
		}
		return false;
	}
	
	/**
	 * Validate for alias name special char.
	 *
	 * @param identifierName the identifier name
	 * @return true, if successful
	 */
	public boolean doesAliasNameFollowCQLAliasNamingConvention(String identifierName) {
		
		char firstChar = identifierName.charAt(0);
		if(!Character.isLetter(firstChar) && firstChar != '_') {
			return false; 
		}
		
		for(int i = 1; i < identifierName.length(); i++) {
			char ch = identifierName.charAt(i);
			if(!Character.isDigit(ch) && !Character.isLetter(ch) && ch != '_') {
				return false; 
			}
		}
		

		return true; 
	}
	
	/**
	 * Validate for code identifier.
	 *
	 * @param url the url
	 * @return true, if successful
	 */
	public boolean validateForCodeIdentifier(String url){
		Matcher matcher = codeRegularExpressionPattern.matcher(url);
		return !matcher.find();
	}
	
	public boolean isCommentMoreThan250Characters(String comment) {
		return comment.length() > 250;
	}
	
	public boolean doesCommentContainInvalidCharacters(String comment) {
		Matcher matcher = commentRegularExpressionPattern.matcher(comment);
		return matcher.find();
	}
	
	/**
	 * Validate for comment text area.
	 *
	 * @param comment the comment
	 * @return true, if successful
	 */
	public boolean isCommentTooLongOrContainsInvalidText(String comment) {
		boolean isInValid = false;
		Matcher matcher = commentRegularExpressionPattern.matcher(comment);
		if (comment.length() > 250 || matcher.find()) {
			isInValid = true;
		}
		return isInValid;
	}
	
}
