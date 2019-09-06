package mat.api.measures.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import mat.api.error.exception.NotFoundException;
import mat.api.measures.MeasuresAPIMessageUtil;
import mat.api.measures.MeasuresAPIUtil;
import mat.api.measures.dto.MeasureHistoryDTO;
import mat.authentication.LoggedInUserUtil;
import mat.dto.AuditLogDTO;
import mat.dto.SearchHistoryDTO;
import mat.model.measure.Measure;
import mat.service.audit.impl.MeasureAuditServiceImpl;
import mat.service.packager.impl.MeasurePackageServiceImpl;

@RestController
public class MeasureHistoryController {
	
	@Autowired private MeasureAuditServiceImpl measureAuditLogService;
	@Autowired private MeasurePackageServiceImpl measurePackageService;

	@GetMapping(value = "/api/measures/{id}/history")
	public ResponseEntity<Object> getMeasureHistory(@PathVariable String id) throws NotFoundException {
		Measure measure = measurePackageService.getById(id);
		
		if(!MeasuresAPIUtil.doesMeasureExist(measure)) {
			throw new NotFoundException(MeasuresAPIMessageUtil.getMeasureNotFoundMessage(id));
		}
		
		if(!MeasuresAPIUtil.canUserViewMeasure(LoggedInUserUtil.getLoggedInUser(), 
				LoggedInUserUtil.getLoggedInUserRole(), measure)) {
			throw new NotFoundException(MeasuresAPIMessageUtil.getMeasureNotFoundMessage(id));
		}
		
		SearchHistoryDTO searchHistory = measureAuditLogService.executeSearch(id, 0, Integer.MAX_VALUE, new ArrayList<>());
		
		List<MeasureHistoryDTO> history = new ArrayList<>();
		
		
		
		for(AuditLogDTO log : searchHistory.getLogs()) {
			
			LocalDateTime date = Instant.ofEpochMilli(log.getEventTs().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
			MeasureHistoryDTO dto = new MeasureHistoryDTO(log.getActivityType(), log.getUserId(), 
					date.toString(), log.getAdditionlInfo());
			history.add(dto);
		}
		
		return ResponseEntity.ok(history);
	}
}
