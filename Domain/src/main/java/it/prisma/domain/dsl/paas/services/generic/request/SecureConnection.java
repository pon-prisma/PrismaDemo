package it.prisma.domain.dsl.paas.services.generic.request;

import java.io.Serializable;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "enabled", "certificatePath" })
public class SecureConnection implements Serializable {

	private static final long serialVersionUID = -6632255995456899804L;

	@JsonProperty("certificatePath")
	private String certificatePath;

	@JsonProperty("certificatePath")
	public String getCertificatePath() {
		return certificatePath;
	}

	@JsonProperty("certificatePath")
	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
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