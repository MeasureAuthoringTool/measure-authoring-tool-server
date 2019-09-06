package mat.api.measures;

import org.hibernate.ObjectNotFoundException;
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
import mat.api.measures.dto.AdvancedMeasureDTO;
import mat.api.measures.dto.CreateMeasureDTO;
import mat.api.measures.dto.MeasureDTO;
import mat.api.measures.validator.CreateNewMeasureValidator;
import mat.authentication.LoggedInUserUtil;
import mat.dao.measure.impl.MeasureDAOImpl;
import mat.entity.SecurityRole;
import mat.model.measure.Measure;
import mat.model.measure.MeasureShare;
import mat.model.measuredetail.ManageMeasureDetailModel;
import mat.result.SaveMeasureResult;
import mat.service.audit.impl.MeasureAuditServiceImpl;
import mat.service.measure.impl.MeasureLibraryServiceImpl;
import mat.service.packager.impl.MeasurePackageServiceImpl;

@RestController
public class MeasuresController {

    @Autowired
    private MeasureDAOImpl measureDAO;

    @Autowired
    private MeasureLibraryServiceImpl measureLibraryService;
    
    @Autowired
    private MeasurePackageServiceImpl measurePackageService;
    
    @Autowired
    private MeasureAuditServiceImpl measureAuditService;

    @GetMapping(value = "/api/measures/{id}", produces = "application/json")
    public ResponseEntity<Object> getMeasureById(@PathVariable String id) throws NotFoundException {
        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        String currentUserRole = LoggedInUserUtil.getLoggedInUserRole();
        Measure measure = measurePackageService.getById(id);
        if(!doesMeasureExist(measure)) {
        	throw new NotFoundException(getNotFoundMessage(id));
        }

        MeasureDTO dto = null;
        if(currentUserRole.equals(SecurityRole.ADMIN_ROLE)) {
            dto = new MeasureDTO(measure);
            return ResponseEntity.ok(dto);
        }

        if(measure.getIsPrivate() && !currentUserRole.equals(SecurityRole.SUPER_USER_ROLE)) {
            if(!currentUserId.equals(measure.getOwner().getId())) {
            	throw new NotFoundException(getNotFoundMessage(id));
            }

            boolean isMeasureSharedWithCurrentUser = false;
            for(MeasureShare share : measure.getShares()) {
                if(currentUserId.equals(share.getShareUser().getId())) {
                   isMeasureSharedWithCurrentUser = true;
                }
            }

            if(!isMeasureSharedWithCurrentUser) {
            	throw new NotFoundException(getNotFoundMessage(id));
            }
        }

        dto = getAdvancedMeasureDTOFromMeasure(currentUserId, currentUserRole, measure);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/api/measures", produces = "application/json")
    public ResponseEntity<Object> createMeasure(@RequestBody CreateMeasureDTO createMeasureDTO, BindingResult result) throws ValidationException {
        if(LoggedInUserUtil.getLoggedInUserRole().equals(SecurityRole.ADMIN_ROLE)) {
        	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    	
    	
    	CreateNewMeasureValidator validator = new CreateNewMeasureValidator();
        validator.validate(createMeasureDTO, result);
        if(result.hasErrors()) {
        	throw new ValidationException("Could not create measure.", ValidatorErrorUtility.convertResultToValidationError(result));
        }

        ManageMeasureDetailModel manageMeasureDetailModel = new ManageMeasureDetailModel();
        manageMeasureDetailModel.setName(createMeasureDTO.getName());
        manageMeasureDetailModel.setShortName(createMeasureDTO.geteCQMAbbreviatedTitle());
        manageMeasureDetailModel.setIsPatientBased(createMeasureDTO.getIsPatientBased());
        manageMeasureDetailModel.setMeasScoring(createMeasureDTO.getMeasureScoring());
        manageMeasureDetailModel.setDraft(true);
        manageMeasureDetailModel.scrubForMarkUp();
        SaveMeasureResult newMeasureResult = measureLibraryService.save(manageMeasureDetailModel);


        Measure measure = measurePackageService.getById(newMeasureResult.getId());
        AdvancedMeasureDTO dto =
                getAdvancedMeasureDTOFromMeasure(LoggedInUserUtil.getLoggedInUser(), LoggedInUserUtil.getLoggedInUserRole(), measure);
     
        measureAuditService.recordMeasureEvent(dto.getId(), "Measure Created", null, false);
        return ResponseEntity.ok(dto);
    }

    
    private String getNotFoundMessage(String id) {
		return "Measure with " + id + " could not be found.";
	}

	private boolean doesMeasureExist(Measure measure) {
		try {
			if(measure.getDescription() == null) {
				return false;
			}
        } catch (ObjectNotFoundException e) {
        	return false;
        }
		return true;
	}

    private AdvancedMeasureDTO getAdvancedMeasureDTOFromMeasure(String currentUserId, String currentUserRole, Measure measure) {
        return  MeasuresAPIUtil.makeMeasureDTO(measureDAO, measure);
    }
}
