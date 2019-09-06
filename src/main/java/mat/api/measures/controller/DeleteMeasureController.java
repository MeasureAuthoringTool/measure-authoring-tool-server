package mat.api.measures.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import mat.api.error.exception.NotFoundException;
import mat.api.measures.MeasuresAPIMessageUtil;
import mat.api.measures.MeasuresAPIUtil;
import mat.authentication.LoggedInUserUtil;
import mat.error.AuthenticationException;
import mat.error.measure.DeleteMeasureException;
import mat.model.measure.Measure;
import mat.service.measure.impl.MeasureLibraryServiceImpl;
import mat.service.packager.impl.MeasurePackageServiceImpl;

@RestController
public class DeleteMeasureController {

	@Autowired private MeasureLibraryServiceImpl measureLibraryService;
	@Autowired private MeasurePackageServiceImpl measurePackageService;
		
	@DeleteMapping("/api/measures/{id}")
	public ResponseEntity<Object> deleteMeasure(@PathVariable String id) throws NotFoundException, DeleteMeasureException, AuthenticationException {
		Measure measure = measurePackageService.getById(id);
		
		if(!MeasuresAPIUtil.doesMeasureExist(measure) 
				|| !MeasuresAPIUtil.canUserViewMeasure(LoggedInUserUtil.getLoggedInUser(), 
						LoggedInUserUtil.getLoggedInUserRole(), measure)) {
			throw new NotFoundException(MeasuresAPIMessageUtil.getMeasureNotFoundMessage(id));
		}
		
		if(!measure.getOwner().getId().equals(LoggedInUserUtil.getLoggedInUser())) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		measureLibraryService.deleteMeasure(id, LoggedInUserUtil.getLoggedInUser());
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
