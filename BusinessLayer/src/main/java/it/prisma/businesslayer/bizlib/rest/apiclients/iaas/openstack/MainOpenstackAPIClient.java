package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.domain.dsl.iaas.openstack.identity.response.Endpoint;

import java.util.List;

public interface MainOpenstackAPIClient {

	/**
	 * Return the list of endpoints of the given type for the current Openstack instance. 
	 * @param type The type of the endpoints.
	 * @return The list.
	 */
	public List<Endpoint> getServiceEndpoints(OpenstackEndpointType type);
	
	public ComputeOpenstackAPIClient getComputeClient(Endpoint endpoint);	
	public IdentityOpenstackAPIClient getIdentityClient(Endpoint endpoint);
	public NetworkOpenstackAPIClient getNetworkClient(Endpoint endpoint);
	public ObjectStorageOpenstackAPIClient getObjectStorageClient(Endpoint endpoint);
	public OrchestrationOpenstackAPIClient getOrchestrationClient(Endpoint endpoint);

}
