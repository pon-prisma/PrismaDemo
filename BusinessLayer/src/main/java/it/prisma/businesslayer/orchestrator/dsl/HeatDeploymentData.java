package it.prisma.businesslayer.orchestrator.dsl;

import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;

import org.openstack4j.model.heat.Stack;

public class HeatDeploymentData extends DeployerDeploymentData {

	private String templateName;
	private String templateVersion;
	private String templateURL;
	private Stack stack;

	public Stack getStack() {
		return stack;
	}

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateVersion() {
		return templateVersion;
	}

	public void setTemplateVersion(String templateVersion) {
		this.templateVersion = templateVersion;
	}

	public String getTemplateURL() {
		return templateURL;
	}

	public void setTemplateURL(String templateURL) {
		this.templateURL = templateURL;
	}

	@Override
	public DeployerType getDeployerType() {
		return DeployerType.HEAT;
	}
}
