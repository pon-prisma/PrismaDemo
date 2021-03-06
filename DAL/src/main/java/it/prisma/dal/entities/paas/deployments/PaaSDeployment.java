package it.prisma.dal.entities.paas.deployments;

// Generated 17-set-2014 21.25.53 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.paas.services.PaaSService;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * PaaSdeployment generated by hbm2java
 */
@Entity
@Table(name = "PaaSDeployment")
public class PaaSDeployment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4246532288150877297L;
	private Long paaSdeploymentId;
	private String paaStype;
	private String name;
	private String status;
	private Set<PaaSDeploymentService> paaSdeploymentServices = new HashSet<PaaSDeploymentService>(0);
	private Set<PaaSService> paaSservices = new HashSet<PaaSService>(0);

	public PaaSDeployment() {
	}

	public PaaSDeployment(String paaStype) {
		this.paaStype = paaStype;
	}

	public PaaSDeployment(String paaStype, String name, String status, Set<PaaSDeploymentService> paaSdeploymentServices, Set<PaaSService> paaSservices) {
		this.paaStype = paaStype;
		this.name = name;
		this.status = status;
		this.paaSdeploymentServices = paaSdeploymentServices;
		this.paaSservices = paaSservices;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PaaSDeploymentID", unique = true, nullable = false)
	public Long getId() {
		return this.paaSdeploymentId;
	}

	public void setId(Long paaSdeploymentId) {
		this.paaSdeploymentId = paaSdeploymentId;
	}

	@Column(name = "PaaSType", nullable = false, length = 256)
	public String getPaaSType() {
		return this.paaStype;
	}

	public void setPaaSType(String paaStype) {
		this.paaStype = paaStype;
	}

	@Column(name = "name", length = 65535)
	@Type(type="text")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paaSDeployment", cascade=CascadeType.ALL)
	@JsonManagedReference
	public Set<PaaSDeploymentService> getPaaSDeploymentServices() {
		return this.paaSdeploymentServices;
	}

	public void setPaaSDeploymentServices(Set<PaaSDeploymentService> paaSdeploymentServices) {
		this.paaSdeploymentServices = paaSdeploymentServices;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paaSDeployment")
	@JsonBackReference
	public Set<PaaSService> getPaaSServices() {
		return this.paaSservices;
	}

	public void setPaaSServices(Set<PaaSService> paaSservices) {
		this.paaSservices = paaSservices;
	}

}
