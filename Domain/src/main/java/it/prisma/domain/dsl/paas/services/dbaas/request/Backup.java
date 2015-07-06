package it.prisma.domain.dsl.paas.services.dbaas.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Backup implements Serializable {

	private static final long serialVersionUID = -5204282148700315914L;
	
	@JsonProperty("interval")
	private Integer interval;

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

}
