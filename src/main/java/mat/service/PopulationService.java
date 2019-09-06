package mat.service;


import mat.result.SaveUpdateCQLResult;

public interface PopulationService {
	SaveUpdateCQLResult savePopulations(String measureId, String nodeName, String nodeToReplace);
}
