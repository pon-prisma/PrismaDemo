package it.prisma.businesslayer.bizlib.orchestration.deployment.providers;

import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import it.prisma.domain.dsl.paas.services.biaas.pentaho.request.PentahoServerOnlyDeployDetails;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDetails;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.util.List;

/**
 * Deployment logic for a PaaSService to be deployed.
 * 
 * @author l.biava
 *
 * @param <PAAS_SERVICE_TYPE>
 * @param <DEPLOY_REQUEST_TYPE>
 */
public interface PaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE extends AbstractPaaSService, DEPLOY_REQUEST_TYPE extends GenericPaaSServiceDeployRequest<?>> {

	public PaaSServiceType getPaaSServiceType();

	/**
	 * Should generate an {@link AbstractPaaSService} using the given deploy
	 * request.<br/>
	 * This should also validate data and throw exceptions accordingly to stop
	 * the deployment process.
	 * 
	 * @param deployRequest
	 * @return
	 */
	public PAAS_SERVICE_TYPE generateAndValidatePaaSService(
			DEPLOY_REQUEST_TYPE deployRequest, UserAccount user,
			Workgroup workgroup);

	/**
	 * Should return a list of IP Addresses to be registered to the
	 * {@link AbstractPaaSService} Domain Name.
	 * 
	 * @param paasService
	 *            the {@link AbstractPaaSService} containing an already
	 *            populated {@link PaaSDeployment} field, inside the
	 *            {@link PaaSService} field.
	 * @return
	 */
	public List<String> getLBIPAddresses(AbstractPaaSService paasService);

	/**
	 * Should return a deployRequest where the specific service details (i.e.
	 * {@link BIaaSDetails#getProductSpecificDetails()}) are cast to an actual
	 * implementation (i.e. {@link PentahoServerOnlyDeployDetails}) depending on
	 * service product type & configuration.
	 * 
	 * @param deployRequest
	 * @return
	 */
	public DEPLOY_REQUEST_TYPE castToProductSpecificRequest(
			DEPLOY_REQUEST_TYPE deployRequest) throws Exception;

	public List<PaaSServiceEndpoint> getPaaSServiceEndpoints(
			PAAS_SERVICE_TYPE paasService);
}
