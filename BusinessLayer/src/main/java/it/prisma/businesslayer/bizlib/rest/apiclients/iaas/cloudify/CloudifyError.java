package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify;

public class CloudifyError {
	private String messageId;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "CloudifyError [messageId=" + messageId + "]";
	}		
}