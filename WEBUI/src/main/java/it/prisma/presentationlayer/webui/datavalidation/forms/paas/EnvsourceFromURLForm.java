package it.prisma.presentationlayer.webui.datavalidation.forms.paas;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class EnvsourceFromURLForm  implements Serializable {
	
	private static final long serialVersionUID = -7498337923836451674L;
	
	@NotNull
	private String urlStorage;	

	@NotNull
	private String sourceLabelPRISMA;
	
	private String appVisibilityPRISMA;

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

	@Override
	public String toString() {
		return "EnvsourceFromURLForm [urlStorage=" + urlStorage
				+ ", sourceLabelPRISMA=" + sourceLabelPRISMA
				+ ", appVisibilityPRISMA=" + appVisibilityPRISMA + "]";
	}
	
}
