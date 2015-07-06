package it.prisma.dal.entities.paas.services;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class AbstractPaaSService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected PaaSService paaSService;

	@OneToOne(fetch = FetchType.EAGER)
	// , cascade = CascadeType.PERSIST)
	@PrimaryKeyJoinColumn
	// @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	// @JoinColumn(name = "PaaSServiceID")
	public PaaSService getPaaSService() {
		return this.paaSService;
	}

	public void setPaaSService(PaaSService paaSservice) {
		this.paaSService = paaSservice;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
