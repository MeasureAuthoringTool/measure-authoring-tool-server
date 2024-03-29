package mat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import mat.constants.MessageDelegate;
import mat.dao.library.CQLLibraryDAO;
import mat.measurepackage.MeasurePackageDetail;
import mat.model.cql.CQLModel;
import mat.cql.CQLUtilityClass;
import mat.cql.CQLUtil;
import mat.xml.XmlProcessor;
import mat.cql.CQLExpressionObject;
import mat.cql.CQLExpressionOprandObject;
import mat.result.SaveUpdateCQLResult;

import mat.measurepackage.MeasurePackageClauseDetail;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Node;

public class PatientBasedValidator {
	
		private static final String SCORING_CONTINUOUS_VARIABLE = "Continuous Variable";

	private static final String SCORING_RATIO = "Ratio";

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(PatientBasedValidator.class);
	
	private static final String SYSTEM_QUANTITY = "System.Quantity";

	private static final String SYSTEM_DECIMAL = "System.Decimal";

	private static final String SYSTEM_INTEGER = "System.Integer";

	private static final String SYSTEM_BOOLEAN = "System.Boolean";

	private static final String CQLFUNCTION = "cqlfunction";
	
	private static final String CQLAGGFUNCTION = "cqlaggfunction";

	private static final String DISPLAY_NAME = "displayName";

	private static final String MEASURE_OBSERVATION = "measureObservation";

	private static final String XPATH_FOR_MEASURE_SCORING = "/measure/measureDetails/scoring";

	private static final String XPATH_FOR_PATIENT_BASED_INDICATOR = "/measure/measureDetails/patientBasedIndicator";

	//Indicator for CQL return types of Integer,Decimal, or Quantity.
	private static final String CQL_RETURN_TYPE_NUMERIC = "NUMERIC";

	//Indicator for CQL return types of Boolean.
	private static final String CQL_RETURN_TYPE_BOOLEAN = "BOOLEAN";	
	
	
	
	public static List<String> checkPatientBasedValidations(String measureXmlL, MeasurePackageDetail detail, CQLLibraryDAO cqlLibraryDAO) throws XPathExpressionException {
		Map<String, List<String>> expressionPopMap = new HashMap<String,List<String>>();
		Map<String, List<String>> assoExpressionPopMap = new HashMap<String,List<String>>();
		
		List<String> errorMessages = new ArrayList<String>();
		
		XmlProcessor xmlProcessor = new XmlProcessor(measureXmlL);

		Node scoringNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_FOR_MEASURE_SCORING);
		String scoringType = scoringNode.getTextContent();
		
		List<String> exprList = new ArrayList<String>();
		List<String> msrObsFunctionList = new ArrayList<String>();
		List<String> moAssociatedPopUsedExpression = new ArrayList<String>();
		List<MeasurePackageClauseDetail> packageClauses =  detail.getPackageClauses();
		
		for(MeasurePackageClauseDetail measurePackageClauseDetail : packageClauses){
			String populationUUID = measurePackageClauseDetail.getId();
			String type = measurePackageClauseDetail.getType();
			String name = measurePackageClauseDetail.getName();
			//ignore "stratification" nodes.
			if(type.equalsIgnoreCase("stratification")){
				continue;
			}
			
			Node clauseNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure//clause[@uuid='"+populationUUID+"']");
					
			if(type.equals(MEASURE_OBSERVATION)){
				
				//find the cqlfunction here
				Node firstChildNode = clauseNode.getFirstChild();
				
				if(firstChildNode.getNodeName().equals(CQLAGGFUNCTION) || firstChildNode.getNodeName().equals(CQLFUNCTION)){
					if(firstChildNode.hasChildNodes()){
						firstChildNode = firstChildNode.getFirstChild();
					}else{
						//MAT-9070 changes
						errorMessages.add(name + " must contain both an Aggregate Function and a valid User Defined Function.");
						continue;
					}
				}				
				msrObsFunctionList.add(firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
				
				 if(expressionPopMap.containsKey(firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue())){
					 expressionPopMap.get(firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue()).add(name); 
				} else {
					List<String> populationTypes = new ArrayList<String>();
					populationTypes.add(name);
					expressionPopMap.put(firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue(), populationTypes); 
				}
				 
								
				String associatedClauseNodeUUID  = null;
				if(scoringType.equalsIgnoreCase(SCORING_CONTINUOUS_VARIABLE)){
					associatedClauseNodeUUID = findMeasurePopInGrouping(packageClauses);
				} else if(scoringType.equalsIgnoreCase(SCORING_RATIO)){
					associatedClauseNodeUUID = measurePackageClauseDetail.getAssociatedPopulationUUID();
				}
				if(associatedClauseNodeUUID !=null){
					Node associatedPopNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure//clause[@uuid='"+associatedClauseNodeUUID+"']");
					String definitionName = associatedPopNode.getFirstChild().getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
					moAssociatedPopUsedExpression.add(definitionName);
					
					 if(assoExpressionPopMap.containsKey(definitionName)){
						 assoExpressionPopMap.get(definitionName).add(name); 
					} else {
						List<String> populationTypes = new ArrayList<String>();
						populationTypes.add(name);
						assoExpressionPopMap.put(definitionName, populationTypes); 
					}
					
				}
				
			}else{
				
				//find cqldefinition here
				Node firstChildNode = clauseNode.getFirstChild();
				
				String definitionName = firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
				
				if(expressionPopMap.containsKey(definitionName)){
					expressionPopMap.get(definitionName).add(name);
				} else {
					List<String> populationTypes = new ArrayList<String>();
					populationTypes.add(name);
					expressionPopMap.put(definitionName, populationTypes);
				}
				
				if(!exprList.contains(definitionName)){
					exprList.add(definitionName);
				}
			}
		}
		
		if (errorMessages.isEmpty()) {
			//MAT-9070 changes
			List<String> usedExprList = new ArrayList<String>();
			usedExprList.addAll(exprList);
			usedExprList.addAll(msrObsFunctionList);
			usedExprList.addAll(moAssociatedPopUsedExpression);

			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureXmlL);

			SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, usedExprList);

			List<CQLExpressionObject> expressions = cqlResult.getCqlObject().getCqlDefinitionObjectList();
			List<CQLExpressionObject> expressionsToBeChecked = new ArrayList<CQLExpressionObject>();
			List<CQLExpressionObject> expressionsToBeCheckedForMO = new ArrayList<CQLExpressionObject>();

			for(CQLExpressionObject cqlExpressionObject : expressions){
				String name = cqlExpressionObject.getName();

				if(exprList.contains(name)){
					expressionsToBeChecked.add(cqlExpressionObject);
				}

				if(moAssociatedPopUsedExpression.contains(name)) {
					expressionsToBeCheckedForMO.add(cqlExpressionObject);
				}
			}

			Node patientBasedIndicatorNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_FOR_PATIENT_BASED_INDICATOR);
			String patientBasedIndicator = patientBasedIndicatorNode.getTextContent();
			boolean isPatientBasedIndicator = patientBasedIndicator.equals("true");
			
			if(isPatientBasedIndicator){

				//Check for MAT-8606 validations.			
				List<String> messages = checkReturnType(expressionsToBeChecked, CQL_RETURN_TYPE_BOOLEAN, expressionPopMap);
				if(messages.size() > 0){
					errorMessages.addAll(messages);
				}
				//Check for MAT-8622 Measure Observation and Patient-based Measure Indicator in Ratio scoring type.
				if(msrObsFunctionList.size() >0 && scoringType.equalsIgnoreCase(SCORING_RATIO) ){
					String message = MessageDelegate.getInstance().getEPISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE();
					errorMessages.add(message);
				}

			}else{

				//Check for MAT-8608 validations.			
				List<String> messages = checkSimilarReturnTypes(expressionsToBeChecked,expressionPopMap);
				if(messages.size() > 0){
					errorMessages.addAll(messages);
				}

			}
			//check for MAT-8627 validations for functions attached to Measure Observations.
			if(!isPatientBasedIndicator || !scoringType.equalsIgnoreCase(SCORING_RATIO)){
				List<CQLExpressionObject> functions = cqlResult.getCqlObject().getCqlFunctionObjectList();
				List<CQLExpressionObject> functionsToBeChecked = new ArrayList<CQLExpressionObject>();

				for(CQLExpressionObject cqlExpressionObject : functions){
					String name = cqlExpressionObject.getName();

					if(msrObsFunctionList.contains(name)){
						functionsToBeChecked.add(cqlExpressionObject);
					}
				}
				//MAT-8624 Single Argument Required for Measure Observation User-defined Function .
				List<String> moArgumentMessage = checkForMOFunctionArgumentCount(functionsToBeChecked, expressionsToBeCheckedForMO,expressionPopMap, assoExpressionPopMap);
				if(moArgumentMessage.size() > 0){
					errorMessages.addAll(moArgumentMessage);
				}

				List<String> messages = checkReturnType(functionsToBeChecked, CQL_RETURN_TYPE_NUMERIC, expressionPopMap);
				if(messages.size() > 0){
					errorMessages.addAll(messages);
				}

			}

		}	
			
		return errorMessages;
	}
	
	private static  String findMeasurePopInGrouping(List<MeasurePackageClauseDetail> packageClauses) {
		String measurePopUUID = null;
		for(MeasurePackageClauseDetail measurePackageClauseDetail : packageClauses){
			String type = measurePackageClauseDetail.getType();
			if(type.equalsIgnoreCase("measurePopulation")){
				measurePopUUID = measurePackageClauseDetail.getId();
			}
		}
		return measurePopUUID;
	}
	
	//MAT-8624 Single Argument Required for Measure Observation User-defined Function .
	private static List<String> checkForMOFunctionArgumentCount(List<CQLExpressionObject> functionsToBeChecked , List<CQLExpressionObject> associatedPopExpressionTobeChecked, Map<String, List<String>> expressionPopMap, Map<String, List<String>> assoExpressionPopMap) {
		List<String> returnMessages = new ArrayList<String>();
		List<String> expressionAlreadyEval = new ArrayList<String>();
		for(CQLExpressionObject cqlExpressionObject : functionsToBeChecked){
			List<CQLExpressionOprandObject> argumentList =  cqlExpressionObject.getOprandList();
			if(argumentList.isEmpty() || argumentList.size() > 1){
				if(!expressionAlreadyEval.contains(cqlExpressionObject.getName())){
					/*List<String> values = expressionPopMap.get(cqlExpressionObject.getName());
					for(int i=0;i<values.size();i++){
						String message = values.get(i) + " : " +MessageDelegate.getInstance().getMEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE();
						returnMessages.add(message);
					}*/
					
					List<String> generatedMessages = generateMessageList(cqlExpressionObject.getName(), expressionPopMap, MessageDelegate.getInstance().getMEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE());
					returnMessages.addAll(generatedMessages);
				}
				//returnMessages.add(MessageDelegate.getInstance().getMEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE());
				//This is commented as Sridhar and Gayathri thinks if illegal number of arguments are passed then this check should not be made.
				/*returnMessages.add("For an added measure observation, the argument in the user-defined function must match the return type"
						+ " of the definition directly applied to the Associated Population.");*/
			} else {
				String funcArgumentReturnType = argumentList.get(0).getReturnType();
				logger.info("funcArgumentReturnType Start ==========" + funcArgumentReturnType);
				// for now parser returns  positive with positive qdm data model and negative of positive for negative qdm data model for function argument return type.
				// Definition dont return positive or negative. It return only qdm data model. For comparison we have to drop positive/negative if it is present in return type.
				/*if(funcArgumentReturnType.contains(POSITIVE)){
					funcArgumentReturnType = funcArgumentReturnType.replaceAll(POSITIVE, "");
				} else if(funcArgumentReturnType.contains(NEGATIVE)){
					funcArgumentReturnType = funcArgumentReturnType.replaceAll(NEGATIVE, "");
				}*/
				for(CQLExpressionObject expressionObject : associatedPopExpressionTobeChecked){
					String returnTypeOfExpression = expressionObject.getReturnType();
					String expressionName = expressionObject.getName();
					//in case list<qdm.{data Model}> , comparison had to be with only qdm.{data Model}. So dropping list and < ,> from definition return type if exists.
					if(returnTypeOfExpression.contains("list")){
						int startIndex = returnTypeOfExpression.indexOf("<");
						int lastIndex = returnTypeOfExpression.indexOf(">");
						if(startIndex > 0 && lastIndex < returnTypeOfExpression.length()){
							returnTypeOfExpression = returnTypeOfExpression.substring(startIndex+1, lastIndex);
							logger.info("returnTypeOfExpression ==========" + returnTypeOfExpression);
						}
					}
					logger.info("funcArgumentReturnType ==========" + funcArgumentReturnType);
					if(!returnTypeOfExpression.equalsIgnoreCase(funcArgumentReturnType)){
						if(!expressionAlreadyEval.contains(expressionName)){
							/*List<String> values = assoExpressionPopMap.get(expressionName);
							for(int i=0;i<values.size();i++){
								String message = values.get(i) + " : " +MessageDelegate.getInstance().getMEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE();
								returnMessages.add(message);
							}*/
							List<String> generatedMessages = generateMessageList(expressionName, assoExpressionPopMap, MessageDelegate.getInstance().getMEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE());
							returnMessages.addAll(generatedMessages);
						}
						
						
						if (!expressionAlreadyEval.contains(expressionName)){
							expressionAlreadyEval.add(expressionName);
						}
						break;
					}
					
				}
				
			}
			if (!expressionAlreadyEval.contains(cqlExpressionObject.getName())){
				expressionAlreadyEval.add(cqlExpressionObject.getName());
			}
		}
		return returnMessages;
	}

	private static List<String> checkSimilarReturnTypes(
			List<CQLExpressionObject> expressionsToBeChecked, Map<String, List<String>> expressionPopMap ) {
		
		List<String> returnMessages = new ArrayList<String>();
		List<String>expressionAlreadyEval = new ArrayList<String>();
		String returnType = null;
		
		for(CQLExpressionObject cqlExpressionObject : expressionsToBeChecked){

			logger.info("Return type for "+cqlExpressionObject.getName()+" is "+cqlExpressionObject.getReturnType());
			String expressionReturnType = cqlExpressionObject.getReturnType();
			boolean isList = expressionReturnType.toLowerCase().startsWith("list");
			
			if(!isList && !expressionAlreadyEval.contains(cqlExpressionObject.getName())){
				/*List<String> values = expressionPopMap.get(cqlExpressionObject.getName());
				for(int i=0;i<values.size();i++){
					String message = values.get(i) + " : " +MessageDelegate.getInstance().getEPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE();
					returnMessages.add(message);
				}*/
				List<String> generatedMessages = generateMessageList(cqlExpressionObject.getName(), expressionPopMap, MessageDelegate.getInstance().getEPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE());
				returnMessages.addAll(generatedMessages);
			}else{
				if(returnType == null){
					returnType = expressionReturnType;
				}else if(!returnType.equals(expressionReturnType) && !expressionAlreadyEval.contains(cqlExpressionObject.getName())){
					/*List<String> values = expressionPopMap.get(cqlExpressionObject.getName());
					for(int i=0;i<values.size();i++){
						String message = values.get(i) + " : " +MessageDelegate.getInstance().getEPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE();
						returnMessages.add(message);
					}*/
					List<String> generatedMessages = generateMessageList(cqlExpressionObject.getName(), expressionPopMap, MessageDelegate.getInstance().getEPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE());
					returnMessages.addAll(generatedMessages);

				}
			}
			if (!expressionAlreadyEval.contains(cqlExpressionObject.getName())){
				expressionAlreadyEval.add(cqlExpressionObject.getName());
			}
		}
		
		return returnMessages;
	}

	private static List<String> checkReturnType(
			List<CQLExpressionObject> expressionsToBeChecked, String returnTypeCheck, Map<String, List<String>> expressionPopMap) {
		
		List<String> returnMessages = new ArrayList<String>();
		List<String>expressionAlreadyEval = new ArrayList<String>();
		
		for(CQLExpressionObject cqlExpressionObject : expressionsToBeChecked){
			
			/*
			 * Angular brackets "<" and ">" are filtered out by Javascript/mark-up hack validators.
			 * Replace angular brackets "<" and ">" by square brackets "[" and "]".
			*/ 
			String returnType = cqlExpressionObject.getReturnType();
			returnType = returnType.replaceAll("<", "[");
			returnType = returnType.replaceAll(">", "]");
			
			//check for return type to be "System.Boolean"
			if(returnTypeCheck.equals(CQL_RETURN_TYPE_BOOLEAN)){
				
				if(!cqlExpressionObject.getReturnType().equals(SYSTEM_BOOLEAN) && !expressionAlreadyEval.contains(cqlExpressionObject.getName())){
					/*List<String> values = expressionPopMap.get(cqlExpressionObject.getName());
					for(int i=0;i<values.size();i++){
						String message = values.get(i) + " : " +MessageDelegate.getInstance().getPATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE();
						returnMessages.add(message);
					}*/
					
					List<String> generatedMessages = generateMessageList(cqlExpressionObject.getName(), expressionPopMap, MessageDelegate.getInstance().getPATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE());
					returnMessages.addAll(generatedMessages);
				}
				
			}else if(returnTypeCheck.equals(CQL_RETURN_TYPE_NUMERIC) && !expressionAlreadyEval.contains(cqlExpressionObject.getName())){
				String exprReturnType = cqlExpressionObject.getReturnType();
				
				if(!exprReturnType.equals(SYSTEM_INTEGER) && 
						!exprReturnType.equals(SYSTEM_DECIMAL) && 
							!exprReturnType.equals(SYSTEM_QUANTITY)){
					/*List<String> values = expressionPopMap.get(cqlExpressionObject.getName());
					for(int i=0;i<values.size();i++){
						String message = values.get(i) + " : " +MessageDelegate.getInstance().getMEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE();
						returnMessages.add(message);
					}*/
					
					List<String> generatedMessages = generateMessageList(cqlExpressionObject.getName(), expressionPopMap, MessageDelegate.getInstance().getMEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE());
					returnMessages.addAll(generatedMessages);
				}
			}
			if (!expressionAlreadyEval.contains(cqlExpressionObject.getName())){
				expressionAlreadyEval.add(cqlExpressionObject.getName());
			}
		}		
		return returnMessages;
	}
	
	private static List<String> generateMessageList(String expressionName, Map<String,List<String>>expressionPopMap, String errorMessage){
		List<String> returnMessages = new ArrayList<String>();
		List<String> values = expressionPopMap.get(expressionName);
		
		for(int i=0;i<values.size();i++){
			String message = values.get(i) + " : " +errorMessage;
			returnMessages.add(message);
		}
		
		return returnMessages;
		
	}

}
