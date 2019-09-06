package mat.api.libraries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import mat.api.error.ValidatorErrorUtility;
import mat.api.error.exception.NotFoundException;
import mat.api.error.exception.ValidationException;
import mat.api.libraries.dto.AdvancedLibraryDTO;
import mat.api.libraries.dto.CreateLibraryDTO;
import mat.api.libraries.dto.LibraryDTO;
import mat.api.libraries.validator.CreateNewLibraryValidator;
import mat.authentication.LoggedInUserUtil;
import mat.entity.SecurityRole;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.result.SaveCQLLibraryResult;
import mat.service.audit.impl.CQLLibraryAuditServiceImpl;
import mat.service.library.impl.CQLLibraryServiceImpl;

@RestController
public class LibrariesController {
	
	@Autowired private CQLLibraryServiceImpl cqlLibraryService;
	@Autowired private CQLLibraryAuditServiceImpl cqlLibraryAuditService;
	
	@GetMapping("/api/libraries/{id}")
	public ResponseEntity<Object> getLibraryById(@PathVariable String id) throws NotFoundException {
		
		CQLLibraryDataSetObject cqlLibraryDataSetObject = cqlLibraryService.findCQLLibraryByID(id);
		
		if(cqlLibraryDataSetObject == null) {
			throw new NotFoundException("Could not find CQL Library with id " + id + "."); 
		}
		
		if(LoggedInUserUtil.getLoggedInUserRole().equals(SecurityRole.ADMIN_ROLE)) {
			return ResponseEntity.ok(new LibraryDTO(cqlLibraryDataSetObject));
		}
		
		return ResponseEntity.ok(new AdvancedLibraryDTO(cqlLibraryDataSetObject));
	}
	
	@PostMapping("/api/libraries")
	public ResponseEntity<Object> createNewLibrary(@RequestBody CreateLibraryDTO createLibraryDTO, BindingResult result) throws ValidationException {
		
		if(LoggedInUserUtil.getLoggedInUserRole().equals(SecurityRole.ADMIN_ROLE)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		
		CreateNewLibraryValidator validator = new CreateNewLibraryValidator();
		validator.validate(createLibraryDTO, result);
		
		if(result.hasErrors()) {
			throw new ValidationException("Could not create library.", ValidatorErrorUtility.convertResultToValidationError(result));
		}
		
		CQLLibraryDataSetObject cqlLibraryDataSetObject = new CQLLibraryDataSetObject();
		cqlLibraryDataSetObject.setCqlName(createLibraryDTO.getName());
		SaveCQLLibraryResult saveResult = cqlLibraryService.save(cqlLibraryDataSetObject);
		cqlLibraryDataSetObject = cqlLibraryService.findCQLLibraryByID(saveResult.getId());
		AdvancedLibraryDTO library = new AdvancedLibraryDTO(cqlLibraryDataSetObject);
		
		cqlLibraryAuditService.recordCQLLibraryEvent(library.getId(), "CQL Library Created", null, false);
		return ResponseEntity.ok(library);
	}
	
	
}
