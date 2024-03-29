package mat.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import mat.error.bonnie.*;
import mat.model.valueset.MatValueSet;
import mat.model.component.ComponentMeasure;
import mat.model.measure.MeasureExport;
import mat.bonnie.api.result.BonnieCalculatedResult;
import mat.export.ExportResult;

public interface SimpleEMeasureService {	
	ExportResult getSimpleXML(String measureId) throws Exception;
	
	ExportResult getEMeasureHTML(String measureId) throws Exception;
	
	ExportResult getEMeasureXLS(String measureId) throws Exception;
	
	ExportResult getEMeasureZIP(String measureId,Date exportDate) throws Exception;
	
	ExportResult getValueSetXLS(String valueSetId) throws Exception;
	
	ExportResult getBulkExportZIP(String[] measureIds, Date[] exportDates) throws Exception;
	
	ExportResult exportMeasureIntoSimpleXML(String measureId, String xmlString, List<MatValueSet> matValueSetList) throws Exception;

	ExportResult getHumanReadableForNode(String measureId, String populationSubXML) throws Exception;

	ExportResult getHumanReadable(String measureId, String currentReleaseVersion) throws Exception;
	
	ExportResult getHQMF(String measureId);

	ExportResult getCQLLibraryFile(String measureId) throws Exception;

	ExportResult getELMFile(String measureId) throws Exception;

	ExportResult getJSONFile(String measureId) throws Exception;
	
	BonnieCalculatedResult getBonnieExportCalculation(String measureId, String userId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, BonnieBadParameterException, BonnieDoesNotExistException, BonnieUnauthorizedException, BonnieServerException, BonnieNotFoundException, BonnieDoesNotExistException, BonnieBadParameterException;

	ExportResult getCompositeExportResult(String id, List<ComponentMeasure> componentMeasures) throws Exception;

	MeasureExport getMeasureExport(String id);

	ExportResult createOrGetCQLLibraryFile(String id, MeasureExport measureExport) throws Exception;

	ExportResult createOrGetELMLibraryFile(String id, MeasureExport measureExport) throws Exception;

	ExportResult createOrGetJSONLibraryFile(String id, MeasureExport measureExport) throws Exception;

	ExportResult createOrGetEMeasureHTML(String measureId) throws Exception;

	ExportResult createOrGetHQMFForv3Measure(String measureId);

	ExportResult getHQMFForv3Measure(String measureId) throws Exception;

	ExportResult createOrGetHQMF(String measureId);

	ExportResult createOrGetHumanReadable(String measureId, String measureVersionNumber) throws Exception;
	
}
