package it.prisma.presentationlayer.webui.core.iaas;

import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.presentationlayer.webui.core.PrismaService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component("vmaas")
public class VMaaSService extends PrismaService<VMRepresentation, VMDeployRequest> {

	static final Logger LOG = LogManager
			.getLogger(VMaaSService.class.getName());


}