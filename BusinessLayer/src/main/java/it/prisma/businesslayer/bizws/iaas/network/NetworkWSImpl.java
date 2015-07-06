package it.prisma.businesslayer.bizws.iaas.network;

import it.prisma.businesslayer.bizlib.iaas.network.NetMgmtBean;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.domain.dsl.iaas.network.NetworkRepresentation;

import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkWSImpl extends BaseWS implements NetworkWS {

	private static Logger LOG = LogManager.getLogger(NetworkWSImpl.class);

	@Inject
	protected NetMgmtBean netMngBean;

	@Override
	public List<NetworkRepresentation> listAllNetworks(Long workgroupId) {
		try {
			return netMngBean.getNetworksByWorkgroupId(workgroupId);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
	
//	@Override
//	public List<NetworkRepresentation> listAllNetworks(
//			HttpServletRequest request, Long workgroupId) {
//		// TODO Auto-generated method stub
//
//		String identityURL = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL);
//
//		// TODO OS Tenant data lookup
//		String tenant = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_NAME);
//		String username = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_USERNAME);
//		String pwd = envConfig
//				.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_PRISMADEMO_PASSWORD);
//
//		OSFactory.enableHttpLoggingFilter(true);
//		OSClient os = OSFactory.builder().endpoint(identityURL + "/v2.0")
//				.credentials(username, pwd).tenantName(tenant).authenticate();
//
//		List<Network> nets = (List<Network>) os.networking().network().list();
//		String extNetwork = "";
//		if (envConfig.getEnvProfile().equals(
//				EnvironmentNames.SIELTE.toString().toLowerCase())) {
//			extNetwork = envConfig.getTestbed().get("SIELTE").getExtNetwork();
//		} else {
//			extNetwork = envConfig.getTestbed().get("INFN").getExtNetwork();
//		}
//		List<NetworkRepresentation> networks = new ArrayList<NetworkRepresentation>();
//
//		for (Network net : nets) {
//			if (!net.getName().equals(extNetwork)) {
//				NetworkRepresentation network = new NetworkRepresentation();
//				network.setId(net.getId());
//				network.setName(net.getName());
//				network.setShared(net.isShared());
//				network.setStatus(net.getStatus().name());
//				network.setAdminstateup(net.isAdminStateUp());
//				List<SubnetRepresentation> subnets = new ArrayList<SubnetRepresentation>();
//				for (String id : net.getSubnets()) {
//					Subnet s = os.networking().subnet().get(id);
//					if (s != null) {
//						SubnetRepresentation subnetRepresentation = new SubnetRepresentation();
//						subnetRepresentation.setCidr(s.getCidr());
//						subnetRepresentation.setEnabledhcp(s.isDHCPEnabled());
//						subnetRepresentation.setGateway(s.getGateway());
//						subnetRepresentation.setId(s.getId());
//						subnetRepresentation.setIpversion(s.getIpVersion().toString());
//						subnetRepresentation.setName(s.getName());
//						subnetRepresentation.setNetworkid(s.getNetworkId());
//						subnets.add(subnetRepresentation);
//					}
//				}
//				network.setSubnets(subnets);
//				networks.add(network);
//			}
//		}
//		return networks;
//	}

}