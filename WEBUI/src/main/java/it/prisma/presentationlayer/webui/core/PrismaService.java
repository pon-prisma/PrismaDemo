package it.prisma.presentationlayer.webui.core;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The <code>PrismaService</code> component
 * 
 * <br/><br/>Example:
 * <pre>
 *   &#64;Service
 *   &#64;Component("vmaas")
 *   {@code public class VMaaSService extends PrismaService<VMRepresentation, VMDeployRequest>}
 * </pre>
 * 
 * @author Reply
 *
 * @param <T> the serviceRepresentation class
 * @param <E> the deployRequest class
 */
public abstract class PrismaService<T extends GenericPaaSServiceRepresentation, E extends GenericPaaSServiceDeployRequest<T>> implements IPrismaService<T, E> {
	
	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;
	
	@Override
	public T getServiceRepresentation(Long workgroupId, Long appId, Long serviceId, Class<T> representationClass) throws Exception {
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		return prismaBizAPIClient.getServiceRepresentation(workgroupId, appId, serviceId, representationClass, authToken.getTokenId());	
	}

	@Override
	public BootstrapTable<T> getAllServiceTableRepresentation(Long workgroupId, RestWSParamsContainer params, Class<T> representationClass) throws Exception  {
		
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		BootstrapTable<T> bootstrapTable = new BootstrapTable<T>();
		
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
	
		bootstrapTable.setEnvironments(	prismaBizAPIClient.getAllServiceRepresentation(workgroupId, null, params, meta, representationClass, authToken.getTokenId()));
		bootstrapTable.setTotal(meta.getMeta().getPagination().getTotalCount());
		return bootstrapTable;
	}
	
	@Override
	public T deployService(Class<T> representationClass, E deployRequest) throws Exception {
		AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();
		return prismaBizAPIClient.deployPaaSService(deployRequest, representationClass, authToken.getTokenId());
	}

}
