package it.prisma.businesslayer.bizws.configurations;

import it.prisma.dal.dao.config.PlatformConfigurationDAO;
import it.prisma.dal.entities.config.PlatformConfiguration;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponse;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/config")
public class PlatformConfigurationWS {

	public static Logger prismaLog = LogManager
			.getLogger(PlatformConfigurationWS.class);

	@Inject
	private PlatformConfigurationDAO platCfgDAO;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/platformconfig")
	public Response getKeys(List<String> keys) throws Exception {
		try {
			try {
				List<PlatformConfiguration> platCfg = platCfgDAO
						.findByListOfKeys(keys);
				if (platCfg.size() == keys.size()) {
					it.prisma.domain.dsl.configuration.PlatformConfigurations resp 
						= new it.prisma.domain.dsl.configuration.PlatformConfigurations();
					List<it.prisma.domain.dsl.configuration.PlatformConfiguration> confs
						= new ArrayList<it.prisma.domain.dsl.configuration.PlatformConfiguration>();
					for (PlatformConfiguration item : platCfg) {
						it.prisma.domain.dsl.configuration.PlatformConfiguration conf 
							= new it.prisma.domain.dsl.configuration.PlatformConfiguration();
						conf.setKey(item.getKey());
						conf.setValue(item.getValue());
						confs.add(conf);
					}
					resp.setPlatformconfiguration(confs);
					return PrismaResponse.status(Status.OK, resp).build()
							.build();
				} else {
					throw new NotFoundException();
				}
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (NotFoundException enfe) {

			return PrismaResponse
					.status(Status.NOT_FOUND,
							OrchestratorErrorCode.ORC_CONFIGURATION_KEY_NOT_FOUND,
							"No such configuration keys.").build().build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/allplatformconfig")
	public Response findAll() throws Exception {
		try {
			try {
				List<PlatformConfiguration> platCfg = platCfgDAO
						.findAll();
				if (platCfg.size() > 0) {
					it.prisma.domain.dsl.configuration.PlatformConfigurations resp 
						= new it.prisma.domain.dsl.configuration.PlatformConfigurations();
					List<it.prisma.domain.dsl.configuration.PlatformConfiguration> confs
						= new ArrayList<it.prisma.domain.dsl.configuration.PlatformConfiguration>();
					for (PlatformConfiguration item : platCfg) {
						it.prisma.domain.dsl.configuration.PlatformConfiguration conf 
							= new it.prisma.domain.dsl.configuration.PlatformConfiguration();
						conf.setKey(item.getKey());
						conf.setValue(item.getValue());
						confs.add(conf);
					}
					resp.setPlatformconfiguration(confs);
					return PrismaResponse.status(Status.OK, resp).build()
							.build();
				} else {
					throw new NotFoundException();
				}
			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (NotFoundException enfe) {

			return PrismaResponse
					.status(Status.NOT_FOUND,
							OrchestratorErrorCode.ORC_CONFIGURATION_KEY_NOT_FOUND,
							"No such configuration keys.").build().build();
		}
	}

}
