package it.prisma.presentationlayer.webui.vos.accounting;

public enum WGPrivileges {


	WG_ADMIN("WG Admin"), WG_USER("WG User");

	private String privilegeName;

	public static WGPrivileges getPrivilegeFromPrivilegeString(String privilegeName)
	{
		switch (privilegeName)
		{
			case "WG Admin":
				return WG_ADMIN;
			case "WG User":
				return WG_USER;
			default:
				return null;
		}		
	}
	
	public String getPrivilegeNameString()
	{
		return this.privilegeName;
	}
	
	WGPrivileges(final String privilegeName) {
		this.privilegeName = privilegeName;
	}

}

