package mat.service.measure;

import mat.error.MatException;
import mat.model.measure.ManageMeasureSearchModel;
import mat.model.measuredetail.ManageMeasureDetailModel;

public interface MeasureCloningService {
	ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, String loggedinUserId, boolean creatingDraft) throws MatException;
}

