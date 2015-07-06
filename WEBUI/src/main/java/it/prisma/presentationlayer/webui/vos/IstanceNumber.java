package it.prisma.presentationlayer.webui.vos;


public enum IstanceNumber {

	ONE_ISTANCE(1, "1istanza"), 
	TWO_INSTANCE(2, "2istanze"),
	THREE_INSTANCE(3, "3istanze"), 
	FOUR_INSTANCE(4, "4istanze");

	private int number;
	private String text;

	private IstanceNumber(int number, String text) {
		this.number = number;
		this.text = text;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static IstanceNumber lookupFromCode(int number) {
		for (IstanceNumber n : values()) {
			if (n.number == number) {
				return n;
			}
		}
		throw new IllegalArgumentException("Can not find IstanceNumber with number: "	+ number);
	}

	public static IstanceNumber lookupFromName(String text) {
		for (IstanceNumber n : values()) {
			if (text.equals(n.getText())) {
				return n;
			}
		}
		throw new IllegalArgumentException("Cannot find IstanceNumber with text: " + text);
	}
}
