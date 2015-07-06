package it.prisma.presentationlayer.webui.uploader;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest implements Serializable {
 
	private static final long serialVersionUID = 263380637726626751L;
 

	private String name;
	private String visibility;

	private MultipartFile file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	} 

}