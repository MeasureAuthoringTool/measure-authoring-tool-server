package mat.result;

import java.util.List;

public class GenericResult  {
	private boolean success;
	private int failureReason;
	private List<String> messages;

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(int failureReason) {
		this.failureReason = failureReason;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public List<String> getMessages() {
		return messages;
	}
}
