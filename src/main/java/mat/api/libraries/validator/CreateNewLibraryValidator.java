package mat.api.libraries.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import mat.api.error.ErrorCode;
import mat.api.libraries.dto.CreateLibraryDTO;
import mat.cql.CQLModelValidator;

public class CreateNewLibraryValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CreateLibraryDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		CreateLibraryDTO createLibraryDTO = (CreateLibraryDTO) o;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", ErrorCode.MISSING_FIELD.getValue(), "Library Name is required.");
		
		if(StringUtils.isNotEmpty(createLibraryDTO.getName())) {
			
			CQLModelValidator cqlLibraryModel = new CQLModelValidator();
			boolean isValid = cqlLibraryModel.doesAliasNameFollowCQLAliasNamingConvention(createLibraryDTO.getName());
			
			if(!isValid) {
				errors.rejectValue("name", ErrorCode.INVALID.getValue(), "Invalid Library Name. Must start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.");
			}
		}
	}

}
