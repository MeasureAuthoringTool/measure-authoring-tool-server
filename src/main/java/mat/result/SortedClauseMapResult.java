package mat.result;

import java.util.LinkedHashMap;

import mat.model.measure.MeasureXmlModel;
public class SortedClauseMapResult {

	/** The measure xml model. */
	private MeasureXmlModel measureXmlModel;

	/**
	 * Gets the measure xml model.
	 *
	 * @return the measure xml model
	 */
	public MeasureXmlModel getMeasureXmlModel() {
		return measureXmlModel;
	}

	/**
	 * Sets the measure xml model.
	 *
	 * @param measureXmlModel the new measure xml model
	 */
	public void setMeasureXmlModel(MeasureXmlModel measureXmlModel) {
		this.measureXmlModel = measureXmlModel;
	}

	/**
	 * Gets the clause map.
	 *
	 * @return the clause map
	 */
	public LinkedHashMap<String, String> getClauseMap() {
		return clauseMap;
	}

	/**
	 * Sets the clause map.
	 *
	 * @param clauseMap the clause map
	 */
	public void setClauseMap(LinkedHashMap<String, String> clauseMap) {
		this.clauseMap = clauseMap;
	}

	/** The clause map. */
	private LinkedHashMap<String, String> clauseMap;

}
