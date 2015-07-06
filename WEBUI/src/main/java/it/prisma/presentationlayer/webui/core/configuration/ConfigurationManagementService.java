package it.prisma.presentationlayer.webui.core.configuration;

import it.prisma.domain.dsl.configuration.PlatformConfiguration;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ConfigurationManagementService {

	
	@Value("${DEFAULT_AUTH_TOKEN}")
	String DEFAULT_AUTH_TOKEN;
	
	static final Logger LOG = LogManager
			.getLogger(ConfigurationManagementService.class.getName());

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	@Value("${prisma.businesslayer.url}")
	private String businessLayerURL;
	
	private List<PlatformConfiguration> configurations = null;

	
	
	/**
	 * Set a List of PlatformConfiguration with the given keys
	 * 
	 * @param keyS - key to search in the database
	 * 
	 */
	public void getConfigurations(String[] keys) {

		PrismaBizAPIClient prismaClient = new PrismaBizAPIClient(
				businessLayerURL);
		
		try {
			
			configurations = prismaClient.getPlatformConfiguration(keys, DEFAULT_AUTH_TOKEN).getPlatformconfiguration();
			
		} catch (JsonParseException e) {
			LOG.error("JsonParseException " + e.getMessage());
		} catch (JsonMappingException e) {
			LOG.error("JsonMappingException " + e.getMessage());
		} catch (MappingException e) {
			LOG.error("MappingException " + e.getMessage());
		} catch (NoMappingModelFoundException e) {
			LOG.error("NoMappingModelFoundException " + e.getMessage());
		} catch (ServerErrorResponseException e) {
			LOG.error("ServerErrorResponseException " + e.getMessage());
		} catch (APIErrorException e) {
			LOG.error("APIErrorException " + e.getMessage());
		} catch (RestClientException e) {
			LOG.error("RestClientException " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException " + e.getMessage());
		}
	}
	
	/**
	 * Get default list from Database, in order to populate drop down list for create-pentaho
	 * page. This value are concerned: database version, flavor and volume.
	 * 
	 * @param keysearch - key to search in the database
	 * @return list of value to populate drop down list
	 */
	public List<String> getListConfiguration(String keysearch)
	{
		
				
		List<String> resultList = new ArrayList<String>();
		
		if (configurations == null){
			String keys[] = {keysearch};
			getConfigurations(keys);
		}
		
		for (PlatformConfiguration plat : this.configurations) {
			if (plat.getKey().equals(keysearch)) {
				plat.getValue().split("-");
				for (String s : plat.getValue().split("-")) {
					resultList.add(s);
				}
			}
		}
		return resultList;
	}
}
