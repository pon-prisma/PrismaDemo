package it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"service",
	"vcpus_used",
	"hypervisor_type",
	"local_gb_used",
	"host_ip",
	"hypervisor_hostname",
	"id",
	"memory_mb",
	"current_workload",
	"vcpus",
	"free_ram_mb",
	"running_vms",
	"free_disk_gb",
	"hypervisor_version",
	"disk_available_least",
	"local_gb",
	"cpu_info",
	"memory_mb_used"
})
public class Hypervisor {

	@JsonProperty("service")
	private Service service;
	@JsonProperty("vcpus_used")
	private Integer vcpusUsed;
	@JsonProperty("hypervisor_type")
	private String hypervisorType;
	@JsonProperty("local_gb_used")
	private Integer localGbUsed;
	@JsonProperty("host_ip")
	private String hostIp;
	@JsonProperty("hypervisor_hostname")
	private String hypervisorHostname;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("memory_mb")
	private Integer memoryMb;
	@JsonProperty("current_workload")
	private Integer currentWorkload;
	@JsonProperty("vcpus")
	private Integer vcpus;
	@JsonProperty("free_ram_mb")
	private Integer freeRamMb;
	@JsonProperty("running_vms")
	private Integer runningVms;
	@JsonProperty("free_disk_gb")
	private Integer freeDiskGb;
	@JsonProperty("hypervisor_version")
	private Integer hypervisorVersion;
	@JsonProperty("disk_available_least")
	private Integer diskAvailableLeast;
	@JsonProperty("local_gb")
	private Integer localGb;
	//TODO: MAppare con CPuInfo
	@JsonProperty("cpu_info")
	private String cpuInfo;
	@JsonProperty("memory_mb_used")
	private Integer memoryMbUsed;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The service
	 */
	@JsonProperty("service")
	public Service getService() {
		return service;
	}

	/**
	 * 
	 * @param service
	 * The service
	 */
	@JsonProperty("service")
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * 
	 * @return
	 * The vcpusUsed
	 */
	@JsonProperty("vcpus_used")
	public Integer getVcpusUsed() {
		return vcpusUsed;
	}

	/**
	 * 
	 * @param vcpusUsed
	 * The vcpus_used
	 */
	@JsonProperty("vcpus_used")
	public void setVcpusUsed(Integer vcpusUsed) {
		this.vcpusUsed = vcpusUsed;
	}

	/**
	 * 
	 * @return
	 * The hypervisorType
	 */
	@JsonProperty("hypervisor_type")
	public String getHypervisorType() {
		return hypervisorType;
	}

	/**
	 * 
	 * @param hypervisorType
	 * The hypervisor_type
	 */
	@JsonProperty("hypervisor_type")
	public void setHypervisorType(String hypervisorType) {
		this.hypervisorType = hypervisorType;
	}

	/**
	 * 
	 * @return
	 * The localGbUsed
	 */
	@JsonProperty("local_gb_used")
	public Integer getLocalGbUsed() {
		return localGbUsed;
	}

	/**
	 * 
	 * @param localGbUsed
	 * The local_gb_used
	 */
	@JsonProperty("local_gb_used")
	public void setLocalGbUsed(Integer localGbUsed) {
		this.localGbUsed = localGbUsed;
	}

	/**
	 * 
	 * @return
	 * The hostIp
	 */
	@JsonProperty("host_ip")
	public String getHostIp() {
		return hostIp;
	}

	/**
	 * 
	 * @param hostIp
	 * The host_ip
	 */
	@JsonProperty("host_ip")
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	/**
	 * 
	 * @return
	 * The hypervisorHostname
	 */
	@JsonProperty("hypervisor_hostname")
	public String getHypervisorHostname() {
		return hypervisorHostname;
	}

	/**
	 * 
	 * @param hypervisorHostname
	 * The hypervisor_hostname
	 */
	@JsonProperty("hypervisor_hostname")
	public void setHypervisorHostname(String hypervisorHostname) {
		this.hypervisorHostname = hypervisorHostname;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The memoryMb
	 */
	@JsonProperty("memory_mb")
	public Integer getMemoryMb() {
		return memoryMb;
	}

	/**
	 * 
	 * @param memoryMb
	 * The memory_mb
	 */
	@JsonProperty("memory_mb")
	public void setMemoryMb(Integer memoryMb) {
		this.memoryMb = memoryMb;
	}

	/**
	 * 
	 * @return
	 * The currentWorkload
	 */
	@JsonProperty("current_workload")
	public Integer getCurrentWorkload() {
		return currentWorkload;
	}

	/**
	 * 
	 * @param currentWorkload
	 * The current_workload
	 */
	@JsonProperty("current_workload")
	public void setCurrentWorkload(Integer currentWorkload) {
		this.currentWorkload = currentWorkload;
	}

	/**
	 * 
	 * @return
	 * The vcpus
	 */
	@JsonProperty("vcpus")
	public Integer getVcpus() {
		return vcpus;
	}

	/**
	 * 
	 * @param vcpus
	 * The vcpus
	 */
	@JsonProperty("vcpus")
	public void setVcpus(Integer vcpus) {
		this.vcpus = vcpus;
	}

	/**
	 * 
	 * @return
	 * The freeRamMb
	 */
	@JsonProperty("free_ram_mb")
	public Integer getFreeRamMb() {
		return freeRamMb;
	}

	/**
	 * 
	 * @param freeRamMb
	 * The free_ram_mb
	 */
	@JsonProperty("free_ram_mb")
	public void setFreeRamMb(Integer freeRamMb) {
		this.freeRamMb = freeRamMb;
	}

	/**
	 * 
	 * @return
	 * The runningVms
	 */
	@JsonProperty("running_vms")
	public Integer getRunningVms() {
		return runningVms;
	}

	/**
	 * 
	 * @param runningVms
	 * The running_vms
	 */
	@JsonProperty("running_vms")
	public void setRunningVms(Integer runningVms) {
		this.runningVms = runningVms;
	}

	/**
	 * 
	 * @return
	 * The freeDiskGb
	 */
	@JsonProperty("free_disk_gb")
	public Integer getFreeDiskGb() {
		return freeDiskGb;
	}

	/**
	 * 
	 * @param freeDiskGb
	 * The free_disk_gb
	 */
	@JsonProperty("free_disk_gb")
	public void setFreeDiskGb(Integer freeDiskGb) {
		this.freeDiskGb = freeDiskGb;
	}

	/**
	 * 
	 * @return
	 * The hypervisorVersion
	 */
	@JsonProperty("hypervisor_version")
	public Integer getHypervisorVersion() {
		return hypervisorVersion;
	}

	/**
	 * 
	 * @param hypervisorVersion
	 * The hypervisor_version
	 */
	@JsonProperty("hypervisor_version")
	public void setHypervisorVersion(Integer hypervisorVersion) {
		this.hypervisorVersion = hypervisorVersion;
	}

	/**
	 * 
	 * @return
	 * The diskAvailableLeast
	 */
	@JsonProperty("disk_available_least")
	public Integer getDiskAvailableLeast() {
		return diskAvailableLeast;
	}

	/**
	 * 
	 * @param diskAvailableLeast
	 * The disk_available_least
	 */
	@JsonProperty("disk_available_least")
	public void setDiskAvailableLeast(Integer diskAvailableLeast) {
		this.diskAvailableLeast = diskAvailableLeast;
	}

	/**
	 * 
	 * @return
	 * The localGb
	 */
	@JsonProperty("local_gb")
	public Integer getLocalGb() {
		return localGb;
	}

	/**
	 * 
	 * @param localGb
	 * The local_gb
	 */
	@JsonProperty("local_gb")
	public void setLocalGb(Integer localGb) {
		this.localGb = localGb;
	}

	/**
	 * 
	 * @return
	 * The cpuInfo
	 */
	@JsonProperty("cpu_info")
	public String getCpuInfo() {
		return cpuInfo;
	}

	/**
	 * 
	 * @param cpuInfo
	 * The cpu_info
	 */
	@JsonProperty("cpu_info")
	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	/**
	 * 
	 * @return
	 * The memoryMbUsed
	 */
	@JsonProperty("memory_mb_used")
	public Integer getMemoryMbUsed() {
		return memoryMbUsed;
	}

	/**
	 * 
	 * @param memoryMbUsed
	 * The memory_mb_used
	 */
	@JsonProperty("memory_mb_used")
	public void setMemoryMbUsed(Integer memoryMbUsed) {
		this.memoryMbUsed = memoryMbUsed;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(service).append(vcpusUsed).append(hypervisorType).append(localGbUsed).append(hostIp).append(hypervisorHostname).append(id).append(memoryMb).append(currentWorkload).append(vcpus).append(freeRamMb).append(runningVms).append(freeDiskGb).append(hypervisorVersion).append(diskAvailableLeast).append(localGb).append(cpuInfo).append(memoryMbUsed).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Hypervisor) == false) {
			return false;
		}
		Hypervisor rhs = ((Hypervisor) other);
		return new EqualsBuilder().append(service, rhs.service).append(vcpusUsed, rhs.vcpusUsed).append(hypervisorType, rhs.hypervisorType).append(localGbUsed, rhs.localGbUsed).append(hostIp, rhs.hostIp).append(hypervisorHostname, rhs.hypervisorHostname).append(id, rhs.id).append(memoryMb, rhs.memoryMb).append(currentWorkload, rhs.currentWorkload).append(vcpus, rhs.vcpus).append(freeRamMb, rhs.freeRamMb).append(runningVms, rhs.runningVms).append(freeDiskGb, rhs.freeDiskGb).append(hypervisorVersion, rhs.hypervisorVersion).append(diskAvailableLeast, rhs.diskAvailableLeast).append(localGb, rhs.localGb).append(cpuInfo, rhs.cpuInfo).append(memoryMbUsed, rhs.memoryMbUsed).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}