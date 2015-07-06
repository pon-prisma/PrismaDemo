package it.prisma.presentationlayer.webui.vos.platformconfig;

import java.util.HashMap;
import java.util.Map;

public class PrismaPlatformConfiguration {
	
	private final Map<String, String> configData;
	
	public PrismaPlatformConfiguration(Map<String, String> configData) {
		this.configData = new HashMap<String, String>(configData);
	}

	/**
	 * 
	 * @return the map that contains all configurations
	 */
	public Map<String, String> getConfigData() {
		return configData;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value associated to a key
	 */
	public String getValue(String key) {
		return getConfigData().get(key);
	}	
}
