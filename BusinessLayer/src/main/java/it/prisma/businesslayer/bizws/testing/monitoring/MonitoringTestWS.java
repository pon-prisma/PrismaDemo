package it.prisma.businesslayer.bizws.testing.monitoring;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.testing.monitoring.MonitoringTestWS.TestResult.Status;
import it.prisma.businesslayer.bizws.testing.monitoring.dsl.TestMonitoringAddServiceRequest;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInServer;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Path("/test/monitoring")
@PrismaWrapper
public interface MonitoringTestWS {

	public static class TestResults {
		private List<TestResult> results = new ArrayList<MonitoringTestWS.TestResult>();
		private int failedTests;
		private int totalTest;
		private int manualCheckTests;

		public int getManualCheckTests() {
			return manualCheckTests;
		}

		public void setManualCheckTests(int manualCheckTests) {
			this.manualCheckTests = manualCheckTests;
		}

		public List<TestResult> getResults() {
			return results;
		}

		public void setResults(List<TestResult> results) {
			this.results = results;
		}

		public int getFailedTests() {
			return failedTests;
		}

		public void setFailedTests(int failedTests) {
			this.failedTests = failedTests;
		}

		public int getTotalTest() {
			return totalTest;
		}

		public void setTotalTest(int totalTest) {
			this.totalTest = totalTest;
		}

		/**
		 * Updates current results list and related failedTests &
		 * manualCheckTests counters.
		 * 
		 * @param results
		 */
		public void updateResults(List<TestResult> results) {
			setResults(results);
			totalTest = 0;
			failedTests = 0;
			manualCheckTests = 0;
			for (TestResult result : results) {
				totalTest++;
				if (result.getStatus() == Status.FAILED)
					failedTests++;
				else if (result.getStatus() == Status.MANUAL_CHECK)
					manualCheckTests++;
			}
		}
		
		public void updateResults() {
			updateResults(this.getResults());
		}
	}

	@JsonPropertyOrder({ "testDescription", "status", "message", "error",
			"additionalInformations" })
	public static class TestResult {
		public enum Status {
			PASSED, FAILED, MANUAL_CHECK
		}

		private Status status = Status.FAILED;
		private String message;
		private Throwable error;
		private Object additionalInformations;
		private String testDescription;

		public String getTestDescription() {
			return testDescription;
		}

		public void setTestDescription(String testDescription) {
			this.testDescription = testDescription;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Throwable getError() {
			return error;
		}

		public void setError(Throwable error) {
			this.error = error;
		}

		public Object getAdditionalInformations() {
			return additionalInformations;
		}

		public void setAdditionalInformations(Object additionalInformations) {
			this.additionalInformations = additionalInformations;
		}

	}

	/**
	 * Add the host to monitoring
	 * 
	 * @return
	 */
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreatedHostInServer testAddHost(TestMonitoringAddServiceRequest request);
	
	
	/**
	 * Test if the service is running
	 * 
	 * @return
	 */
	@GET
	@Path("/{adapterType}/{serviceGroup}/{serviceCategory}/{serviceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isServiceRunning(@PathParam("adapterType") String adapterType, @PathParam("serviceGroup") String serviceGroup, @PathParam("serviceCategory") String serviceCategory, @PathParam("serviceId") String serviceId);
}
