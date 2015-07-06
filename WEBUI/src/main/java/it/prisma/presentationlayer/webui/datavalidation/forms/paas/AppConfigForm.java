package it.prisma.presentationlayer.webui.datavalidation.forms.paas;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class AppConfigForm  implements Serializable {
	
	private static final long serialVersionUID = -7498337923836451674L;
	
	@NotNull
	private String serviceName;	

	@Size(min = 4, max = 200)
	private String description;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
