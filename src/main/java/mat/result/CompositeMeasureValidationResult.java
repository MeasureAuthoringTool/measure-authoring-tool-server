package mat.result;

import java.util.List;

import mat.measure.measuredetails.models.ManageCompositeMeasureDetailModel;

public class CompositeMeasureValidationResult {

	private ManageCompositeMeasureDetailModel model;
	List<String> messages;
	
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public ManageCompositeMeasureDetailModel getModel() {
		return model;
	}
	public void setModel(ManageCompositeMeasureDetailModel model) {
		this.model = model;
	}

	
}
