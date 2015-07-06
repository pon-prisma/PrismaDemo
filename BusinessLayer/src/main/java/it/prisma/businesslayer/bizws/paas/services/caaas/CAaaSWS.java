package it.prisma.businesslayer.bizws.paas.services.caaas;

import it.prisma.businesslayer.bizlib.paas.services.caaas.CaHelper;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.domain.dsl.paas.services.caaas.CaErrorCode;
import it.prisma.domain.dsl.paas.services.caaas.CertificateInfoRepresentation;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejbca.core.protocol.ws.client.gen.AlreadyRevokedException_Exception;
import org.ejbca.core.protocol.ws.client.gen.ApprovalException_Exception;
import org.ejbca.core.protocol.ws.client.gen.AuthorizationDeniedException_Exception;
import org.ejbca.core.protocol.ws.client.gen.CADoesntExistsException_Exception;
import org.ejbca.core.protocol.ws.client.gen.EjbcaException_Exception;
import org.ejbca.core.protocol.ws.client.gen.NotFoundException_Exception;
import org.ejbca.core.protocol.ws.client.gen.WaitingForApprovalException_Exception;

@Path("/CAaas")
public class CAaaSWS extends BaseWS implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger prismaLog = LogManager.getLogger(CAaaSWS.class);

	@EJB
	private CaHelper helper;
	
	
	public CAaaSWS() throws IOException{
		//Helper = new CaHelper();
	}

	/**
	 * get the user's certificates list
	 * @param user
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/allCertificate/{workgroupId}")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<CertificateInfoRepresentation> allCertificate(@PathParam("workgroupId") Long workgroupId) {

		try {
			// Search all certificates of a user
			
			return helper.CertificateList(workgroupId);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	
	/**
	 * Show certificate details 
	 * @param user
	 * @param index
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewCertificate/{workgroupId}/{serialNumber}")
	public Response viewCertificate(@PathParam("workgroupId") Long workgroupId, 
			@PathParam("serialNumber") String serialNumber) {
		try {
			// Print informations of a certificate
			String viewCert = helper.PrintCertificate(workgroupId, serialNumber);
			return handleResult(viewCert);
		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.GENERIC_CERTIFICATE_EXCEPTION, ejbe);
		}
	}

	/**
	 * revoke user's certificate
	 * @param user
	 * @param index
	 * @param reason
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/revokeCertificate/{workgroupId}/{serialNumber}/{reason}")
	public Response revokeCertificate(@PathParam("workgroupId") Long workgroupId, 
			@PathParam("serialNumber") String serialNumber, @PathParam("reason") int reason) {
		try {
			// Revoke a certificate
			helper.revokeCertificate(workgroupId, serialNumber, reason);
			return handleResult("Certificato revocato");
		} catch (MalformedURLException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.MALFORMED_URL_EXCEPTION, ejbe);
		} catch (EjbcaException_Exception | CertificateException ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.SEARCH_CERTIFICATE_EXCEPTION, ejbe);
		} catch (AuthorizationDeniedException_Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.USER_NOT_AUTHORIZED_EXCEPTION, ejbe);
		} catch (CADoesntExistsException_Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.CA_NOT_EXISTS_EXCEPTION, ejbe);
		} catch (NotFoundException_Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.USER_NOT_FOUD_EXCEPTION, ejbe);
		} catch (AlreadyRevokedException_Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.ALREADY_REVOKED_EXCEPTION, ejbe);
		} catch (ApprovalException_Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.APPROVAL_EXCEPTION, ejbe);
		} catch (WaitingForApprovalException_Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.WAITING_APPROVAL_EXCEPTION, ejbe);
		} catch (Exception ejbe) {
			ejbe.printStackTrace();
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.WAITING_APPROVAL_EXCEPTION, ejbe);
		}
	}

	/**
	 * save a certificate
	 * @param index
	 * @param user
	 * @param path
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveCertificate/{workgroupId}/{serialNumber}")
	public Response saveCertificate( @PathParam("workgroupId") Long workgroupId, 
			@PathParam("serialNumber") String serialNumber) {

		try {
			X509Certificate x509cert=helper.saveCertificate(workgroupId, serialNumber);

			//x509 to string
			Base64 encoder = new Base64(64);

			String cert_begin = "-----BEGIN CERTIFICATE-----\n";
			String end_cert = "-----END CERTIFICATE-----";

			byte[] derCert = x509cert.getEncoded();
			String pemCertPre = new String(encoder.encode(derCert));
			String pemCert = cert_begin + pemCertPre + end_cert;
			return handleResult(pemCert);

		} catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.CERTIFICATE_EXCEPTION, ejbe);

		}
	}

	/**
	 * Create a new certificate and download the private key
	 * @return
	 * @throws SQLException 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createCertificate/{workgroupId}/{subjectDN}")
	public Response createCertificate(@PathParam("workgroupId") Long workgroupId, 
			@PathParam("subjectDN") String subjectDN)  {
		//	String key;
		try {
			return handleResult(helper.addCertificate( String.valueOf(workgroupId),  subjectDN));
		}catch (Exception ejbe) {
			return handleError(Status.INTERNAL_SERVER_ERROR,
					CaErrorCode.CERTIFICATE_CREATION_EXCEPTION, ejbe);
		}
	}
}