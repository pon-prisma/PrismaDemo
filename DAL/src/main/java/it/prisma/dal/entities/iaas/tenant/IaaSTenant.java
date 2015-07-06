package it.prisma.dal.entities.iaas.tenant;

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.accounting.Workgroup;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "IaaSTenant")
public class IaaSTenant implements Serializable {

	private static final long serialVersionUID = 5324685081782605116L;

	private Long iaasTenantId;
	private String name;
	private String username;
	private String password;
	private IaaSEnvironment iaasEnvironment;
	private Workgroup workgroup;
	private CloudifyInstance cloudifyInstance;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "iaasTenantId", unique = true, nullable = false)
	public Long getId() {
		return iaasTenantId;
	}

	public void setId(Long iaasTenantId) {
		this.iaasTenantId = iaasTenantId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Important: Leave EAGER !
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "iaasEnvironmentId")
	public IaaSEnvironment getIaasEnvironment() {
		return this.iaasEnvironment;
	}

	public void setIaasEnvironment(IaaSEnvironment iaasEnvironment) {
		this.iaasEnvironment = iaasEnvironment;
	}

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "iaasTenant")
	public Workgroup getWorkgroup() {
		return this.workgroup;
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "cloudifyInstanceID", nullable = true)
	public CloudifyInstance getCloudifyInstance() {
		return cloudifyInstance;
	}

	public void setCloudifyInstance(CloudifyInstance cloudifyEndpoint) {
		this.cloudifyInstance = cloudifyEndpoint;
	}

}
