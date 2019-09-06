package mat.api.measures;

import org.hibernate.ObjectNotFoundException;

import mat.api.measures.dto.AdvancedMeasureDTO;
import mat.dao.measure.MeasureDAO;
import mat.entity.SecurityRole;
import mat.model.measure.Measure;
import mat.model.measure.MeasureShare;
import mat.service.impl.MatContextServiceUtil;

public class MeasuresAPIUtil {
	
	private MeasuresAPIUtil() {
		throw new IllegalStateException("Measures API Util");
	}
		
	public static boolean doesMeasureExist(Measure measure) {
		try {
			if(measure.getDescription() == null) {
				return false;
			}
        } catch (ObjectNotFoundException e) {
        	return false;
        }
		return true;
	}
	
	public static boolean canUserViewMeasure(String currentUserId, String currentUserRole, Measure measure) {
		if(measure.getIsPrivate() && !currentUserRole.equals(SecurityRole.SUPER_USER_ROLE)) {
            if(!currentUserId.equals(measure.getOwner().getId())) {
            	return false;
            }

            boolean isMeasureSharedWithCurrentUser = false;
            for(MeasureShare share : measure.getShares()) {
                if(currentUserId.equals(share.getShareUser().getId())) {
                   isMeasureSharedWithCurrentUser = true;
                }
            }

            if(!isMeasureSharedWithCurrentUser) {
            	return false;
            }
        }
		
		return true;
	}
	
	public static AdvancedMeasureDTO makeMeasureDTO(MeasureDAO measureDAO, Measure measure) {
		boolean isMeasureEditable = MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measure.getId(), true);
		boolean isMeasureCloneable = MatContextServiceUtil.get().isCurrentMeasureClonable(measureDAO, measure.getId());
		boolean isMeasureDraftable = MatContextServiceUtil.get().isMeasureDraftable(measureDAO, measure);
		boolean isMeasureExportable = measure.getExportedDate() != null;
		AdvancedMeasureDTO measureDTO = new AdvancedMeasureDTO(measure, isMeasureEditable, isMeasureDraftable, isMeasureCloneable, isMeasureExportable);
		return measureDTO;
	}
}
