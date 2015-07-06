package it.prisma.dal.entities.iaas.services;

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
import javax.persistence.Table;


@Entity
@Table(name = "IaaSKey")
public class IaaSKey implements Serializable{
	
	private static final long serialVersionUID = -4796263476921695481L;
	
	private long IaaSKeyID;
	private String name;
	private String fingerprint;
	private String publicKey;
	private Workgroup workgroup;
	
	public IaaSKey(){}
	
	public IaaSKey(String name, String fingerprint, String publicKey, Workgroup workgroup) {
		super();
		this.name = name;
		this.fingerprint = fingerprint;
		this.publicKey = publicKey;
		this.workgroup = workgroup;
	}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "IaaSKeyID", unique = true, nullable = false)
	public long getId() {
		return IaaSKeyID;
	}

	public void setId(long IaaSKeyID) {
		this.IaaSKeyID = IaaSKeyID;
	}
	
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "fingerprint", nullable = false)
	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	
	@Column(name = "publicKey",  columnDefinition = "text", nullable = false)
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workgroupID", nullable = false)
	public Workgroup getWorkgroup() {
		return workgroup;
	}


	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

}
