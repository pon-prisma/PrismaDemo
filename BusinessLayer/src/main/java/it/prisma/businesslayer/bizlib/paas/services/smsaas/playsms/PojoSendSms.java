package it.prisma.businesslayer.bizlib.paas.services.smsaas.playsms;
import java.util.ArrayList;
import java.util.List;

/**
 * Pojo for SMS sent
 * @author l.calicchio
 *
 */
public class PojoSendSms {
	
	private int timestamp;

    private List<DataSendSms> data= new ArrayList<DataSendSms>();

    private String error_string;
    
    public PojoSendSms(){}

    public int getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (int timestamp)
    {
        this.timestamp = timestamp;
    }

    public List<DataSendSms> getData ()
    {
        return data;
    }

    public void setData (List<DataSendSms> data)
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
    public String toString() {
		if (data==null)
			return "{ error_string:"+error_string+",timestamp:"+timestamp+" }";
		else 
		{
			String message;
			message="{ Data: [";
			for(int i=0;i<data.size();i++)
				message=message+"{ status:"+data.get(i).getStatus()+",error:"+data.get(i).getError()+",smslog_id:"+data.get(i).getSmslog_id()+",queue:"+data.get(i).getQueue()+",to:"+data.get(i).getTo()+"},";
			message=message+"],error_string:"+error_string+",timestamp:"+timestamp+"}";
			return message;
		}
			
	}
}



