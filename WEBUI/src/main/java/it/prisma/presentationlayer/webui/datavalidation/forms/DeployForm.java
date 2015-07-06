package it.prisma.presentationlayer.webui.datavalidation.forms;

import it.prisma.utils.validation.RegularExpressionList;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <b>DeployForm</b> is the abstract class that contains the common properties
 * of each service
 * 
 * @author <a href="mailto:m.bassi@reply.it">Matteo Bassi</a>
 * @version 0.0.7
 * @since 0.0.7
 * @see <a
 *      href="http://www.ponsmartcities-prisma.it/">Progetto
 *      PRISMA</a>
 * 
 */
public abstract class DeployForm implements IDeployForm, Serializable {

	private static final long serialVersionUID = -652876591763935350L;

	/****************************************************************************************
	 * 										FIRST TAB	 									*
	 ****************************************************************************************/

	@Size(min = 3, max = 16)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_HYPHEN_PATTERN)
	private String serviceName;

	@Size(min = 4, max = 400)
	private String description;

	@Size(min = 1, max = 100)
	@Pattern(regexp = RegularExpressionList.URL_PATTERN)
	private String url;

	@NotNull
	private String domainName;

	@Pattern(regexp = RegularExpressionList.EMAIL_PATTERN)
	private String notificationEmail;

	/****************************************************************************************
	 * 								SECOND TAB 												*
	 ****************************************************************************************/

	@NotNull
	private String zoneSelect;

	@NotNull
	private String qosSelect;

	@NotNull
	private String flavorSelect;

	/**************************************************************************************/

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getZoneSelect() {
		return zoneSelect;
	}

	public void setZoneSelect(String zoneSelect) {
		this.zoneSelect = zoneSelect;
	}

	public String getQosSelect() {
		return qosSelect;
	}

	public void setQosSelect(String qosSelect) {
		this.qosSelect = qosSelect;
	}

	public String getFlavorSelect() {
		return flavorSelect;
	}

	public void setFlavorSelect(String flavorSelect) {
		this.flavorSelect = flavorSelect;
	}

}