package it.prisma.dal.entitywrappers.paas.services.appaas;

import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;
import it.prisma.dal.entities.paas.services.PaaSService;

import java.io.Serializable;
import java.util.Date;

public class AppaaS extends AbstractPaaSService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5043162548213080952L;
	PaaSService paaSservice;

	public AppaaS() {
	}

	public AppaaS(PaaSService paaSService) {
		if (paaSService == null)
			throw new IllegalArgumentException("PaaSService cannot be null.");

		if (!paaSService.getType().equals(
				PaaSService.PaaSServiceType.APPaaS.toString()))
			throw new IllegalArgumentException("PaaSService must be an APPaaS.");
		
		this.paaSservice = paaSService;
	}

	public Long getId() {
		return paaSservice.getId();
	}

	public void setId(Long id) {
		this.paaSservice.setId(id);
	}

	public PaaSService getPaaSService() {
		return this.paaSservice;
	}

	public void setPaaSService(PaaSService paaSservice) {
		this.paaSservice = paaSservice;
	}

	public UserAccount getUserAccount() {
		return this.paaSservice.getUserAccount();
	}

	public void setUserAccount(UserAccount userAccount) {
		this.paaSservice.setUserAccount(userAccount);
	}

	public Workgroup getWorkgroup() {
		return this.paaSservice.getWorkgroup();
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.paaSservice.setWorkgroup(workgroup);
	}

	public String getName() {
		return this.paaSservice.getName();
	}

	public void setName(String name) {
		this.paaSservice.setName(name);
	}

	public String getDescription() {
		return this.paaSservice.getDescription();
	}

	public void setDescription(String description) {
		this.paaSservice.setDescription(description);
	}

	public Date getCreatedAt() {
		return this.paaSservice.getCreatedAt();
	}

	public void setCreatedAt(Date createdAt) {
		this.paaSservice.setCreatedAt(createdAt);
	}
	
	public String getStatus() {
		return this.paaSservice.getStatus();
	}

	public void setStatus(String status) {
		this.paaSservice.setStatus(status);
	}

}
