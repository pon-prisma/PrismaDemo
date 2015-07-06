/*
 * Copyright 2014 PRISMA by MIUR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.prisma.presentationlayer.webui.datavalidation.forms.paas;

import it.prisma.utils.validation.RegularExpressionList;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 
 * @author <a href="mailto:m.bassi@reply.it">Matteo Bassi</a>
 * @version 0.1.0
 * @since 0.1.0
 * @see <a
 *      href="http://www.istc.cnr.it/project/prisma-piattaforme-cloud-interoperabili-smart-government">Progetto
 *      PRISMA</a>
 */
public class APPaaSForm implements Serializable {

	private static final long serialVersionUID = 6400126423611738676L;

	@Size(min = 3, max = 16)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_HYPHEN_PATTERN)
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
	
	
	
	
	
	/*------------------------------------------------
	---------   INFORMAZIONI GENERALI  -------------
	--------------------------------------------
	@Size(min = 4, max = 30)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_DOT_UNDERSCORE_HYPHEN_PATTERN)
	private String applicationName;
	
	@Size(min = 4, max = 200)
	private String appDescription;
	
	
	--------------------------------------------
	---------   DETTAGLI AMBIENTE  -------------
	--------------------------------------------
	@Size(min = 4, max = 30)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_DOT_UNDERSCORE_HYPHEN_PATTERN)
	private String environmentName;
	
	@Size(min = 4, max = 200)
	private String environmentDescription;
	
	
	private String secureConnection;
	
	
	
	------------------------------------------
	---------   TIPOLOGIA SERVER   -----------
	------------------------------------------
	@NotNull
	private String serverType;
	
	@NotNull
	private String serverName;

	@NotNull
	private String loadBal;
	
	
	
	------------------------------------------------
	------------   SELZIONA SORGENTE  --------------
	------------------------------------------------
	@NotNull
	private String source;
	
	private String sourceLabel;
	private String appVisibility;
	
	private String urlStorage;
	private String sourceLabelPRISMA;
	private String appVisibilityPRISMA;
	
	
	-------------------------------------------------
	-----   DETTAGLI DI CONFIGURAZIONETE   ----------
	-------------------------------------------------
	@NotNull
	private String instanceType;
	
	@Email
	private String notificationEmail;
	
	@NotNull
	private String securityGroup;

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getEnvironmentDescription() {
		return environmentDescription;
	}

	public void setEnvironmentDescription(String environmentDescription) {
		this.environmentDescription = environmentDescription;
	}

	public String getSecureConnection() {
		return secureConnection;
	}

	public void setSecureConnection(String secureConnection) {
		this.secureConnection = secureConnection;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getLoadBal() {
		return loadBal;
	}

	public void setLoadBal(String loadBal) {
		this.loadBal = loadBal;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceLabel() {
		return sourceLabel;
	}

	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}

	public String getAppVisibility() {
		return appVisibility;
	}

	public void setAppVisibility(String appVisibility) {
		this.appVisibility = appVisibility;
	}

	public String getUrlStorage() {
		return urlStorage;
	}

	public void setUrlStorage(String urlStorage) {
		this.urlStorage = urlStorage;
	}

	public String getSourceLabelPRISMA() {
		return sourceLabelPRISMA;
	}

	public void setSourceLabelPRISMA(String sourceLabelPRISMA) {
		this.sourceLabelPRISMA = sourceLabelPRISMA;
	}

	public String getAppVisibilityPRISMA() {
		return appVisibilityPRISMA;
	}

	public void setAppVisibilityPRISMA(String appVisibilityPRISMA) {
		this.appVisibilityPRISMA = appVisibilityPRISMA;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	public String getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	*/
}
