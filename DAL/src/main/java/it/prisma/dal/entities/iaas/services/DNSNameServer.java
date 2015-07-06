package it.prisma.dal.entities.iaas.services;

// Generated Nov 26, 2014 3:32:36 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * IaaSNetwork generated by hbm2java
 */
@Entity
@Table(name = "DNSNameServer")
public class DNSNameServer implements java.io.Serializable {

	private static final long serialVersionUID = -2167741662751682699L;
	
	private Long Id;
	private String name;
	
	private IaaSSubnet iaasSubnet;
	
	public DNSNameServer() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "DNSNameServersID", unique = true, nullable = false)
	public Long getId() {
		return this.Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IaasSubnetID", nullable = false)
	public IaaSSubnet getIaasSubnet() {
		return iaasSubnet;
	}


	public void setIaasSubnet(IaaSSubnet iaasSubnet) {
		this.iaasSubnet = iaasSubnet;
	}
	
}
