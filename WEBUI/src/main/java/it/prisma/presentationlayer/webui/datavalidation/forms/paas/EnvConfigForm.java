package it.prisma.presentationlayer.webui.datavalidation.forms.paas;

import it.prisma.utils.validation.RegularExpressionList;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;



public class EnvConfigForm  implements Serializable {
	
	private static final long serialVersionUID = -7498337923836451674L;
	
	@NotNull
	private String serviceName;	

	@NotNull
	private String domainName;
	
	@Pattern(regexp = RegularExpressionList.EMAIL_PATTERN)
	private String notificationEmail;

//	
//	@NotNull
//	private String qosSelect;
//	
//	@NotNull
//	private String flavorSelect;
//	
//	@NotNull
//	private String scalingSelect;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getDomainName() {
		return domainName;
	}	

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}



//
//	public String getQosSelect() {
//		return qosSelect;
//	}
//
//	public void setQosSelect(String qosSelect) {
//		this.qosSelect = qosSelect;
//	}
//
//	public String getFlavorSelect() {
//		return flavorSelect;
//	}
//
//	public void setFlavorSelect(String flavorSelect) {
//		this.flavorSelect = flavorSelect;
//	}
//
//	public String getScalingSelect() {
//		return scalingSelect;
//	}
//
//	public void setScalingSelect(String scalingSelect) {
//		this.scalingSelect = scalingSelect;
//	}
	
	
}
