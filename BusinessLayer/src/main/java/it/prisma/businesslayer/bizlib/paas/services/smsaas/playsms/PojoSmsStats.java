package it.prisma.businesslayer.bizlib.paas.services.smsaas.playsms;
import java.util.ArrayList;
import java.util.List;

/**
 * Pojo for sms sent
 * @author l.calicchio
 *
 */

public class PojoSmsStats {
	
		private String error;
		
		private String status;
		 
	 	private int timestamp;

	    private List<DataSmsStats> data= new ArrayList<DataSmsStats>();

	    private String error_string;
	    
	    public String getError()
	    {
	    	return error;
	    }
	    
	    public void setError(String error)
	    {
	    	this.error=error;
	    }

	    public int getTimestamp ()
	    {
	        return timestamp;
	    }

	    public void setTimestamp (int timestamp)
	    {
	        this.timestamp = timestamp;
	    }

	    public List<DataSmsStats> getData ()
	    {
	        return data;
	    }

	    public void setData (List<DataSmsStats> data)
	    {
	        this.data = data;
	    }

	    public String getError_string ()
	    {
	        return error_string;
	    }

	    public void setError_string (String error_string)
	    {
	        this.error_string = error_string;
	    }
	    
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		
		public String toString() {
			if (data==null)
				return "{ Status:"+ status+",error:"+error+",error_string:"+error_string+",timestamp:"+timestamp+" }";
			else 
			{
				String message;
				message="{ Data: [";
				for(int i=0;i<data.size();i++)
					message=message+"{smslog_id:"+data.get(i).getSmslog_id()+",src:"+data.get(i).getSrc()+",dst:"+data.get(i).getDst()+",msg:"+data.get(i).getMsg()+",dt:"+data.get(i).getDt()+",update:"+data.get(i).getUpdate()+",status:"+data.get(i).getStatus()+"},";
				message=message+"],error_string:"+error_string+",timestamp:"+timestamp+"}";
				return message;
			}
				
		}
}

	

