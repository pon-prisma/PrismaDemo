package it.prisma.domain.dsl.iaas.openstack.network.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Deprecated
@JsonPropertyOrder({ "id", "name", "name" })
public class NetworkRepresentation {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	
	
}
