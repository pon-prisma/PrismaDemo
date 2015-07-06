package it.prisma.domain.dsl.monitoring.pillar.zabbix.request;


import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"output",
	"expandExpression",
	"only_true",
	"group"
})
public class TriggerParamRequestByGroup {

	@JsonProperty("output")
	private String output;
	@JsonProperty("expandExpression")
	private Boolean expandExpression;
	@JsonProperty("only_true")
	private Boolean onlyTrue;
	@JsonProperty("group")
	private String group;
	@JsonProperty("lastChangeTill")
	private String lastChangeTill;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The output
	 */
	@JsonProperty("output")
	public String getOutput() {
		return output;
	}

	/**
	 * 
	 * @param output
	 * The output
	 */
	@JsonProperty("output")
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * 
	 * @return
	 * The expandExpression
	 */
	@JsonProperty("expandExpression")
	public Boolean getExpandExpression() {
		return expandExpression;
	}

	/**
	 * 
	 * @param expandExpression
	 * The expandExpression
	 */
	@JsonProperty("expandExpression")
	public void setExpandExpression(Boolean expandExpression) {
		this.expandExpression = expandExpression;
	}

	/**
	 * 
	 * @return
	 * The onlyTrue
	 */
	@JsonProperty("only_true")
	public Boolean getOnlyTrue() {
		return onlyTrue;
	}

	/**
	 * 
	 * @param onlyTrue
	 * The only_true
	 */
	@JsonProperty("only_true")
	public void setOnlyTrue(Boolean onlyTrue) {
		this.onlyTrue = onlyTrue;
	}

	/**
	 * 
	 * @return
	 * The group
	 */
	@JsonProperty("group")
	public String getGroup() {
		return group;
	}

	/**
	 * 
	 * @param group
	 * The group
	 */
	@JsonProperty("group")
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * 
	 * @return
	 * The lastChangeTill
	 */
	@JsonProperty("lastChangeTill")
	public String getLastChangeTill() {
		return lastChangeTill;
	}

	/**
	 * 
	 * @param lastChangeTill
	 * The lastChangeTill
	 */
	@JsonProperty("lastChangeTill")
	public void setLastChangeTill(String lastChangeTill) {
		this.lastChangeTill = lastChangeTill;
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(output).append(expandExpression).append(onlyTrue).append(group).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof TriggerParamRequestByGroup) == false) {
			return false;
		}
		TriggerParamRequestByGroup rhs = ((TriggerParamRequestByGroup) other);
		return new EqualsBuilder().append(output, rhs.output).append(expandExpression, rhs.expandExpression).append(onlyTrue, rhs.onlyTrue).append(group, rhs.group).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}
