package it.prisma.dal.entities.iaas.tenant;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CloudifyInstance")
public class CloudifyInstance implements Serializable {

	private static final long serialVersionUID = -7769594828950824991L;
	
	private Long id;
	private String url;
	private String version;
	private String username;
	private String password;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "cloudifyInstance")
	private IaaSTenant iaaSTenant;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cloudifyInstanceID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "url", nullable=false)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "version", nullable=false)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public IaaSTenant getIaaSTenant() {
		return iaaSTenant;
	}

	public void setIaaSTenant(IaaSTenant iaaSTenant) {
		this.iaaSTenant = iaaSTenant;
	}

}
