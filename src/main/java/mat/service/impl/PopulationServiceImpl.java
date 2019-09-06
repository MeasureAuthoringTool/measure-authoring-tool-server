package mat.service.impl;

import mat.service.PopulationService;
import mat.model.measure.MeasureXmlModel;
import org.apache.commons.lang3.StringUtils;

import mat.service.measure.MeasurePackageService;
import mat.xml.XmlProcessor;
import mat.result.SaveUpdateCQLResult;
import org.springframework.beans.factory.annotation.Autowired;

public class PopulationServiceImpl implements PopulationService {
	@Autowired MeasurePackageService measurePackageService;

	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private MeasurePackageService getService() {
		return measurePackageService;
	}

	@Override
	public SaveUpdateCQLResult savePopulations(String measureId, String nodeName, String nodeToReplace) {
		
		MeasureXmlModel model = getService().getMeasureXmlForMeasure(measureId);
		XmlProcessor xmlProcessor = new XmlProcessor(model.getXml());

		String parentNode = createParentNode(nodeName);		
		
		String newXml = xmlProcessor.replaceNode(nodeToReplace, nodeName, parentNode);		
		newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
		//Set the updated XML to the model
		model.setXml(newXml);
		//Persist the Modified XML
		getService().saveMeasureXml(model);

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		
		if (StringUtils.isNotBlank(model.getXml())) {
			result.setXml(model.getXml());
			result.setSetId(measureId);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	private String createParentNode(String populationTyp) {
		if(populationTyp.equals("measureObservations") || populationTyp.equals("strata")) {
			return "measure";
		} else {
			return "populations";
		}
		
	}

}
