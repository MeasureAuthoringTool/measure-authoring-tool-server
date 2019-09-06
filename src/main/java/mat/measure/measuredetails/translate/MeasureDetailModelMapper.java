package mat.measure.measuredetails.translate;

import mat.model.measuredetail.ManageMeasureDetailModel;
import mat.measure.measuredetails.models.MeasureDetailsModel;

public interface MeasureDetailModelMapper {
	ManageMeasureDetailModel convertMeasureDetailsToManageMeasureDetailModel();
	MeasureDetailsModel getMeasureDetailsModel(boolean isCompositeMeasure);
}
