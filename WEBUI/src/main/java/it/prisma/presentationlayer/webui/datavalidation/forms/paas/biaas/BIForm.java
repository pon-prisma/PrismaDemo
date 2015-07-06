package it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas;

import it.prisma.presentationlayer.webui.datavalidation.forms.DeployForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.IDeployForm;

public class BIForm extends DeployForm implements IDeployForm {

	private static final long serialVersionUID = 8437668666167163018L;

	@Override
	public String toString() {
		return "BIForm [getServiceName()=" + getServiceName()
				+ ", getDescription()=" + getDescription() + ", getUrl()="
				+ getUrl() + ", getDomainName()=" + getDomainName()
				+ ", getNotificationEmail()=" + getNotificationEmail()
				+ ", getZoneSelect()=" + getZoneSelect() + ", getQosSelect()="
				+ getQosSelect() + ", getFlavorSelect()=" + getFlavorSelect()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
