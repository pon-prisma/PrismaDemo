package it.prisma.businesslayer.orchestrator.dsl;

public class MonitoringEndpoint extends AbstractEndpoint {

    private static final long serialVersionUID = -8054656968653643119L;

    public static final String NAME = "Monitoring";

    private String adapterType;

    public String getAdapterType() {
	return adapterType;
    }

    public void setAdapterType(String adapterType) {
	this.adapterType = adapterType;
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public String toString() {
	return "MonitoringEndpoint [adapterType=" + adapterType + "]";
    }

}
