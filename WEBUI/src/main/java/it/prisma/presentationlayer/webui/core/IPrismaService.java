package it.prisma.presentationlayer.webui.core;

import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

/**
 * The <code>IPrismaService</code> component
 * 
 * @author Reply
 *
 * @param <T> the serviceRepresentation class
 * @param <E> the deployRequest class
 */
public interface IPrismaService<T extends GenericPaaSServiceRepresentation, E extends GenericPaaSServiceDeployRequest<T>> {

	/**
	 * Get a representation of the service 
	 * 
	 * @param workgroupId
	 * @param serviceId
	 * @param representationClass
	 * 
	 * @return the representation of the service
	 * 
	 * @throws Exception
	 */
	public T getServiceRepresentation(Long workgroupId, Long appId, Long serviceId, Class<T> representationClass) throws Exception;
	
	/**
	 * Get all service
	 * 
	 * @param workgroupId
	 * @param params
	 * @param representationClass
	 * 
	 * @return {@link BootstrapTable<T>}
	 * 
	 * @throws Exception
	 */
	public BootstrapTable<T> getAllServiceTableRepresentation(Long workgroupId, RestWSParamsContainer params, Class<T> representationClass) throws Exception;
		
	/**
	 * Create the service
	 * 
	 * @param representationClass
	 * @param deployRequest
	 * 
	 * @return the representation of the deployed service
	 * 
	 * @throws Exception
	 */
	public T deployService(Class<T> representationClass, E deployRequest) throws Exception;

}
