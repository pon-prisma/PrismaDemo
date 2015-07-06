package it.prisma.domain.dsl.paas.services.smsaas;

/**
 * Information of sms sent
 * 
 * @author l.calicchio
 * 
 */
public class DataSmsStats {
	
	private String update;

	private String dt;

	private String status;

	private String dst;

	private String src;

	private String msg;

	private String smslog_id;

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSmslog_id() {
		return smslog_id;
	}

	public void setSmslog_id(String smslog_id) {
		this.smslog_id = smslog_id;
	}
}
