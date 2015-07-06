package it.prisma.businesslayer.bizlib.paas.services.caaas;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.ejbca.core.protocol.ws.client.gen.RevokeStatus;


public class CertHelper {
	
	
	static String getPublicKeyModulus(X509Certificate certificate)
	{
		String mod = null;
		if (certificate.getPublicKey() instanceof RSAPublicKey)
		{
			mod = ""
					+ ((RSAPublicKey) certificate.getPublicKey()).getModulus()
							.toString(16);
			mod = mod.toUpperCase();
			mod = StringUtils.abbreviate(mod, 50);
		}
		else if (certificate.getPublicKey() instanceof DSAPublicKey)
		{
			mod = ""
					+ ((DSAPublicKey) certificate.getPublicKey()).getY()
							.toString(16);
			mod = mod.toUpperCase();
			mod = StringUtils.abbreviate(mod, 50);
		}
		else if (certificate.getPublicKey() instanceof ECPublicKey)
		{
			mod = ""
					+ ((ECPublicKey) certificate.getPublicKey()).getW()
							.getAffineX().toString(16);
			mod = mod
					+ ((ECPublicKey) certificate.getPublicKey()).getW()
							.getAffineY().toString(16);
			mod = mod.toUpperCase();
			mod = StringUtils.abbreviate(mod, 50);
		}
		return mod;
	}
	
	static Map<String, String> getExtendedKeyUsageMap()
	
	{
		Map<String, String> mapExtendedKeyUsage = null;
		if (null == mapExtendedKeyUsage)
		{
			mapExtendedKeyUsage = new HashMap<String, String>();

			String[] oid =
			{
					"2.5.29.37.0",
					"1.3.6.1.5.5.7.3.1",
					"1.3.6.1.5.5.7.3.2",
					"1.3.6.1.5.5.7.3.3",
					"1.3.6.1.5.5.7.3.4",
					// # IPSECENDSYSTEM is deprecated
					// extendedkeyusage.oid.5 = 1.3.6.1.5.5.7.3.5
					// # IPSECTUNNEL is deprecated
					// extendedkeyusage.oid.6 = 1.3.6.1.5.5.7.3.6
					// # IPSECUSER is deprecated
					// extendedkeyusage.oid.7 = 1.3.6.1.5.5.7.3.7
					"1.3.6.1.5.5.7.3.8",
					// # MS smart card logon
					"1.3.6.1.4.1.311.20.2.2",
					"1.3.6.1.5.5.7.3.9",
					// # Microsoft Encrypted File System Certificates
					"1.3.6.1.4.1.311.10.3.4",
					// # Microsoft Encrypted File System Recovery Certificates
					"1.3.6.1.4.1.311.10.3.4.1",
					"1.3.6.1.5.5.7.3.17",
					"1.3.6.1.5.5.7.3.15",
					"1.3.6.1.5.5.7.3.16",
					// # Microsoft Signer of documents
					"1.3.6.1.4.1.311.10.3.12",
					// # Intel AMT (out of band) network management
					"2.16.840.1.113741.1.2.3",
					// # ETSI TS 102 231 TSL Signer (id-tsl-kp-tslSigning)
					"0.4.0.2231.3.0"
			};

			String[] oidDescription =
			{
					"Any Extended Key Usage",
					"Server Authentication",
					"Client Authentication",
					"Code Signing",
					"Email Protection",
					// # IPSECENDSYSTEM is deprecated
					// extendedkeyusage.name.5 = null
					// # IPSECTUNNEL is deprecated
					// extendedkeyusage.name.6 = null
					// # IPSECUSER is deprecated
					// extendedkeyusage.name.7 = null
					"Time Stamping",
					"MS Smart Card Logon",
					"OCSP Signer",
					"MS Encrypted File System (EFS)",
					"MS EFS Recovery",
					"Internet Key Exchange for IPsec",
					"SCVP Server",
					"SCVP Client",
					"MS Document Signing",
					"Intel AMT Management",
					"TSL Signing"
			};

			assert (oid.length == oidDescription.length);

			for (int i = 0; i < oid.length; i++)
			{
				mapExtendedKeyUsage.put(oid[i], oidDescription[i]);
			}
		}

		return mapExtendedKeyUsage;
	}
	
	static boolean hasQcStatement(X509Certificate cert) throws IOException
	{
		boolean ret = false;
		String oid = X509Extensions.QCStatements.getId();
		byte[] bytes = cert.getExtensionValue(oid);
		if (bytes == null)
		{
			return false;
		}
		ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(
				bytes));
		ASN1OctetString octs = (ASN1OctetString) aIn.readObject();
		aIn = new ASN1InputStream(new ByteArrayInputStream(octs.getOctets()));
		DERObject obj = aIn.readObject();

		if (obj != null)
		{
			ret = true;
		}

		return ret;
	}
	
	
	static String getBasicConstraints(X509Certificate x509cert)
	{
		String retval = "None";
		int bc = x509cert.getBasicConstraints();
		if (bc == Integer.MAX_VALUE)
		{
			retval = "No limit to the allowed length of the certification path.";
		}
		else if (bc == -1)
		{
			retval = "End Entity";
		}
		else
		{
			retval = "Maximum number of CA certificates that may follow this certificate in a certification path: "
					+ x509cert.getBasicConstraints();
		}
		return retval;
	}
	
	
	
	static String getKeyUsage(X509Certificate cert)
	{
		String retval = "";

		String[] strKeyUsage =
		{
				"Digital Signature",
				"Non-repudiation",
				"Key Encipherment",
				"Data Encipherment",
				"Key Agreement",
				"Key Certificate Sign",
				"CRL Sign",
				"Encipher Only",
				"Decipher Only"
		};

		boolean[] usages = cert.getKeyUsage();
		for (int i = 0; i < strKeyUsage.length; i++)
		{
			if (usages[i])
			{
				if (0 != retval.length())
				{
					retval += ", ";
				}
				retval += strKeyUsage[i];
			}
		}

		if (0 == retval.length())
		{
			retval = "None";
		}
		return retval;
	}
	
	
	
	static String getHumanReadableRevocationReason(int reason)
	{
		switch (reason)
		{
			case RevokeStatus.NOT_REVOKED:
				return "The certificate is not revoked";
			case RevokeStatus.REVOKATION_REASON_UNSPECIFIED:
				return "Unspecified";
			case RevokeStatus.REVOKATION_REASON_KEYCOMPROMISE:
				return "Key compromise";
			case RevokeStatus.REVOKATION_REASON_CACOMPROMISE:
				return "CA compromise";
			case RevokeStatus.REVOKATION_REASON_AFFILIATIONCHANGED:
				return "Affiliation changed";
			case RevokeStatus.REVOKATION_REASON_SUPERSEDED:
				return "Superseded";
			case RevokeStatus.REVOKATION_REASON_CESSATIONOFOPERATION:
				return "Cessation of operation";
			case RevokeStatus.REVOKATION_REASON_CERTIFICATEHOLD:
				return "Certificate hold";
			case RevokeStatus.REVOKATION_REASON_REMOVEFROMCRL:
				return "Remove from CRL";
			case RevokeStatus.REVOKATION_REASON_PRIVILEGESWITHDRAWN:
				return "Privileges withdrawn";
			case RevokeStatus.REVOKATION_REASON_AACOMPROMISE:
				return "AA compromise";
			default:
				return "Unknown";
		}
	}


}
