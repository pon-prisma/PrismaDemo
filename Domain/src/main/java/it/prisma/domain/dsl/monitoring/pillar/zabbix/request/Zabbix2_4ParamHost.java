package it.prisma.domain.dsl.monitoring.pillar.zabbix.request;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"output",
	//		"monitoredhosts",
	"groupids",
	"hostids",
	"filter", 
	"selectInventory", 
	"triggerids", 

	"searchWildcardsEnabled",
	"searchInventory", 

	"selectItems", 
	"selectParentTemplates"
})
public class Zabbix2_4ParamHost {

	@JsonProperty("output")
	private String output;
	//		@JsonProperty("monitored_hosts")
	//		private Object monitored_hosts;
	@JsonProperty("groupids")
	private String groupids;
	//		@JsonProperty("groupids")
	//		private ArrayList<String> groupids = new ArrayList<String>();
	@JsonProperty("filter")
	private ZabbixFilterRequest filter;
	private String selectInventory;
	@JsonProperty("hostids")
	private String hostids;
	//		@JsonProperty("hostids")
	//		private List<String> hostids = new ArrayList<String>();
	//		@JsonProperty("search")
	//		private ZabbixSearchKeyRequest search;
	@JsonProperty("triggerids")
	private String triggerids;
	@JsonProperty("searchWildcardsEnabled")
	private Boolean searchWildcardsEnabled;
	@JsonProperty("searchInventory")
	private SearchInventory searchInventory;
	@JsonProperty("selectItems")
	private String selectItems;
	@JsonProperty("selectParentTemplates")
	private String selectParentTemplates;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();


	/**
	 * 
	 * @return
	 * The searchWildcardsEnabled
	 */
	@JsonProperty("searchWildcardsEnabled")
	public Boolean getSearchWildcardsEnabled() {
		return searchWildcardsEnabled;
	}

	/**
	 * 
	 * @param searchWildcardsEnabled
	 * The searchWildcardsEnabled
	 */
	@JsonProperty("searchWildcardsEnabled")
	public void setSearchWildcardsEnabled(Boolean searchWildcardsEnabled) {
		this.searchWildcardsEnabled = searchWildcardsEnabled;
	}

	/**
	 * 
	 * @return
	 * The searchInventory
	 */
	@JsonProperty("searchInventory")
	public SearchInventory getSearchInventory() {
		return searchInventory;
	}

	/**
	 * 
	 * @param searchInventory
	 * The searchInventory
	 */
	@JsonProperty("searchInventory")
	public void setSearchInventory(SearchInventory searchInventory) {
		this.searchInventory = searchInventory;
	}

	@JsonProperty("output")
	public String getOutput() {
		return output;
	}

	@JsonProperty("output")
	public void setOutput(String output) {
		this.output = output;
	}

	//		@JsonProperty("monitored_hosts")
	//		public Object getMonitoredHosts() {
	//			return monitored_hosts;
	//		}
	//
	//		@JsonProperty("monitored_hosts")
	//		public void setMonitoredHosts(Object monitored_hosts) {
	//			this.monitored_hosts = monitored_hosts;
	//		}

	@JsonProperty("groupids")
	public String getGroupids() {
		return groupids;
	}

	@JsonProperty("groupids")
	public void setGroupids(String groupids) {
		this.groupids = groupids;
	}

	@JsonProperty("selectInventory")
	public String getSelectInventory() {
		return selectInventory;
	}

	@JsonProperty("selectInventory")
	public void setSelectInventory(String selectInventory) {
		this.selectInventory = selectInventory;
	}

	//		@JsonProperty("groupids")
	//		public ArrayList<String> getGroupids() {
	//		return groupids;
	//		}
	//
	//		@JsonProperty("groupids")
	//		public void setGroupids(ArrayList<String> groupids) {
	//		this.groupids = groupids;
	//		}

	@JsonProperty("filter")
	public ZabbixFilterRequest getFilter() {
		return filter;
	}

	@JsonProperty("filter")
	public void setFilter(ZabbixFilterRequest filter) {
		this.filter = filter;
	}

	@JsonProperty("hostids")
	public String getHostids() {
		return hostids;
	}

	@JsonProperty("hostids")
	public void setHostids(String hostids) {
		this.hostids = hostids;
	}

	//		@JsonProperty("hostids")
	//		public List<String> getHostids() {
	//		return hostids;
	//		}
	//
	//		@JsonProperty("hostids")
	//		public void setHostids(List<String> hostids) {
	//		this.hostids = hostids;
	//		}

	//		@JsonProperty("search")
	//		public ZabbixSearchKeyRequest getSearch() {
	//		return search;
	//		}
	//
	//		@JsonProperty("search")
	//		public void setSearch(ZabbixSearchKeyRequest search) {
	//		this.search = search;
	//		}

	@JsonProperty("triggerids")
	public String getTriggerids() {
		return triggerids;
	}

	@JsonProperty("triggerids")
	public void setTriggerids(String triggerids) {
		this.triggerids = triggerids;
	}

	@JsonProperty("selectItems")
	public String getSelectItems() {
		return selectItems;
	}

	@JsonProperty("selectItems")
	public void setSelectItems(String selectItems) {
		this.selectItems = selectItems;
	}

	@JsonProperty("selectParentTemplates")
	public String getSelectParentTemplates() {
		return selectParentTemplates;
	}

	@JsonProperty("selectParentTemplates")
	public void setSelectParentTemplates(String selectParentTemplates) {
		this.selectParentTemplates = selectParentTemplates;
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