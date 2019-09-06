package mat.api.measures.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import mat.api.error.ErrorCode;
import mat.api.error.ValidatorErrorUtility;
import mat.api.error.dto.FieldErrorDTO;
import mat.api.error.exception.NotFoundException;
import mat.api.error.exception.ValidationException;
import mat.api.measures.MeasuresAPIMessageUtil;
import mat.api.measures.MeasuresAPIUtil;
import mat.api.measures.dto.ShareMeasureDTO;
import mat.api.measures.validator.MeasureShareValidator;
import mat.api.user.UserAPIUtil;
import mat.authentication.LoggedInUserUtil;
import mat.entity.SecurityRole;
import mat.model.clause.ShareLevel;
import mat.model.measure.ManageMeasureShareModel;
import mat.model.measure.Measure;
import mat.model.measure.MeasureShareDTO;
import mat.service.impl.UserServiceImpl;
import mat.service.measure.impl.MeasureLibraryServiceImpl;
import mat.service.packager.impl.MeasurePackageServiceImpl;

@RestController
public class MeasureShareController {

	@Autowired private MeasurePackageServiceImpl measurePackageService;
	@Autowired private MeasureLibraryServiceImpl measureLibraryService;
	@Autowired private UserServiceImpl userService;
	
	@PostMapping(value = "/api/measures/{id}/share", produces = "application/json")
	public ResponseEntity<Object> shareMeasure(@RequestBody ShareMeasureDTO requestBody, @PathVariable String id, BindingResult result) throws NotFoundException, ValidationException {
		Measure measure = measurePackageService.getById(id);
        
        if(LoggedInUserUtil.getLoggedInUserRole().equals(SecurityRole.ADMIN_ROLE)) {
        	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
		
		if(!MeasuresAPIUtil.canUserViewMeasure(LoggedInUserUtil.getLoggedInUser(), LoggedInUserUtil.getLoggedInUserRole(), measure) 
				|| !MeasuresAPIUtil.doesMeasureExist(measure)) {
			throw new NotFoundException(MeasuresAPIMessageUtil.getMeasureNotFoundMessage(id));
		}
        
		if(!measure.getOwner().getId().equals(LoggedInUserUtil.getLoggedInUser()) 
				&& !LoggedInUserUtil.getLoggedInUserRole().equals(SecurityRole.SUPER_USER_ROLE)) {
        	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		MeasureShareValidator validator = new MeasureShareValidator();
		validator.validate(requestBody, result);
        if(result.hasErrors()) {
        	throw new ValidationException("Could not share measure.", ValidatorErrorUtility.convertResultToValidationError(result));
        }
		   
		if(!UserAPIUtil.doesUserExist(userService.getById(requestBody.getUserId()))) {
			FieldErrorDTO error = new FieldErrorDTO("userId", ErrorCode.MISSING.getValue(), "Could not find user.");
			throw new ValidationException("Could not share measure.", error);
		}
		
		
		ManageMeasureShareModel shareModel = measureLibraryService.getUsersForShare(null, id, 0, Integer.MAX_VALUE);
		MeasureShareDTO newShare = new MeasureShareDTO(id, ShareLevel.MODIFY_ID, LoggedInUserUtil.getLoggedInUser());
		shareModel.getData().add(newShare);
		newShare.setUserId(requestBody.getUserId());
		measureLibraryService.updateUsersShare(shareModel);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
