package mat.humanreadable;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathExpressionException;

import mat.cql.CQLUtilityClass;
import mat.constants.MatConstants;
import mat.cql.CQLUtil;
import mat.cql.LibHolderObject;
import mat.filesystem.ResourceLoader;
import mat.humanreadable.cql.*;
import mat.measure.MeasureUtil;
import mat.result.SaveUpdateCQLResult;
import mat.humanreadable.cql.HumanReadableTerminologyModel;
import mat.humanreadable.cql.HumanReadableExpressionModel;
import mat.humanreadable.cql.HumanReadableModel;
import mat.humanreadable.cql.HumanReadablePopulationModel;
import mat.humanreadable.cql.HumanReadableValuesetModel;
import mat.humanreadable.qdm.HQMFHumanReadableGenerator;
import mat.xml.XmlProcessor;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import freemarker.template.TemplateException;
import mat.dao.library.CQLLibraryDAO;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;

@Component
public class HumanReadableGenerator {
	
	private final String CQLFUNCTION = "cqlfunction";

	private final String CQLDEFINITION = "cqldefinition";
	
	private static final String[] POPULATION_NAME_ARRAY = {MatConstants.INITIAL_POPULATION,
			MatConstants.DENOMINATOR, MatConstants.DENOMINATOR_EXCLUSIONS, MatConstants.NUMERATOR,
			MatConstants.NUMERATOR_EXCLUSIONS, MatConstants.DENOMINATOR_EXCEPTIONS,
			MatConstants.MEASURE_POPULATION, MatConstants.MEASURE_POPULATION_EXCLUSIONS, MatConstants.STRATUM ,
			MatConstants.MEASURE_OBSERVATION_POPULATION};
	
	@Autowired
    CQLHumanReadableGenerator humanReadableGenerator;
	
	public String generateHTMLForPopulationOrSubtree(String measureId, String subXML, String measureXML,CQLLibraryDAO cqlLibraryDAO) {

		XmlProcessor subXMLProcessor = new XmlProcessor(subXML);
		String html = "";

		if (subXMLProcessor.getOriginalDoc().getDocumentElement().hasChildNodes()) {
			String firstNodeName = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild().getNodeName();
			System.out.println("firstNodeName:" + firstNodeName);

			if ("cqldefinition".equals(firstNodeName) || "cqlfunction".equals(firstNodeName)
					|| "cqlaggfunction".equals(firstNodeName)) {
				Node cqlNode = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild();
				HumanReadablePopulationModel population;
				try {
					population = getPopulationModel(measureXML, cqlNode.getParentNode());
					html = humanReadableGenerator.generateSinglePopulation(population);
				} catch (XPathExpressionException | IOException | TemplateException e) {
					e.printStackTrace();
				}
			} else {
				html = HQMFHumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId, subXML, measureXML);
			}
		} else {
			return "<html></html>";
		}

		return html;
	}
	
	public String generateHTMLForMeasure(String measureId, String simpleXml, String measureReleaseVersion, CQLLibraryDAO cqlLibraryDAO){
		
		String html = "";
		System.out.println("Generating human readable for ver:"+measureReleaseVersion);
		if(MeasureUtil.getInstance().isCQLMeasure(measureReleaseVersion)){
			try {
				XmlProcessor processor = new XmlProcessor(simpleXml);
				Mapping mapping = new Mapping(); 
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("SimpleXMLHumanReadableModelMapping.xml"));
				Unmarshaller unmarshaller = new Unmarshaller(mapping);
				unmarshaller.setClass(HumanReadableModel.class);
				unmarshaller.setWhitespacePreserve(true);
							
				CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(simpleXml);
		

				CQLUtil.CQLArtifactHolder usedCQLArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(processor.getOriginalDoc());
				SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, getCQLIdentifiers(cqlModel));
				Map<String, XmlProcessor> includedLibraryXmlProcessors = loadIncludedLibXMLProcessors(cqlModel);
				
				HumanReadableModel model = (HumanReadableModel) unmarshaller.unmarshal(new InputSource(new StringReader(simpleXml)));
				model.setPopulationCriterias(getPopulationCriteriaModels(processor));
				model.setSupplementalDataElements(getSupplementalDataElements(processor));
				model.setRiskAdjustmentVariables(getRiskAdjustmentVariables(processor));
				model.setValuesetDataCriteriaList(getValuesetDataCriteria(processor));
				model.setCodeDataCriteriaList(getCodeDataCriteria(processor));
						
				List<HumanReadableTerminologyModel> valuesetTerminologyList = getValuesetTerminology(processor);
				sortTerminologyList(valuesetTerminologyList);
				model.setValuesetTerminologyList(valuesetTerminologyList);

				List<HumanReadableTerminologyModel> codeTerminologyList = getCodeTerminology(processor);
				sortTerminologyList(codeTerminologyList);
				model.setCodeTerminologyList(codeTerminologyList);
				
				model.setDefinitions(getDefinitions(cqlModel, processor, includedLibraryXmlProcessors, cqlResult, usedCQLArtifactHolder));
				model.setFunctions(getFunctions(cqlModel, processor, includedLibraryXmlProcessors, cqlResult, usedCQLArtifactHolder));
				
				List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList = new ArrayList<>();
				valuesetAndCodeDataCriteriaList.addAll(model.getValuesetDataCriteriaList());
				valuesetAndCodeDataCriteriaList.addAll(model.getCodeDataCriteriaList());
				sortDataCriteriaList(valuesetAndCodeDataCriteriaList);
				model.setValuesetAndCodeDataCriteriaList(valuesetAndCodeDataCriteriaList);
				
				html = humanReadableGenerator.generate(model);
			} catch (IOException | TemplateException | MappingException | MarshalException | ValidationException | XPathExpressionException e) {
				e.printStackTrace();
			}
		} else {
			html = HQMFHumanReadableGenerator.generateHTMLForMeasure(measureId,simpleXml);
		}
		
		return html;
	}

	private void sortDataCriteriaList(List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList) {
		Collections.sort(valuesetAndCodeDataCriteriaList, new Comparator<HumanReadableTerminologyModel>() {

			@Override
			public int compare(HumanReadableTerminologyModel o1, HumanReadableTerminologyModel o2) {
				
				String o1String = o1.getDatatype() + ": " + o1.getName();
				String o2String = o2.getDatatype() + ": " + o2.getName();
				return o1String.compareToIgnoreCase(o2String);
			}
		});
	}

	private void sortTerminologyList(List<HumanReadableTerminologyModel> terminologyList) throws XPathExpressionException {
		Collections.sort(terminologyList, new Comparator<HumanReadableTerminologyModel>() {

			@Override
			public int compare(HumanReadableTerminologyModel o1, HumanReadableTerminologyModel o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
	}
	
	private List<String> getCQLIdentifiers(CQLModel cqlModel) {
		List<String> identifiers = new ArrayList<>(); 
		List<CQLDefinition> cqlDefinition = cqlModel.getDefinitionList();
		for(CQLDefinition cqlDef:cqlDefinition){
			identifiers.add(cqlDef.getName());
		}
		
		List<CQLFunctions> cqlFunctions = cqlModel.getCqlFunctions();
		for(CQLFunctions cqlFunc:cqlFunctions){
			identifiers.add(cqlFunc.getName());
		}
		
		return identifiers;
	}
	
	private HumanReadablePopulationModel getPopulationModel(String measureXML, Node populationNode) throws XPathExpressionException {
		XmlProcessor processor = new XmlProcessor(measureXML);
		return getPopulationCriteria(processor, populationNode);
	}
	
	private Map<String, XmlProcessor> loadIncludedLibXMLProcessors(CQLModel cqlModel) {
		Map<String, XmlProcessor> returnMap = new HashMap<String, XmlProcessor>();
		Map<String, LibHolderObject> includeMap = cqlModel.getIncludedCQLLibXMLMap();
		for (String libName : includeMap.keySet()) {
			LibHolderObject lib = includeMap.get(libName);
			String xml = lib.getMeasureXML();
			XmlProcessor xmlProcessor = new XmlProcessor(xml);
			returnMap.put(libName, xmlProcessor);
		}

		return returnMap;
	}
	
	private String getCQLFunctionSignature(String expressionName, XmlProcessor populationOrSubtreeXMLProcessor) {
		
		String signature = "";
		
		String xPath = "//cqlLookUp//function[@name = '"+ expressionName +"']/arguments";
		try {
			Node argumentsNode = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), xPath);
			if(argumentsNode != null){
				NodeList children = argumentsNode.getChildNodes();
				for(int i=0;i < children.getLength();i++){
					Node child = children.item(i);
					if(child.getNodeName().equals("argument")){
						String type = child.getAttributes().getNamedItem("type").getNodeValue();
						if("QDM Datatype".equals(type)){
							type = child.getAttributes().getNamedItem("qdmDataType").getNodeValue();
							type = "\"" + type + "\"";
						}else if("Others".equals(type)){
							type = child.getAttributes().getNamedItem("otherType").getNodeValue();
						}
						String argName = child.getAttributes().getNamedItem("argumentName").getNodeValue();
						signature += argName + " " + type + ", ";
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		if(signature.length() > 0){
			signature = signature.trim();
			if(signature.endsWith(",")){
				signature = signature.substring(0, signature.length() - 1);
			}
			signature = "(" + signature + ")";
		}else{
			signature = "()";
		}
		
		return signature;
	}
	
	private List<HumanReadableExpressionModel> getDefinitions(CQLModel cqlModel, XmlProcessor parentLibraryProcessor, Map<String, XmlProcessor> includedLibraryXmlProcessors, SaveUpdateCQLResult cqlResult, CQLUtil.CQLArtifactHolder usedCQLArtifactHolder) {
		List<HumanReadableExpressionModel> definitions = new ArrayList<>(); 		
		List<String> usedDefinitions = cqlResult.getUsedCQLArtifacts().getUsedCQLDefinitions();
		List<String> definitionsList = new ArrayList<String>(usedCQLArtifactHolder.getCqlDefFromPopSet());
		definitionsList.removeAll(usedDefinitions);
		definitionsList.addAll(usedDefinitions);
		definitionsList = definitionsList.stream().distinct().collect(Collectors.toList());
		Collections.sort(definitionsList, String.CASE_INSENSITIVE_ORDER);
		
		for(String expressionName : definitionsList) {
			String statementIdentifier = expressionName;
			XmlProcessor currentProcessor = parentLibraryProcessor; 
			String[] arr = expressionName.split(Pattern.quote("|"));
			if(arr.length == 3){
				expressionName = arr[2];
				statementIdentifier = arr[1] + "." + arr[2];
				currentProcessor = includedLibraryXmlProcessors.get(arr[0] + "|" + arr[1]);
			}
			
			HumanReadableExpressionModel expression = new HumanReadableExpressionModel(statementIdentifier, getLogicStringFromXMLByName(expressionName, CQLDEFINITION, currentProcessor));
			definitions.add(expression);
		}
		
		return definitions; 
	}
	
	private List<HumanReadableExpressionModel> getFunctions(CQLModel cqlModel, XmlProcessor parentLibraryProcessor, Map<String, XmlProcessor> includedLibraryXmlProcessors, SaveUpdateCQLResult cqlResult, CQLUtil.CQLArtifactHolder usedCQLArtifactHolder) {
		List<HumanReadableExpressionModel> functions = new ArrayList<>(); 
		List<String> usedFunctions = cqlResult.getUsedCQLArtifacts().getUsedCQLFunctions();
		List<String> functionsList = new ArrayList<String>(usedCQLArtifactHolder.getCqlFuncFromPopSet());
		functionsList.removeAll(usedFunctions);
		functionsList.addAll(usedFunctions);
		functionsList = functionsList.stream().distinct().collect(Collectors.toList());
		Collections.sort(functionsList, String.CASE_INSENSITIVE_ORDER);
		
		for(String expressionName : functionsList) {
			String statementIdentifier = expressionName;
			XmlProcessor currentProcessor = parentLibraryProcessor; 
			String[] arr = expressionName.split(Pattern.quote("|"));
			if(arr.length == 3) {
				expressionName = arr[2];
				statementIdentifier = arr[1] + "." + arr[2];
				currentProcessor = includedLibraryXmlProcessors.get(arr[0] + "|" + arr[1]);
			}
				
				HumanReadableExpressionModel expression = new HumanReadableExpressionModel(
						statementIdentifier +  getCQLFunctionSignature(expressionName, currentProcessor), 
						getLogicStringFromXMLByName(expressionName, CQLFUNCTION, currentProcessor));
				functions.add(expression);
		}
				
		return functions; 
	}
	
	private String getLogicStringFromXMLByName(String cqlName, String cqlType, XmlProcessor simpleXMLProcessor) {
		
		String logic = "";
		String xPath = "//cqlLookUp//";
		
		if(cqlType.equals(CQLDEFINITION)){
			xPath += "definition[@name='" + cqlName + "']/logic";
		}else if(cqlType.equals(CQLFUNCTION)){
			xPath += "function[@name='" + cqlName + "']/logic";
		}
		
		 try {
			Node logicNode = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(), xPath);
			if(logicNode != null){
				logic = logicNode.getTextContent();
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}		
		 
		return logic;
	}

	
	private List<HumanReadableExpressionModel> getSupplementalDataElements(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableExpressionModel> supplementalDataElements = new ArrayList<>(); 
		if(processor.findNode(processor.getOriginalDoc(), "/measure/supplementalDataElements") != null) {
			NodeList supplementalDataElementNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/supplementalDataElements/cqldefinition");
			
			for(int i = 0; i < supplementalDataElementNodes.getLength(); i++) {
				Node sde = supplementalDataElementNodes.item(i);
				supplementalDataElements.add(getExpressionModel(processor, sde));
			}
		}
		return supplementalDataElements;
	}
	
	private List<HumanReadableValuesetModel> getValuesetDataCriteria(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableValuesetModel> valuesets = new ArrayList<>(); 
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code='false'][@datatype]");
		
		for(int i = 0; i < elements.getLength(); i++) {
			
			String datatype = elements.item(i).getAttributes().getNamedItem("datatype").getNodeValue(); 
			String name = elements.item(i).getAttributes().getNamedItem("name").getNodeValue(); 
			String oid = elements.item(i).getAttributes().getNamedItem("oid").getNodeValue(); 

			String version = "";
			if(elements.item(i).getAttributes().getNamedItem("version") != null){
				version = elements.item(i).getAttributes().getNamedItem("version").getNodeValue();
			}

			HumanReadableValuesetModel valueset = new HumanReadableValuesetModel(name, oid, version, datatype);
			valuesets.add(valueset);
		}
		
		valuesets.sort(Comparator.comparing(HumanReadableValuesetModel::getDataCriteriaDisplay));
		return valuesets; 
	}
	
	private List<HumanReadableCodeModel> getCodeDataCriteria(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableCodeModel> codes = new ArrayList<>(); 
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(),  "/measure/elementLookUp/qdm[@code='true'][@datatype]");
		
		for(int i = 0; i < elements.getLength(); i++) {
			NamedNodeMap attributeMap = elements.item(i).getAttributes();
			String datatype = attributeMap.getNamedItem("datatype").getNodeValue(); 
			if("attribute".equals(datatype)){
				datatype = "Attribute";
			}
			
			String name = attributeMap.getNamedItem("name").getNodeValue(); 
			String oid = attributeMap.getNamedItem("oid").getNodeValue(); 
			String codesystemVersion = attributeMap.getNamedItem("codeSystemVersion").getNodeValue();					
			String codesystemName = attributeMap.getNamedItem("taxonomy").getNodeValue();
			boolean isCodeSystemVersionIncluded = true;
			Node isCodeSystemVersionIncludedNode = attributeMap.getNamedItem("isCodeSystemVersionIncluded");
			if(isCodeSystemVersionIncludedNode != null) {
				isCodeSystemVersionIncluded = Boolean.parseBoolean(isCodeSystemVersionIncludedNode.getNodeValue());
			} 				 
			
			HumanReadableCodeModel code = new HumanReadableCodeModel(name, oid, codesystemName, isCodeSystemVersionIncluded, codesystemVersion, datatype);
			codes.add(code);
		}
		
		codes.sort(Comparator.comparing(HumanReadableCodeModel::getDataCriteriaDisplay));
		return codes; 
	}
	
	private List<HumanReadableTerminologyModel> getValuesetTerminology(XmlProcessor processor) throws XPathExpressionException {
		Set<HumanReadableValuesetModel> valuesets = new HashSet<>(); 
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"false\"]");
		
		for(int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			String name = current.getAttributes().getNamedItem("name").getNodeValue();
			String oid = current.getAttributes().getNamedItem("oid").getNodeValue();  
			String version = current.getAttributes().getNamedItem("version").getNodeValue(); 
			HumanReadableValuesetModel valueset = new HumanReadableValuesetModel(name, oid, version, "");
			valuesets.add(valueset);
		}
		
		List<HumanReadableTerminologyModel> valuesetList =  new ArrayList<HumanReadableTerminologyModel>(valuesets);
		valuesetList.sort(Comparator.comparing(HumanReadableTerminologyModel::getTerminologyDisplay));
		return valuesetList;
	}
	
	private List<HumanReadableTerminologyModel> getCodeTerminology(XmlProcessor processor) throws XPathExpressionException {
		Set<HumanReadableCodeModel> codes = new HashSet<>();
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"true\"]");
		for(int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			String name = current.getAttributes().getNamedItem("name").getNodeValue(); 
			String oid = current.getAttributes().getNamedItem("oid").getNodeValue();
			String codesystemName = current.getAttributes().getNamedItem("taxonomy").getNodeValue(); 
			String codesystemVersion = "";

			Node isCodeSystemVersionIncludedNode = current.getAttributes().getNamedItem("isCodeSystemVersionIncluded");
			boolean isCodeSystemVersionIncluded = true; // by default the code system should be included if the isCodeSystemIncluded tag does not exist
			if(isCodeSystemVersionIncludedNode != null) {
				isCodeSystemVersionIncluded = Boolean.parseBoolean(isCodeSystemVersionIncludedNode.getNodeValue());
			} 
			
			if(isCodeSystemVersionIncluded) {
				codesystemVersion = current.getAttributes().getNamedItem("codeSystemVersion").getNodeValue();
			}
			
			HumanReadableCodeModel model = new HumanReadableCodeModel(name, oid, codesystemName, isCodeSystemVersionIncluded, codesystemVersion, "");
			codes.add(model);
		}  
		
		List<HumanReadableTerminologyModel> codesList =  new ArrayList<HumanReadableTerminologyModel>(codes);
		codesList.sort(Comparator.comparing(HumanReadableTerminologyModel::getTerminologyDisplay));
		return codesList; 
	}

	private HumanReadableExpressionModel getExpressionModel(XmlProcessor processor, Node sde)
			throws XPathExpressionException {
		String uuid = sde.getAttributes().getNamedItem("uuid").getNodeValue();
		String name = sde.getAttributes().getNamedItem("displayName").getNodeValue();
		String logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//definition[@id='"+ uuid +"']/logic").getTextContent();
		HumanReadableExpressionModel expression = new HumanReadableExpressionModel(name, logic);
		return expression;
	}
	
	private List<HumanReadableExpressionModel> getRiskAdjustmentVariables(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableExpressionModel> riskAdjustmentVariables = new ArrayList<>(); 
		
		if(processor.findNode(processor.getOriginalDoc(), "/measure/riskAdjustmentVariables") != null) {
			NodeList riskAdjustmentVariableNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/riskAdjustmentVariables/cqldefinition");
			for(int i = 0; i < riskAdjustmentVariableNodes.getLength(); i++) {
				Node rav = riskAdjustmentVariableNodes.item(i);
				riskAdjustmentVariables.add(getExpressionModel(processor, rav));
			}
		}
		
		return riskAdjustmentVariables;
	}
	
	private List<HumanReadablePopulationCriteriaModel> getPopulationCriteriaModels(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadablePopulationCriteriaModel> groups = new ArrayList<>();
		
		NodeList groupNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/measureGrouping/group");
		for(int i = 0; i < groupNodes.getLength(); i++) {
			Node group = groupNodes.item(i);
			
			int populationCriteriaNumber = Integer.parseInt(group.getAttributes().getNamedItem("sequence").getNodeValue());
			
			List<HumanReadablePopulationModel> populations = new ArrayList<>();
			
			NodeList populationNodes = group.getChildNodes();
			for(int j = 0; j < populationNodes.getLength(); j++) {
				Node populationNode = populationNodes.item(j);
				HumanReadablePopulationModel population = getPopulationCriteria(processor, populationNode);
				populations.add(population);
			}
			
			populations = sortPopulations(populations); 
			
			String displayName = "Population Criteria " + populationCriteriaNumber;
			HumanReadablePopulationCriteriaModel populationCriteria = new HumanReadablePopulationCriteriaModel(displayName, populations, populationCriteriaNumber);
			groups.add(populationCriteria);
		}
		
		
		
		groups.sort(Comparator.comparing(HumanReadablePopulationCriteriaModel::getSequence));
		return groups; 
	}
	 
	private List<HumanReadablePopulationModel> sortPopulations(List<HumanReadablePopulationModel> populations) {
		List<HumanReadablePopulationModel> sortedPopulations = new ArrayList<>();  
		
		for(String populationType : POPULATION_NAME_ARRAY) {
			sortedPopulations.addAll(getPopulationsByType(populationType, populations));
		}
		
		return sortedPopulations;
	}
	
	private List<HumanReadablePopulationModel> getPopulationsByType(String type, List<HumanReadablePopulationModel> populations) {
		List<HumanReadablePopulationModel> models = new ArrayList<>(); 
		
		
		for(HumanReadablePopulationModel model : populations) {
			if(type.equals(model.getType())) {
				models.add(model);
			}
		}
		
		return models;
	}

	private String getPopulationNameByUUID(String uuid, XmlProcessor processor) throws XPathExpressionException {
		Node population = processor.findNode(processor.getOriginalDoc(),"//clause[@uuid=\"" + uuid + "\"]");
		String name = "";
		if (population != null) {
			String type = population.getAttributes().getNamedItem("type").getNodeValue();
			int numberOfPopulationsWithSameType = countSimilarPopulationsInGroup(type, population.getParentNode());
			
			// if there is only one of the population kind, then we only want to display the population name (without a number attached to it)
			name = (numberOfPopulationsWithSameType == 1)? getPopulationNameByType(type) : population.getAttributes().getNamedItem("displayName").getNodeValue();
		}
		
		return name; 
	}
	
	private int countSimilarPopulationsInGroup(String type, Node node) {
		int count = 0;
		NodeList childClauses = node.getChildNodes();
		if (childClauses != null) {
			for (int i = 0; i < childClauses.getLength(); i++) {
				Node clauseNode = childClauses.item(i);
				String popType = clauseNode.getAttributes().getNamedItem("type").getNodeValue();
				if (popType.equals(type)) {
					count++;
				}
			}
		}
		return count;
	}
	
	private String getPopulationNameByType(String type) {
		String populationName = "";
		if (MatConstants.INITIAL_POPULATION.equals(type)) {
			populationName = "Initial Population";
		} else if (MatConstants.MEASURE_POPULATION.equals(type)) {
			populationName = "Measure Population";
		} else if (MatConstants.MEASURE_POPULATION_EXCLUSIONS.equals(type)) {
			populationName = "Measure Population Exclusions";
		} else if (MatConstants.MEASURE_OBSERVATION_POPULATION.equals(type)) {
			populationName = "Measure Observation";
		} else if (MatConstants.STRATUM.equals(type)) {
			populationName = "Stratification";
		} else if (MatConstants.DENOMINATOR.equals(type)) {
			populationName = "Denominator";
		} else if (MatConstants.DENOMINATOR_EXCLUSIONS.equals(type)) {
			populationName = "Denominator Exclusions";
		} else if (MatConstants.DENOMINATOR_EXCEPTIONS.equals(type)) {
			populationName = "Denominator Exceptions";
		} else if (MatConstants.NUMERATOR.equals(type)) {
			populationName = "Numerator";
		} else if (MatConstants.NUMERATOR_EXCLUSIONS.equals(type)) {
			populationName = "Numerator Exclusions";
		}
		return populationName;
	}

	private HumanReadablePopulationModel getPopulationCriteria(XmlProcessor processor, Node populationNode)
			throws XPathExpressionException {
		String expressionName = "";
		String expressionUUID = "";
		String logic = "";
		String aggregation = null;
		String associatedPopulationName = ""; 
		boolean isInGroup = true; 
		
		if(populationNode.getAttributes().getNamedItem("associatedPopulationUUID") != null) {
			String uuid = populationNode.getAttributes().getNamedItem("associatedPopulationUUID").getNodeValue();
			associatedPopulationName = getPopulationNameByUUID(uuid, processor);
		}
		
		
		if(populationNode.getAttributes().getNamedItem("isInGrouping") != null) {
			isInGroup= Boolean.parseBoolean(populationNode.getAttributes().getNamedItem("isInGrouping").getNodeValue());
		}
				
		String populationName = getPopulationNameByUUID(populationNode.getAttributes().getNamedItem("uuid").getNodeValue(), processor);
		String type = populationNode.getAttributes().getNamedItem("type").getNodeValue();
		if(populationName.contains("Measure Observation")) {
			
			Node functionNode = null;
			Node aggregationNode = null;
			if(populationNode.getFirstChild() != null) {
				if("cqlaggfunction".equals(populationNode.getFirstChild().getNodeName())) {
					aggregationNode = populationNode.getFirstChild();
					aggregation = aggregationNode.getAttributes().getNamedItem("displayName").getNodeValue();
					functionNode = aggregationNode.getFirstChild();
				} else {
					functionNode = populationNode.getFirstChild();
				}
				
				
				expressionName = functionNode.getAttributes().getNamedItem("displayName").getNodeValue();
				expressionName = getPopulationNameByType(expressionName);
				expressionUUID = functionNode.getAttributes().getNamedItem("uuid").getNodeValue();
				logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//function[@id='"+ expressionUUID +"']/logic").getTextContent();
				

				
				if(aggregation != null) {
					logic = aggregation + " (\n  " + logic + "\n)"; // for measures, add in the aggregation
				}
			}
		} else  {
			Node definitionNode = populationNode.getFirstChild();
			if(populationName.contains("Stratum")) {
				if(populationName.equals("stratum")) {
					populationName = populationName + " 1"; // we need to enumerate the the stratum, and if there is only one stratum, it doesn't have a number in the name.... 
				}
				
				populationName = populationName.replace("Stratum", "Stratification"); // for some reason the simple xml uses "stratum" but it really should be "Stratification"
			}

			if(definitionNode != null) {
				expressionName = definitionNode.getAttributes().getNamedItem("displayName").getNodeValue();
				expressionUUID = definitionNode.getAttributes().getNamedItem("uuid").getNodeValue();
				logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//definition[@id='"+ expressionUUID +"']/logic").getTextContent();
			}
		}
		
		HumanReadablePopulationModel population = new HumanReadablePopulationModel(populationName, logic, expressionName, expressionUUID, aggregation, associatedPopulationName, isInGroup, type);
		return population;
	}
	
}
