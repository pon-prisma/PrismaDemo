package it.prisma.dal.entities.paas.deployments;

// Generated 17-set-2014 21.25.53 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.paas.services.PaaSService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * PaaSdeploymentServiceInstance generated by hbm2java
 */
@Entity
@Table(name = "PaaSDeploymentServiceInstance")
public class PaaSDeploymentServiceInstance implements java.io.Serializable {

	public enum StatusType {
		UNKNOWN, STARTING, RUNNING, STOPPED, DELETED, ERROR;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5730032691899149973L;
	private Long paaSdeploymentServiceInstanceId;
	private PaaSDeploymentService paaSdeploymentService;
	private String publicIP;
	private String privateIP;

	private String status;
	private String instanceId;
	private String hostId;
	private Integer port;
	/**
	 * An optional link to the service with placeholder to be replaced by other
	 * fields like {@link PaaSDeploymentServiceInstance#getPort()} and
	 * {@link PaaSService#getDomainName()} (i.e. PhpMyAdmin :
	 * http://{domain_name}:{port}/phpmyadmin).
	 */
	private String link;

	public PaaSDeploymentServiceInstance() {
	}

	public PaaSDeploymentServiceInstance(
			PaaSDeploymentService paaSDeploymentService) {
		this.paaSdeploymentService = paaSDeploymentService;
	}

	public PaaSDeploymentServiceInstance(
			PaaSDeploymentService paaSDeploymentService, String publicIP,
			String privateIP, String status, String instanceId, String hostId) {
		this.paaSdeploymentService = paaSDeploymentService;
		this.publicIP = publicIP;
		this.privateIP = privateIP;

		this.status = status;
		this.instanceId = instanceId;
		this.hostId = hostId;
	}

	public PaaSDeploymentServiceInstance(
			PaaSDeploymentService paaSDeploymentService, String publicIP,
			String privateIP, String status, String instanceId, String hostId,
			Integer port) {
		this.paaSdeploymentService = paaSDeploymentService;
		this.publicIP = publicIP;
		this.privateIP = privateIP;

		this.status = status;
		this.instanceId = instanceId;
		this.hostId = hostId;
		this.port = port;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PaaSDeploymentServiceInstanceID", unique = true, nullable = false)
	public Long getId() {
		return this.paaSdeploymentServiceInstanceId;
	}

	public void setId(Long paaSdeploymentServiceInstanceId) {
		this.paaSdeploymentServiceInstanceId = paaSdeploymentServiceInstanceId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PaaSDeploymentServiceID", nullable = false)
	@JsonBackReference
	public PaaSDeploymentService getPaaSDeploymentService() {
		return this.paaSdeploymentService;
	}

	public void setPaaSDeploymentService(
			PaaSDeploymentService paaSdeploymentService) {
		this.paaSdeploymentService = paaSdeploymentService;
	}

	@Column(name = "publicIP", length = 32)
	public String getPublicIP() {
		return this.publicIP;
	}

	public void setPublicIP(String publicIP) {
		this.publicIP = publicIP;
	}

	@Column(name = "privateIP", length = 32)
	public String getPrivateIP() {
		return this.privateIP;
	}

	public void setPrivateIP(String privateIP) {
		this.privateIP = privateIP;
	}

	@Column(name = "status", length = 128)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "instanceId", length = 45)
	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "hostId", length = 512)
	public String getHostId() {
		return this.hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	@Column(name = "link", nullable = true, length = 256)
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Column(name = "port", nullable = true)
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
