package it.prisma.domain.dsl.orchestrator.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "appFileRepositoryPath", "appFileObjectStoragePath",
		"appFileTag", "appFilePublic" })
public class AppFile implements Serializable {

	private static final long serialVersionUID = -7980139398487111551L;

	@JsonProperty("appFileRepositoryPath")
	private String appFileRepositoryPath;
	@JsonProperty("appFileObjectStoragePath")
	private String appFileObjectStoragePath;
	@JsonProperty("appFileTag")
	private String appFileTag;
	@JsonProperty("appFilePublic")
	private Boolean appFilePublic;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("appFileRepositoryPath")
	public String getAppFileRepositoryPath() {
		return appFileRepositoryPath;
	}

	@JsonProperty("appFileRepositoryPath")
	public void setAppFileRepositoryPath(String appFileRepositoryPath) {
		this.appFileRepositoryPath = appFileRepositoryPath;
	}

	@JsonProperty("appFileObjectStoragePath")
	public String getAppFileObjectStoragePath() {
		return appFileObjectStoragePath;
	}

	@JsonProperty("appFileObjectStoragePath")
	public void setAppFileObjectStoragePath(String appFileObjectStoragePath) {
		this.appFileObjectStoragePath = appFileObjectStoragePath;
	}

	@JsonProperty("appFileTag")
	public String getAppFileTag() {
		return appFileTag;
	}

	@JsonProperty("appFileTag")
	public void setAppFileTag(String appFileTag) {
		this.appFileTag = appFileTag;
	}

	@JsonProperty("appFilePublic")
	public Boolean getAppFilePublic() {
		return appFilePublic;
	}

	@JsonProperty("appFilePublic")
	public void setAppFilePublic(Boolean appFilePublic) {
		this.appFilePublic = appFilePublic;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}