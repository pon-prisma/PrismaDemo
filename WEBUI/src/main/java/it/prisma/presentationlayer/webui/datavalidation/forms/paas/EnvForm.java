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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

/**
 * 
 * @author <a href="mailto:m.bassi@reply.it">Matteo Bassi</a>
 * @version 0.1.0
 * @since 0.1.0
 * @see <a
 *      href="http://www.istc.cnr.it/project/prisma-piattaforme-cloud-interoperabili-smart-government">Progetto
 *      PRISMA</a>
 */
public class EnvForm implements Serializable {

	private static final long serialVersionUID = 6400126423611738676L;

	/*--------------------------------------------------*/
	/*---------   CONFIGURAZIONE GENERALE  -------------*/
	/*--------------------------------------------------*/
	@Size(min = 4, max = 30)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_HYPHEN_PATTERN)
	private String serviceName;

	@Size(min = 4, max = 200)
	private String description;

	private String url;
	
	private String domainName;
	
	private String publicip;
	

	private String secureConnection;

	@Email
	private String notificationEmail;

	/*------------------------------------------------------*/
	/*---------   Configurazione del Servizio  -------------*/
	/*------------------------------------------------------*/

	private String zoneSelect;

	private String qosSelect;

	private String flavorSelect;

	@NotNull
	private String serverType;

	@NotNull
	private String serverName;

	private String scalingSelect;

	/*------------------------------------------------*/
	/*------------   SELZIONA SORGENTE  --------------*/
	/*------------------------------------------------*/
	@NotNull
	private String source;

	private String sourceLabel;
	private String appVisibility;
	private Long fileuploadId;

	private String urlStorage;
	private String sourceLabelPRISMA;
	private String appVisibilityPRISMA;
	private Long fileURLId;

	private Long inputPrivateId;
	private Long inputPublicId;
	
	public Long getFileuploadId() {
		return fileuploadId;
	}

	public void setFileuploadId(Long fileuploadId) {
		this.fileuploadId = fileuploadId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDescription() {
		return description;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublicip() {
		return publicip;
	}

	public void setPublicip(String publicip) {
		this.publicip = publicip;
	}

	public String getSecureConnection() {
		return secureConnection;
	}

	public void setSecureConnection(String secureConnection) {
		this.secureConnection = secureConnection;
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

	public String getScalingSelect() {
		return scalingSelect;
	}

	public void setScalingSelect(String scalingSelect) {
		this.scalingSelect = scalingSelect;
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

	public Long getFileURLId() {
		return fileURLId;
	}

	public void setFileURLId(Long fileURLId) {
		this.fileURLId = fileURLId;
	}

	public Long getInputPublicId() {
		return inputPublicId;
	}

	public void setInputPublicId(Long inputPublicId) {
		this.inputPublicId = inputPublicId;
	}

	public Long getInputPrivateId() {
		return inputPrivateId;
	}

	public void setInputPrivateId(Long inputPrivateId) {
		this.inputPrivateId = inputPrivateId;
	}

}