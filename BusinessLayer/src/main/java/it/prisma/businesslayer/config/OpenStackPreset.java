package it.prisma.businesslayer.config;

import java.util.HashMap;
import java.util.Map;

public class OpenStackPreset {

	private Map<String, String> images;
	private Map<String, String> networks;
	private String PaaSKey;
	private String extNetwork;
	
	public OpenStackPreset(){
		images = new HashMap<String, String>();
	}

	public Map<String, String> getImages() {
		return images;
	}

	public void setImages(Map<String, String> images) {
		this.images = images;
	}

	public Map<String, String> getNetworks() {
		return networks;
	}

	public void setNetworks(Map<String, String> networks) {
		this.networks = networks;
	}

	public String getPaaSKey() {
		return PaaSKey;
	}

	public void setPaaSKey(String paaSKey) {
		PaaSKey = paaSKey;
	}

	public String getExtNetwork() {
		return extNetwork;
	}

	public void setExtNetwork(String extNetwork) {
		this.extNetwork = extNetwork;
	}

	@Override
	public String toString() {
		return "OpenStackPreset [images=" + images + ", networks=" + networks
				+ ", PaaSKey=" + PaaSKey + ", extNetwork=" + extNetwork + "]";
	}

}