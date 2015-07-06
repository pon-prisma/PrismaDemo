package it.prisma.dal.entities.paas.services;

import static javax.persistence.GenerationType.IDENTITY;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "PaaSServiceEndpoint")
public class PaaSServiceEndpoint implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1634673295535106218L;
	private Long paasServiceEndpointId;
	private PaaSService paasService;
	private String type;
	private String name;
	private URI uri;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PaaSServiceEndpointID", unique = true, nullable = false)
	public Long getPaasServiceEndpointId() {
		return paasServiceEndpointId;
	}

	public void setPaasServiceEndpointId(Long paasServiceEndpointId) {
		this.paasServiceEndpointId = paasServiceEndpointId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PaaSServiceID", nullable = false)
	@JsonBackReference
	public PaaSService getPaasService() {
		return paasService;
	}

	public void setPaasService(PaaSService paasService) {
		this.paasService = paasService;
	}

	@Column(name = "type", nullable = false, length = 32)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "name", nullable = false, length = 128)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "uri", nullable = false, length = 256)
	public String getUriToPersist() {
		return (uri == null ? null : uri.toString());
	}

	public void setUriToPersist(String uri) {
		if (uri == null)
			this.uri = null;
		else
			this.uri = URI.create(uri);
	}

	@Transient
	public URI getUri() {
		return uri;
	}

	@Transient
	public void setUri(URI uri) {
		this.uri = uri;
	}

}
