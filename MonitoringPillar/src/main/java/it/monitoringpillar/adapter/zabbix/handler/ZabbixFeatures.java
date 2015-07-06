package it.monitoringpillar.adapter.zabbix.handler;

public class ZabbixFeatures {

	public enum ZabbixMethods {
		HOSTCREATE("host.create"), HOSTGROUPCREATE("hostgroup.create"), HOSTDELETE("host.delete"), GROUPDELETE("hostgroup.delete"), HOST("host.get"), HOSTGROUP(
				"hostgroup.get"), TEMPLATE("template.get"), INTERFACE("hostinterface.get"), METRIC("item.get"), TRIGGER(
						"trigger.get"), HISTORY("history.get"), EVENT("event.get"), MASSUPDATE("host.massupdate"), ITEMUPDATE(
								"item.update");

		private ZabbixMethods(String zabbixMethod) {
			this.zabbixMethod = zabbixMethod;
		}

		private String zabbixMethod;

		public String getzabbixMethod() {
			return this.zabbixMethod;
		}

		public String toString() {
			return this.zabbixMethod;
		}
	}

	/**
	 * 
	 * 
	 * 
	 */
	public enum ServerType {

		SERVERIAAS("serveriaas"), SERVERMETRICS("servermetrics"), SERVERWATCHER("serverwatcher");

		private String serverType;

		private ServerType(String serverType) {
			this.serverType = serverType;
		}

		public String getServerType() {
			return this.serverType;
		}

		public String toString() {
			return this.serverType;
		}
	}
}
