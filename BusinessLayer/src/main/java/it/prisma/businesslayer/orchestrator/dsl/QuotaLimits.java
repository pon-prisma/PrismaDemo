package it.prisma.businesslayer.orchestrator.dsl;

import java.io.Serializable;

public class QuotaLimits implements Serializable {

	private static final long serialVersionUID = 1L;

	private int cpus, ram, instances, volumes, volumeSize, ips;

	public int getCpus() {
		return cpus;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public int getVolumes() {
		return volumes;
	}

	public void setVolumes(int volumes) {
		this.volumes = volumes;
	}

	public int getVolumeSize() {
		return volumeSize;
	}

	public void setVolumeSize(int volumeSize) {
		this.volumeSize = volumeSize;
	}

	public int getIps() {
		return ips;
	}

	public void setIps(int ips) {
		this.ips = ips;
	}

	@Override
	public String toString() {
		return "QuotaLimits [cpus=" + cpus + ", ram=" + ram + ", instances="
				+ instances + ", volumes=" + volumes + ", volumeSize="
				+ volumeSize + ", ips=" + ips + "]";
	}

	public static QuotaLimits defaults() {
		int MIN_INSTANCE = 1;
		int MIN_CPU = 4;
		int MIN_RAM = 4; // in GB
		int MIN_IP = 2;
		int MIN_VOLUME = 1;
		int MIN_VOLUME_SIZE = 50; // in GB

		return QuotaLimitsBuilder.quotaLimits().withCpus(MIN_CPU)
				.withInstances(MIN_INSTANCE).withIps(MIN_IP).withRam(MIN_RAM)
				.withVolumes(MIN_VOLUME).withVolumeSize(MIN_VOLUME_SIZE)
				.build();
	}
}