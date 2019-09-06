package mat.api.measures.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import mat.api.error.exception.NotFoundException;
import mat.api.error.exception.ValidationException;
import mat.api.measures.MeasuresAPIMessageUtil;
import mat.api.measures.MeasuresAPIUtil;
import mat.authentication.LoggedInUserUtil;
import mat.dao.measure.impl.MeasureDAOImpl;
import mat.error.MatException;
import mat.model.measure.ManageMeasureSearchModel.Result;
import mat.model.measure.Measure;
import mat.model.measuredetail.ManageMeasureDetailModel;
import mat.service.impl.MatContextServiceUtil;
import mat.service.measure.impl.MeasureCloningServiceImpl;
import mat.service.measure.impl.MeasureLibraryServiceImpl;
import mat.service.packager.impl.MeasurePackageServiceImpl;

@RestController
public class MeasureDraftController {

	@Autowired private MeasureLibraryServiceImpl measureLibraryService;
	@Autowired private MeasureCloningServiceImpl measureCloningService; 
	@Autowired private MeasurePackageServiceImpl measurePackageService;
	@Autowired private MeasureDAOImpl measureDAO;
	
	@PostMapping(value = "/api/measures/{id}/draft", produces = "application/json")
	public ResponseEntity<Object> draftMeasure(@PathVariable String id) throws MatException, NotFoundException, ValidationException {
		Measure currentMeasure = measurePackageService.getById(id);
		
		// check if the measure exists / user can view the measure
		if(!MeasuresAPIUtil.doesMeasureExist(currentMeasure) 
				|| !MeasuresAPIUtil.canUserViewMeasure(LoggedInUserUtil.getLoggedInUser(),
						LoggedInUserUtil.getLoggedInUserRole(), currentMeasure)) {
			throw new NotFoundException(MeasuresAPIMessageUtil.getMeasureNotFoundMessage(id));
		}
		
		// check for edit privileges on the measure
		if(!MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, id, false)) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		// check if the measure can be drafted
		List<Measure> measureList = new ArrayList<>();
		measureList.add(currentMeasure);
		if(!MatContextServiceUtil.get().isMeasureDraftable(measureDAO, currentMeasure)) {
			throw new ValidationException("Cannot draft a measure if there is a measure in the family that is already in a draft state.");
		}
		
		ManageMeasureDetailModel currentMeasureDetailModel = measureLibraryService.getMeasure(id);
		Result result = measureCloningService.clone(currentMeasureDetailModel, LoggedInUserUtil.getLoggedInUser(), true);
		Measure newMeasure = measurePackageService.getById(result.getId());
		return ResponseEntity.ok(MeasuresAPIUtil.makeMeasureDTO(measureDAO, newMeasure));
	}
}
