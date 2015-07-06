package it.prisma.businesslayer.bizws.testing.monitoring;

import it.prisma.businesslayer.bizlib.monitoring.MonitoringServiceBean;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.bizws.testing.monitoring.dsl.TestMonitoringAddServiceRequest;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInServer;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.google.common.base.Preconditions;

@Stateless
public class MonitoringTestWSImpl implements MonitoringTestWS {

	@Inject
	MonitoringServiceBean monitoringService;

	// public TestResults testAddHost() {
	//
	// TestResults trs = new TestResults();
	//
	// String hostName = UUID.randomUUID().toString();
	// String hostGroupID = UUID.randomUUID().toString();
	// Random r = new Random();
	// String hostIP = r.nextInt(256) + "." + r.nextInt(256) + "."
	// + r.nextInt(256) + "." + r.nextInt(256);
	//
	// List<String> hostGroupTypesList = new ArrayList<String>();
	// for (PaaSServiceType type : PaaSServiceType.values()) {
	// try {
	// hostGroupTypesList.add(monConfig
	// .getHostGroupByPaaSServiceType(type));
	// } catch (Exception e) {
	// }
	// }
	// String[] hostGroupTypes = (String[]) hostGroupTypesList.toArray(new
	// String[hostGroupTypesList.size()]);
	//
	// List<String> atomicServicesList = new ArrayList<String>();
	// for (PaaSDeploymentServiceRepresentationType type :
	// PaaSDeploymentServiceRepresentationType.values()) {
	// atomicServicesList.add(monConfig.getAtomicServiceByType(type));
	// }
	// String[] atomicServices = (String[]) atomicServicesList.toArray(new
	// String[atomicServicesList.size()]);;
	//
	// for (String pillar : pillars) {
	// String testDescrA = String.format("Adding host to: Pillar [%s]",
	// pillar);
	//
	// for (String hostGroupType : hostGroupTypes) {
	// String testDescrB = String.format(testDescrA
	// + ", HostGroups [%s]", hostGroupType);
	//
	// String testDescrC = String.format(testDescrB
	// + ", AtomicServices [%s]",
	// StringUtils.join(atomicServices, ","));
	//
	// TestResult tr = new TestResult();
	// trs.getResults().add(tr);
	// tr.setTestDescription(testDescrC);
	// try {
	// String id = monitoringService.addMonitoredVM(pillar, hostName,
	// hostName, hostIP, hostGroupType, hostGroupID,
	// Arrays.asList(atomicServices));
	//
	// if (id != null && !id.equals("")) {
	// tr.setStatus(Status.PASSED);
	// } else {
	// tr.setStatus(Status.FAILED);
	// tr.setMessage("HostId result is null or empty");
	// tr.setAdditionalInformations(id);
	// break;
	// }
	// } catch (Exception e) {
	// tr.setError(e);
	// tr.setMessage("Error during host registration: "
	// + e.getMessage());
	// tr.setStatus(Status.FAILED);
	// break;
	// }
	// }
	// }
	// trs.updateResults();
	//
	// return trs;
	// }

	@Override
	public CreatedHostInServer testAddHost(
			TestMonitoringAddServiceRequest request) {
		try {

			Preconditions.checkNotNull(request.getAdapterType());
			Preconditions.checkNotNull(request.getVmuuid());
			Preconditions.checkNotNull(request.getVmip());
			Preconditions.checkNotNull(request.getGroup());
			Preconditions.checkNotNull(request.getServiceCategory());
			Preconditions.checkNotNull(request.getServiceId());
			Preconditions.checkNotNull(request.getServices());

			return monitoringService.addServiceToMonitoring(
					request.getAdapterType(), request.getVmuuid(),
					request.getVmip(), request.getGroup(),
					request.getServiceCategory(), request.getServiceId(),
					request.getServices());

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public boolean isServiceRunning(String adapterType,
			String serviceGroup, String serviceCategory, String serviceId) {
		try{
			return monitoringService.isServiceRunning(adapterType, serviceGroup, serviceId);

		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
}
