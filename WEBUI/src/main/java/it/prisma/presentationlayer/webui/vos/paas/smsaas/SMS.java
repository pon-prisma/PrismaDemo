package it.prisma.presentationlayer.webui.vos.paas.smsaas;

public class SMS {

	private int id;
	private String recipient;
	private String text;
	private String date;
	private String sender;

	public SMS(int id, String recipient, String text, String date, String sender){
		this.id = id;
		this.recipient = recipient;
		this.text = text;
		this.date = date;
		this.sender = sender;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
}
