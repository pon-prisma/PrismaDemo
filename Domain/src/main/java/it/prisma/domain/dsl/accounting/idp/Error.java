package it.prisma.domain.dsl.accounting.idp;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "errorCode", "errorName", "errorMsg", "verbose" })
public class Error {

	@JsonProperty("errorCode")
	private Long errorCode;
	@JsonProperty("errorName")
	private String errorName;
	@JsonProperty("errorMsg")
	private String errorMsg;
	@JsonProperty("verbose")
	private String verbose;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The errorCode
	 */
	@JsonProperty("errorCode")
	public Long getErrorCode() {
		return errorCode;
	}

	/**
	 * 
	 * @param errorCode
	 *            The errorCode
	 */
	@JsonProperty("errorCode")
	public void setErrorCode(Long errorCode) {
		this.errorCode = errorCode;
	}

	public Error withErrorCode(Long errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	/**
	 * 
	 * @return The errorName
	 */
	@JsonProperty("errorName")
	public String getErrorName() {
		return errorName;
	}

	/**
	 * 
	 * @param errorName
	 *            The errorName
	 */
	@JsonProperty("errorName")
	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public Error withErrorName(String errorName) {
		this.errorName = errorName;
		return this;
	}

	/**
	 * 
	 * @return The errorMsg
	 */
	@JsonProperty("errorMsg")
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * 
	 * @param errorMsg
	 *            The errorMsg
	 */
	@JsonProperty("errorMsg")
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Error withErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}

	/**
	 * 
	 * @return The verbose
	 */
	@JsonProperty("verbose")
	public String getVerbose() {
		return verbose;
	}

	/**
	 * 
	 * @param verbose
	 *            The verbose
	 */
	@JsonProperty("verbose")
	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}

	public Error withVerbose(String verbose) {
		this.verbose = verbose;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Error withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}