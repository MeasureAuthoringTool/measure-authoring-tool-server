package mat.dao.clause;


import java.util.List;

import mat.dao.IDAO;
import mat.model.component.ComponentMeasure;

public interface ComponentMeasuresDAO extends IDAO<ComponentMeasure, String> {
	
	public void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList);
	
	public void updateComponentMeasures(String measureId, List<ComponentMeasure> componentMeasuresList);

	public List<ComponentMeasure> findByComponentMeasureId(String measureId);

}
