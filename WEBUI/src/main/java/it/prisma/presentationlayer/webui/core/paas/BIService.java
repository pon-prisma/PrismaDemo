package it.prisma.presentationlayer.webui.core.paas;

import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.presentationlayer.webui.core.PrismaService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component("biaas")
public class BIService extends PrismaService<BIaaSRepresentation, BIaaSDeployRequest>{
	
	static final Logger LOG = LogManager.getLogger(BIService.class.getName());

}
