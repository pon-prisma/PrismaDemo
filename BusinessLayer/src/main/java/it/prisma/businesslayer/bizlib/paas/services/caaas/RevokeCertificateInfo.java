package it.prisma.businesslayer.bizlib.paas.services.caaas;
import java.security.cert.X509Certificate;

import javax.xml.datatype.XMLGregorianCalendar;

import org.ejbca.core.protocol.ws.client.gen.AuthorizationDeniedException_Exception;
import org.ejbca.core.protocol.ws.client.gen.CADoesntExistsException_Exception;
import org.ejbca.core.protocol.ws.client.gen.EjbcaException_Exception;
import org.ejbca.core.protocol.ws.client.gen.EjbcaWS;
import org.ejbca.core.protocol.ws.client.gen.RevokeStatus;

/**
 * 
 */

/**
 * This class is used for know the revoke status of a certificate
 * @author l.calicchio
 *
 */
public class RevokeCertificateInfo {
	private int code;
	private XMLGregorianCalendar data;
	/**
	 * 
	 * @param ws: web service connection
	 * @param x509Cert: certificate to be verified
	 * @return code of certification revoke (http://ejbca.org/docs/ws/constant-values.html#org.ejbca.core.protocol.ws.client.gen.RevokeStatus.NOT_REVOKED)
	 * 			-1 if not revoked
	 * @throws AuthorizationDeniedException_Exception
	 * @throws CADoesntExistsException_Exception
	 * @throws EjbcaException_Exception
	 */
	public RevokeCertificateInfo(EjbcaWS ws,X509Certificate x509Cert) throws AuthorizationDeniedException_Exception, CADoesntExistsException_Exception, EjbcaException_Exception{
		RevokeStatus revokeStatus = ws.checkRevokationStatus(x509Cert.getIssuerDN()
				.getName(), x509Cert.getSerialNumber().toString(16));
		code=revokeStatus.getReason();
		data=revokeStatus.getRevocationDate();
		
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * 
	 * @return true if the certificate is revoked
	 */
	public boolean isRevoked(){
		return code==-1;
	}

	/**
	 * @return the data
	 */
	public XMLGregorianCalendar getData() {
		return data;
	}
	
}
