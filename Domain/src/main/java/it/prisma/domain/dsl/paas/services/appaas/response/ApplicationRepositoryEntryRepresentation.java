package it.prisma.domain.dsl.paas.services.appaas.response;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationRepositoryEntryRepresentation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("status")
	private Long Id;
	@JsonProperty("ownerUserId")
	private Long ownerUserId;
	@JsonProperty("ownerWorkgroupId")
	private Long ownerWorkgroupId;
	@JsonProperty("applicationName")
	private String applicationName;
	@JsonProperty("tag")
	private String tag;
	@JsonProperty("description")
	private String description;
	@JsonProperty("public")
	private boolean public_;
	@JsonProperty("fileName")
	private String fileName;
	@JsonProperty("filePath")
	private String filePath;
	@JsonProperty("fileSize")
	private Long fileSize;
	@JsonProperty("createdAt")
	private Date createdAt;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(Long ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public Long getOwnerWorkgroupId() {
		return ownerWorkgroupId;
	}

	public void setOwnerWorkgroupId(Long ownerWorkgroupId) {
		this.ownerWorkgroupId = ownerWorkgroupId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublic() {
		return public_;
	}

	public void setPublic(boolean public_) {
		this.public_ = public_;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

}
