package mat.service.packager;

import mat.measurepackage.MeasurePackageDetail;
import mat.measurepackage.MeasurePackageOverview;
import mat.result.MeasurePackageSaveResult;
import mat.error.packager.SaveRiskAdjustmentVariableException;
import mat.error.packager.SaveSupplementalDataElementException;

import javax.xml.xpath.XPathExpressionException;

/**
 * The Interface PackagerService.
 */
public interface PackagerService {
	
	/**
	 * Builds the overview for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure package overview
	 */
	public MeasurePackageOverview buildOverviewForMeasure(String measureId);
	
	/**
	 * Save.
	 *
	 * @param detail            the detail
	 * @return the measure package save result
	 */
	public MeasurePackageSaveResult save(MeasurePackageDetail detail, String loggedInUserId, String scoring);
	
	/**
	 * Delete.
	 * 
	 * @param detail
	 *            the detail
	 */
	public void delete(MeasurePackageDetail detail);
	
	/**
	 * Save qdm data.
	 * 
	 * @param detail
	 *            the detail
	 * @throws SaveSupplementalDataElementException 
	 * @throws XPathExpressionException 
	 */
	public void saveQDMData(MeasurePackageDetail detail) throws SaveSupplementalDataElementException;

	/**
	 * Save risk variables.
	 *
	 * @param detail the detail
	 * @throws XPathExpressionException 
	 * @throws SaveRiskAdjustmentVariableException 
	 */
	void saveRiskAdjVariables(MeasurePackageDetail detail) throws SaveRiskAdjustmentVariableException;

    MeasurePackageSaveResult save(MeasurePackageDetail detail);
}
