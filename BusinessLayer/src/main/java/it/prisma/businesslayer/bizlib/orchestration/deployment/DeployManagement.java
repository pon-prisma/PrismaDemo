package it.prisma.businesslayer.bizlib.orchestration.deployment;

import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSServiceEndpoint;

import java.util.List;

public interface DeployManagement {
	/**
	 * Should return a list of {@link PaaSServiceEndpoint} representing direct
	 * end-point to show to the user to access the current service (only
	 * high-level and user-accessible end-points should be returned; ie. PENTAHO
	 * has DB + PENTAHO SRV -> only Pentaho console end-point should be created).
	 * 
	 * @param paasService
	 *            an {@link AbstractPaaSService} which already has
	 *            {@link PaaSDeployment} informations set.
	 * @return
	 */
	public List<PaaSServiceEndpoint> getPaaSServiceEnpoints(
			AbstractPaaSService paasService);
}
