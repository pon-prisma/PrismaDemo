package it.prisma.businesslayer.bizlib.paas.services.caaas;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.dao.paas.services.caaas.EjbcaCostDAO;
import it.prisma.dal.dao.queryparams.annotations.DAOQueryParams;
import it.prisma.dal.entities.paas.services.caaas.EjbcaCost;
import it.prisma.domain.dsl.paas.services.caaas.CaException;
import it.prisma.domain.dsl.paas.services.caaas.CertificateInfoRepresentation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Hex;
import org.ejbca.core.model.AlgorithmConstants;
import org.ejbca.core.protocol.ws.client.gen.AlreadyRevokedException_Exception;
import org.ejbca.core.protocol.ws.client.gen.ApprovalException_Exception;
import org.ejbca.core.protocol.ws.client.gen.AuthorizationDeniedException_Exception;
import org.ejbca.core.protocol.ws.client.gen.CADoesntExistsException_Exception;
import org.ejbca.core.protocol.ws.client.gen.Certificate;
import org.ejbca.core.protocol.ws.client.gen.CertificateResponse;
import org.ejbca.core.protocol.ws.client.gen.EjbcaException_Exception;
import org.ejbca.core.protocol.ws.client.gen.EjbcaWS;
import org.ejbca.core.protocol.ws.client.gen.EjbcaWSService;
import org.ejbca.core.protocol.ws.client.gen.NotFoundException_Exception;
import org.ejbca.core.protocol.ws.client.gen.UserDataVOWS;
import org.ejbca.core.protocol.ws.client.gen.WaitingForApprovalException_Exception;
import org.ejbca.core.protocol.ws.common.CertificateHelper;
import org.ejbca.util.Base64;
import org.ejbca.util.CertTools;
import org.ejbca.util.cert.SubjectDirAttrExtension;
import org.ejbca.util.keystore.KeyTools;

/**
 * Class for certification operation
 * 
 * @author l.calicchio
 * 
 */
@Stateless
public class CaHelper {

	@Inject
	EjbcaCostDAO ejbcaDAO;
	
	@Inject
	protected EnvironmentConfig envConfig;
	// Url of Ca server
	String ipEjbca;
	String caName;

	final static int COSTOCERTIFICATO = 100;

	Properties properties;

	@PostConstruct
	private void init() throws IOException{
		this.ipEjbca=envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_EJBCA_IP);
		this.caName=envConfig.getSvcEndpointProperty(EnvironmentConfig.SVCEP_EJBCA_CA_NAME);
	}
	
	@Inject
	@RequestScoped
	@DAOQueryParams
	it.prisma.dal.dao.queryparams.DAOQueryParams daoQueryParams;
	
	/**
	 * @param username
	 *            : the name of a user
	 * @return list of certificates of a user
	 * @throws MalformedURLException
	 * @throws EjbcaException_Exception
	 * @throws AuthorizationDeniedException_Exception
	 * @throws CertificateException
	 * @throws CADoesntExistsException_Exception 
	 */
	public List<CertificateInfoRepresentation> CertificateList(Long workgroupId)
			throws MalformedURLException,
			AuthorizationDeniedException_Exception, EjbcaException_Exception,
			CertificateException, CADoesntExistsException_Exception {

		// Connection to EjbcaWS
		EjbcaWS ws = getConnection();
		CertificateInfoRepresentation certificateInfo;
		// List of certificate of a user
		List<Certificate> certs = ws.findCerts(String.valueOf(workgroupId), false);
		X509Certificate x509Cert;
		List<CertificateInfoRepresentation> certDesc = new ArrayList<CertificateInfoRepresentation>();
		if(daoQueryParams!=null) {
			daoQueryParams.getPaginationParams().setResultTotalCount(certs.size());
			int limit = (int) daoQueryParams.getPaginationParams().getLimit();
			int offset = (int) daoQueryParams.getPaginationParams().getOffset();
			int min = Math.min(limit + offset, certs.size());
			for(int i = offset; i < min; i++){
				x509Cert = (X509Certificate) CertTools.getCertfromByteArray(certs.get(i).getRawCertificateData());
				certificateInfo=new CertificateInfoRepresentation();
				certificateInfo.setName(x509Cert.getSubjectDN().toString().replace("CN=", ""));
				certificateInfo.setSerialNumber(CertTools.getSerialNumberAsString(x509Cert));
				RevokeCertificateInfo rev = new RevokeCertificateInfo(ws, x509Cert);
				certificateInfo.setRevoked(!rev.isRevoked());	
				certDesc.add(certificateInfo);
			}
		}
		
		return certDesc;
	}

	/**
	 * Print the certificate
	 * 
	 * @param username
	 *            : the name of a user
	 * @param index
	 *            : position of the certificate
	 * @return String with text of a certificate
	 * @throws Exception
	 */
	public String PrintCertificate(Long workgroupId, String serialNumber) throws Exception {

		// Connection to EjbcaWS
		EjbcaWS ws = getConnection();
		String startFormat="<br/><font size='3' color='blue'>";
		String endFormat="</font><font size='3'>";
		// Find certificate
		X509Certificate x509Cert = getCertificate(workgroupId, ws, serialNumber);

		// Print certificate info
		String strResult = "";
		strResult += startFormat + "User Name: " + endFormat +  workgroupId;
		strResult += startFormat + "Certificate Version: " + endFormat + x509Cert.getType() + " v."
				+ x509Cert.getVersion();
		strResult += startFormat+ "Certificate Serial Number: " + endFormat
				+ CertTools.getSerialNumberAsString(x509Cert);
		strResult += startFormat + "Issuer DN: " + endFormat + x509Cert.getIssuerDN();
		strResult += startFormat + "Valid From: " + endFormat + x509Cert.getNotBefore();
		strResult += startFormat + "Valid To: " + endFormat + x509Cert.getNotAfter();
		strResult += startFormat + "Subject DN: " + endFormat + x509Cert.getSubjectDN();
		strResult += startFormat + "Subject Alternative Name: " + endFormat;

		Collection<List<?>> listSubjectAlternativeNames = x509Cert
				.getSubjectAlternativeNames();
		strResult += ((null == listSubjectAlternativeNames) ? " None"
				: listSubjectAlternativeNames.toString());

		strResult += startFormat + "Subject Directory Attributes: " + endFormat;
		String strSubjectDirAttr = "";

		strSubjectDirAttr = SubjectDirAttrExtension
				.getSubjectDirectoryAttributes(x509Cert);

		strResult += ((null == strSubjectDirAttr) ? " None" : strSubjectDirAttr);

		PublicKey publicKey = x509Cert.getPublicKey();
		strResult += startFormat + "Public Key: " + endFormat + publicKey.getAlgorithm() + " ("
				+ KeyTools.getKeyLength(x509Cert.getPublicKey()) + " bits)<br/>"
				+ CertHelper.getPublicKeyModulus(x509Cert);
		strResult += startFormat + "Basic Constraints: " + endFormat
				+ CertHelper.getBasicConstraints(x509Cert);
		strResult += startFormat + "Key Usage: " + endFormat + CertHelper.getKeyUsage(x509Cert);

		strResult += startFormat + "Extended Key Usage: " + endFormat;
		List<String> listExtKeyUsage = null;

		listExtKeyUsage = x509Cert.getExtendedKeyUsage();

		if (null == listExtKeyUsage) {
			strResult += "None";
		} else {
			// Based on code in CertificateView.java
			String[] strExtKeyUsage = new String[listExtKeyUsage.size()];
			Map<String, String> map = CertHelper.getExtendedKeyUsageMap();
			for (int i = 0; i < listExtKeyUsage.size(); i++) {
				String item = listExtKeyUsage.get(i);
				strExtKeyUsage[i] = (String) map.get(item);
				strResult += "<br/>" + i + ": " + strExtKeyUsage[i] + " ("
						+ item + ")";
			}
		}

		strResult += startFormat + "Qualified Certificate Statement: " + endFormat;
		try {
			boolean bQCS = CertHelper.hasQcStatement(x509Cert);
			strResult +=  bQCS;
		} catch (IOException e) {
			strResult += "Unknown";
		}

		strResult += startFormat + "Signature Algorithm: " + endFormat + x509Cert.getSigAlgName();

		strResult += startFormat + "Fingerprint SHA-1: " + endFormat;
		try {
			byte[] res = CertTools.generateSHA1Fingerprint(x509Cert
					.getEncoded());
			String ret = new String(Hex.encode(res));
			strResult += "<br/>" + ret.toUpperCase();
		} catch (CertificateEncodingException cee) {
		}

		strResult += startFormat + "Fingerprint M5: " + endFormat;
		try {
			byte[] res = CertTools
					.generateMD5Fingerprint(x509Cert.getEncoded());
			String ret = new String(Hex.encode(res));
			strResult += "<br/>" + ret.toUpperCase();
		} catch (CertificateEncodingException cee) {
		}

		RevokeCertificateInfo rev = new RevokeCertificateInfo(ws, x509Cert);
		strResult = strResult
				+ startFormat + "Revoked: " + endFormat
				+ ((rev.isRevoked()) ? "No" : ("Yes<br/>Date: " + rev.getData()
						+ startFormat + "Reason: " + endFormat + CertHelper
						.getHumanReadableRevocationReason(rev.getCode())));

		return strResult.toString();
	}

	//	/**
	//	 * Check if a certificate is revoked
	//	 * @param workgroupId
	//	 * @param serialNumber
	//	 * @return
	//	 * @throws MalformedURLException
	//	 * @throws AuthorizationDeniedException_Exception
	//	 * @throws CADoesntExistsException_Exception
	//	 * @throws EjbcaException_Exception
	//	 * @throws CertificateException
	//	 */
	//	public boolean isRevokedCertificate(Long workgroupId, String serialNumber) throws MalformedURLException, AuthorizationDeniedException_Exception, CADoesntExistsException_Exception, EjbcaException_Exception, CertificateException{
	//		// Connection to EjbcaWS
	//		EjbcaWS ws = Connection();
	//		RevokeStatus revokestatus = ws.checkRevokationStatus(String.valueOf(workgroupId),serialNumber);
	//		  if(revokestatus != null){
	//		    if(revokestatus.getReason() != RevokeStatus.NOT_REVOKED){
	//		      return true;
	//		    }else{
	//			  return false;
	//		    }
	//		  }else{
	//			  throw new CertificateException();
	//		  }
	//	}


	/**
	 * Revoke a certificate
	 * 
	 * @param reason
	 *            : code of the reason of revocation
	 * @param username
	 *            : the name of a user
	 * @param index
	 *            : position of the certificate
	 * @throws EjbcaException_Exception
	 * @throws AuthorizationDeniedException_Exception
	 * @throws CertificateException
	 * @throws MalformedURLException
	 * @throws WaitingForApprovalException_Exception
	 * @throws NotFoundException_Exception
	 * @throws CADoesntExistsException_Exception
	 * @throws ApprovalException_Exception
	 * @throws AlreadyRevokedException_Exception
	 */
	public void revokeCertificate(Long workgroupId, String serialNumber, int reason)
			throws CertificateException,
			AuthorizationDeniedException_Exception, EjbcaException_Exception,
			MalformedURLException, AlreadyRevokedException_Exception,
			ApprovalException_Exception, CADoesntExistsException_Exception,
			NotFoundException_Exception, WaitingForApprovalException_Exception {

		// Connection to EjbcaWS
		EjbcaWS ws = getConnection();
		// Find certificate
		X509Certificate x509Cert = getCertificate(workgroupId, ws, serialNumber);

		// Revoke the certificate
		ws.revokeCert(x509Cert.getIssuerDN().toString(),
				CertTools.getSerialNumberAsString(x509Cert), reason);

	}

	/**
	 * Save the certificate(PEM: .cer, .crt, .pem) in a specific path
	 * 
	 * @param path
	 *            : path where save the certificate
	 * @param username
	 *            : the name of a user
	 * @param index
	 *            : position of the certificate
	 * @throws CertificateEncodingException
	 * @throws IOException
	 * @throws EjbcaException_Exception
	 * @throws AuthorizationDeniedException_Exception
	 * @throws CertificateException
	 */
	public X509Certificate saveCertificate(Long workgroupId, String serialNumber)
			throws IOException, CertificateException,
			AuthorizationDeniedException_Exception, EjbcaException_Exception {

		// Connection to EjbcaWS
		EjbcaWS ws = getConnection();
		X509Certificate x509cert= getCertificate(workgroupId, ws, serialNumber );
		if (x509cert==null)
			throw new CertificateException("Error retrieving the certificate");
		return x509cert;
	}

	/**
	 * Generate a certificate from a PKCS10 request
	 * 
	 * @param username
	 * @param subjectDN
	 * @return private key
	 * @throws Exception 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public String addCertificate(String workgroupId, String subjectDN) throws Exception{
		EjbcaCost ejbcaCost=null;
		try{

			ejbcaCost = ejbcaDAO.create(new EjbcaCost(workgroupId,"TempSerial", "TempDate",
					COSTOCERTIFICATO));

			// Parameters of a new user
			UserDataVOWS user1 = new UserDataVOWS();
			user1.setUsername(workgroupId);
			user1.setSubjectDN("CN=" + subjectDN);
			// CA into EJBCA
			user1.setCaName(caName);
			user1.setEmail(null);
			user1.setSubjectAltName(null);
			// End entity profile in EJBCA
			user1.setEndEntityProfileName("UtentePrisma");
			// Certificate profile in EJBCA
			user1.setCertificateProfileName("UTENTEPRISMA");

			// Connection to EjbcaWS
			EjbcaWS ws = getConnection();

			// Object containing public and private key
			KeyPair keys;
			// Generate keys
			keys = KeyTools.genKeys("1024", AlgorithmConstants.KEYALGORITHM_RSA);
			// Certificate request
			PKCS10CertificationRequest pkcs10 = new PKCS10CertificationRequest(
					"SHA256WithRSA", CertTools.stringToBcX509Name("CN=NOUSED"),
					keys.getPublic(), null, keys.getPrivate());

			String start = "-----BEGIN RSA PRIVATE KEY-----\n";
			String end = "\n-----END RSA PRIVATE KEY-----";
			String privateKeyBytes = new String(Base64.encode(keys.getPrivate()
					.getEncoded()));
			String privateKey = start + privateKeyBytes + end;

			// Certificate creation
			CertificateResponse certenv =	ws.certificateRequest(user1,
					new String(Base64.encode(pkcs10.getEncoded())),
					CertificateHelper.CERT_REQ_TYPE_PKCS10, null,
					CertificateHelper.RESPONSETYPE_CERTIFICATE);

			//Certificate info recover from this variable
			X509Certificate cert = certenv.getCertificate ();

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 

			ejbcaCost.setDate( sdf.format(new Date()));
			ejbcaCost.setCertificateSN(CertTools.getSerialNumberAsString(cert));;
			ejbcaDAO.update(ejbcaCost);

			/**	OLD METHOD THAT HAVE ONE ROW FOR EVERY WORKGROUP AND NOT HAVE DETAIL OF CERTIFICATE

			 * // add certificate to cost database
			//Certificate info recover from this variable
			X509Certificate cert = certenv.getCertificate();

			//Add row
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
			String dataStr = sdf.format(new Date());


			/**	OLD METHOD THAT HAVE ONE ROW FOR EVERY WORKGROUP AND NOT HAVE DETAIL OF CERTIFICATE

			 * // add certificate to cost database
			String query1 = "select username from ejbcaCost where username ='"
					+ workgroupId + "'";// used to select the user
			String query2 = "UPDATE ejbcaCost SET cost=cost+? WHERE username=?";// if the user exists
			// update the
			// certificate cost
			String query3 = "INSERT INTO ejbcaCost(username, cost)" // if the user
					// doesn't exist
					// creates it
					// and him cost
					+ "VALUES (?,?)";

			database.incrementCost(query1, query2, query3, workgroupId,
					COSTOCERTIFICATO);
			 **/
			return privateKey;
		}catch(Exception e){
			if (ejbcaCost!=null){
				ejbcaDAO.delete(ejbcaCost.getId()); 
				throw new CaException("Impossible to create a certificate because there is problem with ejbca.");
			}
			throw new CaException("Impossible to create a certificate because there is problem with database.");
		}
	}

	/**
	 * Establishes connection with WebServer
	 * 
	 * @return connection object
	 * @throws MalformedURLException
	 */
	public EjbcaWS getConnection() throws MalformedURLException {
		// URL of ejbca server
		String urlstr = "https://" + ipEjbca + ":8443/ejbca/ejbcaws/ejbcaws?wsdl";
		QName qname = new QName("http://ws.protocol.core.ejbca.org/",
				"EjbcaWSService");
		EjbcaWSService service = new EjbcaWSService(new URL(urlstr), qname);
		return service.getEjbcaWSPort();
	}

	/**
	 * 
	 * @param username
	 *            : the name of a user
	 * @param ws
	 *            : connection object
	 * @param index
	 *            : position of the certificate
	 * @return certificate of user
	 * @throws AuthorizationDeniedException_Exception
	 * @throws EjbcaException_Exception
	 * @throws CertificateException
	 */
	private static X509Certificate getCertificate(Long workgroupId, EjbcaWS ws,
			String serialNumber) throws AuthorizationDeniedException_Exception,
			EjbcaException_Exception, CertificateException {
		List<Certificate> certs = ws.findCerts(String.valueOf(workgroupId), false);
		for(Certificate cert:certs){
			X509Certificate x509Cert = (X509Certificate) CertTools.getCertfromByteArray(cert
					.getRawCertificateData());
			String serial=CertTools.getSerialNumberAsString(x509Cert);
			//da valutare se togliere gli zeri iniziali perchè restituisce un serialnumber senza gli zeri avanti				
			if (serial.equals(serialNumber))
				return x509Cert;
		}
		return null;
	}
}
