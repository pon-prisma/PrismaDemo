package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

public class OpenstackError {
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