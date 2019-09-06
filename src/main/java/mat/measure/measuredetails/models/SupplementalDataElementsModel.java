package mat.measure.measuredetails.models;

import java.util.List;

public class SupplementalDataElementsModel extends MeasureDetailsRichTextAbstractModel {
	public SupplementalDataElementsModel() {
		super("", "");
	}
	
	public SupplementalDataElementsModel(SupplementalDataElementsModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}

	@Override
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.isDirty(this);
	}
}
