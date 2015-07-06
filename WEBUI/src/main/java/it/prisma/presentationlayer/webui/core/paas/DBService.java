package it.prisma.presentationlayer.webui.core.paas;

import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.presentationlayer.webui.core.PrismaService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component("dbaas")
public class DBService extends PrismaService<DBaaSRepresentation, DBaaSDeployRequest>{
	
	static final Logger LOG = LogManager.getLogger(DBService.class.getName());
	
}
