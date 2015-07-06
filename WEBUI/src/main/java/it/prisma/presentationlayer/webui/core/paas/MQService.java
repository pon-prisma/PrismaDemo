package it.prisma.presentationlayer.webui.core.paas;

import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;
import it.prisma.presentationlayer.webui.core.PrismaService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component("mqaas")
public class MQService extends PrismaService<MQaaSRepresentation, MQaaSDeployRequest>{
	
	static final Logger LOG = LogManager.getLogger(MQService.class.getName());
	
}
