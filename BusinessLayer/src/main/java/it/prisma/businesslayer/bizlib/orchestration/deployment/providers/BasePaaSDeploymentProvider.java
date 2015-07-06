package it.prisma.businesslayer.bizlib.orchestration.deployment.providers;

import it.prisma.businesslayer.bizlib.dns.DNSUtils;
import it.prisma.businesslayer.bizlib.orchestration.deployment.DeploymentUtils;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService.PaaSDeploymentServiceType;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentServiceInstance;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;
import it.prisma.domain.dsl.paas.services.generic.request.BaseCustomPaaSDetails;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.utils.json.JsonUtility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public abstract class BasePaaSDeploymentProvider<PAAS_SERVICE_TYPE extends AbstractPaaSService, DEPLOY_REQUEST_TYPE extends GenericPaaSServiceDeployRequest<?>>
		implements
		PaaSServiceDeploymentProvider<PAAS_SERVICE_TYPE, DEPLOY_REQUEST_TYPE> {

	@Inject
	private DeploymentUtils deploymentUtils;

	/**
	 * Default implementation using single host IP.
	 */
	@Override
	public List<String> getLBIPAddresses(AbstractPaaSService paasService) {
		return DNSUtils.getSingleHostIPAddresses(paasService);
	}

	protected int getURIPort(Integer port) {
		return (port != null ? port.intValue() : -1);
	}

	@Override
	public List<PaaSServiceEndpoint> getPaaSServiceEndpoints(
			PAAS_SERVICE_TYPE paasService) {

		List<PaaSServiceEndpoint> pses = new ArrayList<PaaSServiceEndpoint>();
		for (PaaSDeploymentService pds : paasService.getPaaSService()
				.getPaaSDeployment().getPaaSDeploymentServices()) {
			for (PaaSDeploymentServiceInstance pdsi : pds
					.getPaaSDeploymentServiceInstances()) {
				PaaSServiceEndpoint pse = new PaaSServiceEndpoint();
				pse.setName(pdsi.getPaaSDeploymentService().getName());
				pse.setPaasService(paasService.getPaaSService());
				pse.setType(pdsi.getPaaSDeploymentService().getType());
				try {
					pse.setUri(new URI(getScheme(pdsi
							.getPaaSDeploymentService().getType()), null, pdsi
							.getPublicIP(), getURIPort(pdsi.getPort()), null,
							null, null));
				} catch (URISyntaxException e) {
					// TODO: Improve and log properly
					e.printStackTrace();
				}
				pses.add(pse);
			}
		}

		return pses;
	}

	/* UTILITIES */
	protected DeploymentUtils getDeploymentUtils() {
		return deploymentUtils;
	}

	@SuppressWarnings("unchecked")
	protected <SPECIFIC_PRODUCT_DETAILS_TYPE> SPECIFIC_PRODUCT_DETAILS_TYPE castToSpecificProductDetails(
			BaseCustomPaaSDetails baseDetails,
			Class<SPECIFIC_PRODUCT_DETAILS_TYPE> type)
			throws JsonParseException, JsonMappingException, IOException {
		String json = JsonUtility.serializeJson(baseDetails);
		return (SPECIFIC_PRODUCT_DETAILS_TYPE) JsonUtility.deserializeJson(
				json, type);
	}

	protected PaaSDeployment createPaaSDeployment(String infrastructureName,
			String reference) {

		PaaSDeployment paasDeployment = new PaaSDeployment(infrastructureName);
		paasDeployment.setName(reference);

		String status = PaaSService.Status.START_IN_PROGRESS.toString();
		paasDeployment.setStatus(status);

		return paasDeployment;
	}

	protected String getScheme(String serviceType) {
		return serviceType.replaceAll("[^\\p{L}\\p{Nd}]+", "-");
	}

	@SuppressWarnings("unchecked")
	protected Collection<PaaSDeploymentService> selectPaaSDeploymentServiceByType(
			AbstractPaaSService paasService, PaaSDeploymentServiceType type) {
		return (Collection<PaaSDeploymentService>) CollectionUtils.select(
				paasService.getPaaSService().getPaaSDeployment()
						.getPaaSDeploymentServices(),
				new PaaSDeploymentServiceByTypePredicate(type.toString()));
	}

	@SuppressWarnings("unchecked")
	protected Collection<PaaSDeploymentService> selectPaaSDeploymentServiceByType(
			Collection<PaaSDeploymentService> collection,
			PaaSDeploymentServiceType type) {
		return (Collection<PaaSDeploymentService>) CollectionUtils.select(
				collection,
				new PaaSDeploymentServiceByTypePredicate(type.toString()));
	}

	protected static class PaaSDeploymentServiceByTypePredicate implements
			Predicate {
		private String type;

		public PaaSDeploymentServiceByTypePredicate(String type) {
			this.type = type;
		}

		public boolean evaluate(Object o) {
			PaaSDeploymentService pds = (PaaSDeploymentService) o;
			return pds.getType().equals(type);
		}
	}
}
