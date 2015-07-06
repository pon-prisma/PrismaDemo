package it.prisma.businesslayer.orchestrator.dsl;

import java.util.List;

public class DNSEndpoint extends AbstractEndpoint {

	public static final String NAME = "DNS";

	private List<String> baseDomains;

	public List<String> getBaseDomains() {
		return baseDomains;
	}

	public void setBaseDomains(List<String> baseDomains) {
		this.baseDomains = baseDomains;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
