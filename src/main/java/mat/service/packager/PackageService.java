package mat.service.packager;

import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import mat.measurepackage.MeasurePackageDetail;
import mat.model.measure.ManageMeasureSearchModel;
import mat.measurepackage.MeasurePackageOverview;
import mat.result.MeasurePackageSaveResult;
import mat.error.MatException;
import mat.error.packager.SaveRiskAdjustmentVariableException;
import mat.error.packager.SaveSupplementalDataElementException;


public interface PackageService {
	
	/**
	 * Gets the clauses and packages for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the clauses and packages for measure
	 */
	public MeasurePackageOverview getClausesAndPackagesForMeasure(String measureId);
	
	/**
	 * Gets the clauses and packages for measure list.
	 * 
	 * @param measureList
	 *            the List of measures
	 * @return the clauses and packages for measures
	 */
	public Map<String, MeasurePackageOverview> getClausesAndPackagesForMeasures(List<ManageMeasureSearchModel.Result> measureList);
	
	/**
	 * Save.
	 *
	 * @param detail            the detail
	 * @return the measure package save result
	 * @throws MatException             the mat exception
	 */
	public MeasurePackageSaveResult save(MeasurePackageDetail detail) throws MatException;
	
	/**
	 * Delete.
	 * 
	 * @param pkg
	 *            the pkg
	 */
	public void delete(MeasurePackageDetail pkg);
	
	/**
	 * Save qdm data.
	 * 
	 * @param detail
	 *            the detail
	 * @throws MatException
	 *             the mat exception
	 * @throws SaveSupplementalDataElementException 
	 * @throws XPathExpressionException 
	 */
	public void saveQDMData(MeasurePackageDetail detail) throws MatException, SaveSupplementalDataElementException;

	/**
	 * Save risk variables.
	 *
	 * @param currentDetail the current detail
	 * @throws SaveRiskAdjustmentVariableException 
	 * @throws XPathExpressionException 
	 */
	void saveRiskVariables(MeasurePackageDetail currentDetail) throws SaveRiskAdjustmentVariableException;
}
