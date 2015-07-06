package it.prisma.presentationlayer.webui.datavalidation.forms.iaas;

import it.prisma.presentationlayer.webui.datavalidation.forms.DeployForm;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * <b>VMForm</b> is the class that contains custom VM's properties
 * 
 * @author <a href="mailto:m.bassi@reply.it">Matteo Bassi</a>
 * @version 0.0.7
 * @since 0.0.7
 * @see DeployForm
 * 
 */
public class VMForm extends DeployForm {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String imageName;
	
	@NotNull
	private int volume;
	
	@NotNull
	private String network;
	
	@NotNull
	private String key;
	
	
	private List<String> securityGroup;
	
//	private String script;
//	
//	@NotNull
//	private String diskPartition;



	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(List<String> securityGroup) {
		this.securityGroup = securityGroup;
	}

//	public String getScript() {
//		return script;
//	}
//
//	public void setScript(String script) {
//		this.script = script;
//	}
//
//	public String getDiskPartition() {
//		return diskPartition;
//	}
//
//	public void setDiskPartition(String diskPartition) {
//		this.diskPartition = diskPartition;
//	}
	
}
