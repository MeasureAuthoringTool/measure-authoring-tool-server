package mat.model.humanreadable;

import java.util.List;

public class HumanReadableMeasureInformationModel {
	private String ecqmTitle; 
	private String ecqmIdentifier;
	private String ecqmVersionNumber; 
	private String nqfNumber; 
	private String guid; 
	private String measurementPeriod; 
	private boolean isCalendarYear; 
	private String measurementPeriodStartDate;
	private String measurementPeriodEndDate;
	private String measureSteward; 
	private List<String> measureDevelopers; 
	private String endorsedBy; 
	private String description; 
	private String copyright; 
	private String disclaimer; 
	private String compositeScoringMethod;
	private String measureScoring; 
	private List<String> measureTypes; 
	private List<HumanReadableComponentMeasureModel> componentMeasures; 
	private String stratification; 
	private String riskAdjustment; 
	private String rateAggregation; 
	private String rationale; 
	private String clinicalRecommendationStatement; 
	private String improvementNotation; 
	private List<String> references; 
	private String definition; 
	private String guidance; 
	private String transmissionFormat; 
	private String initialPopulation; 
	private String denominator;
	private String denominatorExclusions; 
	private String denominatorExceptions; 
	private String numerator; 
	private String numeratorExclusions; 
	private String measurePopulation; 
	private String measurePopulationExclusions; 
	private String measureObservations; 
	private String supplementalDataElements;
	private String measureSet;
	private boolean patientBased; 
 
	public HumanReadableMeasureInformationModel() {
		
	}
	
	public HumanReadableMeasureInformationModel(String eCQMTitle, String eCQMIdentifier, String eCQMVersionNumber, String nqfNumber,
			String guid, String measurementPeriodStartDate, String measurementPeriodEndDate, boolean isCalendarYear,
			String measureSteward, List<String> measureDevelopers, String endorsedBy,
			String description, String copyright, String disclaimer, String compositeScoringMethod, String measureScoring, List<String> measureTypes,
			List<HumanReadableComponentMeasureModel> componentMeasures, String stratification, String riskAdjustment, String rateAggregation, String rationale,
			String clinicalRecommendationStatement, String improvementNotation, List<String> references, String definition,
			String guidance, String transmissionFormat, String initialPopulation, String denominator,
			String denominatorExclusions, String denominatorExceptions, String numerator, String numeratorExclusions,
			String measurePopulation, String measurePopulationExclusions, String measureObservations,
			String supplementalDataElements, String measureSet, boolean patientBased) {
		this.ecqmTitle = eCQMTitle;
		this.ecqmIdentifier = eCQMIdentifier;
		this.ecqmVersionNumber = eCQMVersionNumber;
		this.nqfNumber = nqfNumber;
		this.guid = guid;
		this.isCalendarYear = isCalendarYear;
		this.measurementPeriodStartDate = measurementPeriodStartDate; 
		this.measurementPeriodEndDate = measurementPeriodEndDate;
		this.measureSteward = measureSteward;
		this.measureDevelopers = measureDevelopers;
		this.endorsedBy = endorsedBy;
		this.description = description;
		this.copyright = copyright;
		this.disclaimer = disclaimer;
		this.compositeScoringMethod = compositeScoringMethod;
		this.measureScoring = measureScoring;
		this.measureTypes = measureTypes;
		this.componentMeasures = componentMeasures;
		this.stratification = stratification;
		this.riskAdjustment = riskAdjustment;
		this.rateAggregation = rateAggregation;
		this.rationale = rationale;
		this.clinicalRecommendationStatement = clinicalRecommendationStatement;
		this.improvementNotation = improvementNotation;
		this.references = references;
		this.definition = definition;
		this.guidance = guidance;
		this.transmissionFormat = transmissionFormat;
		this.initialPopulation = initialPopulation;
		this.denominator = denominator;
		this.denominatorExclusions = denominatorExclusions;
		this.denominatorExceptions = denominatorExceptions;
		this.numerator = numerator;
		this.numeratorExclusions = numeratorExclusions;
		this.measurePopulation = measurePopulation;
		this.measurePopulationExclusions = measurePopulationExclusions;
		this.measureObservations = measureObservations;
		this.supplementalDataElements = supplementalDataElements;
		this.measureSet = measureSet; 
		this.patientBased = patientBased;
	}

	public String getEcqmTitle() {
		return ecqmTitle;
	}

	public void setEcqmTitle(String ecqmTitle) {
		this.ecqmTitle = ecqmTitle;
	}

	public String getEcqmIdentifier() {
		return ecqmIdentifier;
	}

	public void setEcqmIdentifier(String ecqmIdentifier) {
		this.ecqmIdentifier = ecqmIdentifier;
	}

	public String getEcqmVersionNumber() {
		return ecqmVersionNumber;
	}

	public void setEcqmVersionNumber(String ecqmVersionNumber) {
		this.ecqmVersionNumber = ecqmVersionNumber;
	}

	public String getNqfNumber() {
		return nqfNumber;
	}
	public void setNqfNumber(String nafNumber) {
		this.nqfNumber = nafNumber;
	}
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getMeasurementPeriod() {
		return measurementPeriod;
	}
	public void setMeasurementPeriod(String measurementPeriod) {
		this.measurementPeriod = measurementPeriod;
	}
	public boolean getIsCalendarYear() {
		return isCalendarYear;
	}

	public void setIsCalendarYear(boolean isCalendarYear) {
		this.isCalendarYear = isCalendarYear;
	}

	public String getMeasurementPeriodStartDate() {
		return measurementPeriodStartDate;
	}

	public void setMeasurementPeriodStartDate(String measurementPeriodStartDate) {
		this.measurementPeriodStartDate = measurementPeriodStartDate;
	}

	public String getMeasurementPeriodEndDate() {
		return measurementPeriodEndDate;
	}

	public void setMeasurementPeriodEndDate(String measurementPeriodEndDate) {
		this.measurementPeriodEndDate = measurementPeriodEndDate;
	}

	public String getMeasureSteward() {
		return measureSteward;
	}
	public void setMeasureSteward(String measureSteward) {
		this.measureSteward = measureSteward;
	}
	public List<String> getMeasureDevelopers() {
		return measureDevelopers;
	}
	public void setMeasureDevelopers(List<String> measureDevelopers) {
		this.measureDevelopers = measureDevelopers;
	}
	public String getEndorsedBy() {
		return endorsedBy;
	}
	public void setEndorsedBy(String endorsedBy) {
		this.endorsedBy = endorsedBy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	public String getCompositeScoringMethod() {
		return compositeScoringMethod;
	}

	public void setCompositeScoringMethod(String compositeScoring) {
		this.compositeScoringMethod = compositeScoring;
	}

	public String getMeasureScoring() {
		return measureScoring;
	}
	public void setMeasureScoring(String measureScoring) {
		this.measureScoring = measureScoring;
	}
	public List<String> getMeasureTypes() {
		return measureTypes;
	}
	public void setMeasureTypes(List<String> measureTypes) {
		this.measureTypes = measureTypes;
	}
	public List<HumanReadableComponentMeasureModel> getComponentMeasures() {
		return componentMeasures;
	}

	public void setComponentMeasures(List<HumanReadableComponentMeasureModel> compositeMeasures) {
		this.componentMeasures = compositeMeasures;
	}

	public String getStratification() {
		return stratification;
	}
	public void setStratification(String stratification) {
		this.stratification = stratification;
	}
	public String getRiskAdjustment() {
		return riskAdjustment;
	}
	public void setRiskAdjustment(String riskAdjustment) {
		this.riskAdjustment = riskAdjustment;
	}
	public String getRateAggregation() {
		return rateAggregation;
	}
	public void setRateAggregation(String rateAggregation) {
		this.rateAggregation = rateAggregation;
	}
	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = rationale;
	}
	public String getClinicalRecommendationStatement() {
		return clinicalRecommendationStatement;
	}
	public void setClinicalRecommendationStatement(String clinicalRecommendationStatement) {
		this.clinicalRecommendationStatement = clinicalRecommendationStatement;
	}
	public String getImprovementNotation() {
		return improvementNotation;
	}
	public void setImprovementNotation(String improvementNotation) {
		this.improvementNotation = improvementNotation;
	}
	public List<String> getReferences() {
		return references;
	}
	public void setReferences(List<String> references) {
		this.references = references;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getGuidance() {
		return guidance;
	}
	public void setGuidance(String guidance) {
		this.guidance = guidance;
	}
	public String getTransmissionFormat() {
		return transmissionFormat;
	}
	public void setTransmissionFormat(String transmissionFormat) {
		this.transmissionFormat = transmissionFormat;
	}
	public String getInitialPopulation() {
		return initialPopulation;
	}
	public void setInitialPopulation(String initialPopulation) {
		this.initialPopulation = initialPopulation;
	}
	public String getDenominator() {
		return denominator;
	}
	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}
	public String getDenominatorExclusions() {
		return denominatorExclusions;
	}
	public void setDenominatorExclusions(String denominatorExclusions) {
		this.denominatorExclusions = denominatorExclusions;
	}
	public String getDenominatorExceptions() {
		return denominatorExceptions;
	}
	public void setDenominatorExceptions(String denominatorExceptions) {
		this.denominatorExceptions = denominatorExceptions;
	}
	public String getNumerator() {
		return numerator;
	}
	public void setNumerator(String numerator) {
		this.numerator = numerator;
	}
	public String getNumeratorExclusions() {
		return numeratorExclusions;
	}
	public void setNumeratorExclusions(String numeratorExclusions) {
		this.numeratorExclusions = numeratorExclusions;
	}
	public String getMeasurePopulation() {
		return measurePopulation;
	}
	public void setMeasurePopulation(String measurePopulation) {
		this.measurePopulation = measurePopulation;
	}
	public String getMeasurePopulationExclusions() {
		return measurePopulationExclusions;
	}
	public void setMeasurePopulationExclusions(String measurePopulationExclusions) {
		this.measurePopulationExclusions = measurePopulationExclusions;
	}
	public String getMeasureObservations() {
		return measureObservations;
	}
	public void setMeasureObservations(String measureObservations) {
		this.measureObservations = measureObservations;
	}
	public String getSupplementalDataElements() {
		return supplementalDataElements;
	}
	public void setSupplementalDataElements(String supplementalDataElements) {
		this.supplementalDataElements = supplementalDataElements;
	}

	public String getMeasureSet() {
		return measureSet;
	}

	public void setMeasureSet(String measureSet) {
		this.measureSet = measureSet;
	}

	public boolean getPatientBased() {
		return patientBased;
	}

	public void setPatientBased(boolean patientBased) {
		this.patientBased = patientBased;
	} 
	
	
}
