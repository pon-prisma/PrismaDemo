package it.prisma.businesslayer.bizlib.infrastructure;

public class AppRepoLookupInfo {
	private long workgroupId;

	public AppRepoLookupInfo() {
	}

	public AppRepoLookupInfo(long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(long workgroupId) {
		this.workgroupId = workgroupId;
	}
}