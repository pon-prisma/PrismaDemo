package it.prisma.monitoringpillar.db;

// Generated Aug 29, 2014 7:02:25 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * PrismamonitoredHosts generated by hbm2java
 */
@Entity
@Table(name = "PRISMAMonitoredHosts", catalog = "monitoring")
public class PrismamonitoredHosts implements java.io.Serializable {

	private String prismamonitoredHostNames;
	private String testbedType;
	private Set prismamonitoringServerses = new HashSet(0);

	public PrismamonitoredHosts() {
	}

	public PrismamonitoredHosts(String prismamonitoredHostNames,
			String testbedType) {
		this.prismamonitoredHostNames = prismamonitoredHostNames;
		this.testbedType = testbedType;
	}

	public PrismamonitoredHosts(String prismamonitoredHostNames,
			String testbedType, Set prismamonitoringServerses) {
		this.prismamonitoredHostNames = prismamonitoredHostNames;
		this.testbedType = testbedType;
		this.prismamonitoringServerses = prismamonitoringServerses;
	}

	@Id
	@Column(name = "PRISMAMonitoredHostNames", unique = true, nullable = false, length = 45)
	public String getPrismamonitoredHostNames() {
		return this.prismamonitoredHostNames;
	}

	public void setPrismamonitoredHostNames(String prismamonitoredHostNames) {
		this.prismamonitoredHostNames = prismamonitoredHostNames;
	}

	@Column(name = "TestbedType", nullable = false, length = 45)
	public String getTestbedType() {
		return this.testbedType;
	}

	public void setTestbedType(String testbedType) {
		this.testbedType = testbedType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prismamonitoredHosts")
	public Set getPrismamonitoringServerses() {
		return this.prismamonitoringServerses;
	}

	public void setPrismamonitoringServerses(Set prismamonitoringServerses) {
		this.prismamonitoringServerses = prismamonitoringServerses;
	}

}
