package mat.measure.measuredetails.models;

import java.util.List;

public class DenominatorExceptionsModel extends MeasureDetailsRichTextAbstractModel {
	
	public DenominatorExceptionsModel() {
		super("", "");
	}
	
	public DenominatorExceptionsModel(DenominatorExceptionsModel model) {
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
