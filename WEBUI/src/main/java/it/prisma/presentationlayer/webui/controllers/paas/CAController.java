package it.prisma.presentationlayer.webui.controllers.paas;

import it.prisma.domain.dsl.paas.services.caaas.CertificateInfoRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.core.paas.CAManagementService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.openssl.PEMReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CAController {

	static final Logger LOG = LogManager
			.getLogger(CAController.class.getName());

	@Autowired
	private CAManagementService caManagementService;

	@RequestMapping(value = "/paas/caaas", method = RequestMethod.GET)
	public String getAllCa(Model model, @CurrentUser PrismaUserDetails user) {
		return "pages/paas/caaas/create-ca";
	}

	@RequestMapping(value = "/async/paas/caaas", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<CertificateInfoRepresentation> getAllCAaaS(
			@PrismaRestWSParams RestWSParamsContainer params,
			@CurrentUser PrismaUserDetails user, HttpServletResponse response) {
		try {
			return caManagementService.allCertificate(user
					.getActiveWorkgroupMembership().getWorkgroupId(), params);
		} catch (Exception e) {
			response.setStatus(500);
			LOG.error("Impossible to retrieve the certificate:", e);
			return null;
		}
	}

	@RequestMapping(value = "/async/paas/caaas/{commonName}", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse createCertificate(
			@PathVariable String commonName, HttpServletRequest request,
			@CurrentUser PrismaUserDetails user) {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		try {
			String key = caManagementService.createCertificate(user
					.getActiveWorkgroupMembership().getWorkgroupId(),
					commonName);
			request.getSession().setAttribute("privateKey", key);
			request.getSession().setMaxInactiveInterval(30000);
			return response;
		} catch (Exception e) {
			LOG.error("Impossible to create the certificate");
			return ExceptionHelper.getPrismaJSONResponse(e, CAController.class);
		}
	}

	@RequestMapping(value = "async/paas/caaas/download", method = RequestMethod.GET)
	public void getFile(HttpServletRequest request,
			HttpServletResponse response, @CurrentUser PrismaUserDetails user) {
		try {
			String file = "privateKey.key";
			String privateKey = (String) request.getSession().getAttribute(
					"privateKey");
			request.getSession().removeAttribute("privateKey");
			if (privateKey != null) {
				// PrintWriter out = new PrintWriter(file);
				// out.println(privateKey);
				// out.close();
				InputStream is = new ByteArrayInputStream(privateKey.getBytes());
				// InputStream is = new FileInputStream(new File(file));
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + file + "\"");

				org.apache.commons.io.IOUtils.copy(is,
						response.getOutputStream());
				response.flushBuffer();
			}

		} catch (Exception ex) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			LOG.error("Impossible to download the private key for workgroup: "
					+ user.getActiveWorkgroupMembership().getWorkgroupId(), ex);
		}
	}

	@RequestMapping(value = "/async/paas/caaas/{serialNumber}", method = RequestMethod.GET)
	public @ResponseBody PrismaJSONResponse viewCertificateInfo(
			@PathVariable String serialNumber,
			@CurrentUser PrismaUserDetails user) {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		try {
			String infoCertificate = caManagementService.viewCertificate(user
					.getActiveWorkgroupMembership().getWorkgroupId(),
					serialNumber);
			response.setResult(infoCertificate);
			return response;
		} catch (Exception e) {
			LOG.error("Impossible to view certificates");
			return ExceptionHelper.getPrismaJSONResponse(e, CAController.class);
		}
	}

	@RequestMapping(value = "/async/paas/caaas/{serialNumber}/{reasonSelect}", method = RequestMethod.PUT)
	public @ResponseBody PrismaJSONResponse revokeCertificate(
			@PathVariable String serialNumber, @PathVariable int reasonSelect,
			@CurrentUser PrismaUserDetails user) {
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		try {
			String infoCertificate = caManagementService.revokeCertificate(user
					.getActiveWorkgroupMembership().getWorkgroupId(),
					serialNumber, reasonSelect);
			response.setResult(infoCertificate);
			return response;
		} catch (Exception e) {
			LOG.error("Impossible to revoke certificates");
			return ExceptionHelper.getPrismaJSONResponse(e, CAController.class);
		}
	}

	@RequestMapping(value = "/async/paas/caaas/downloadCertificate/{serialNumber}", method = RequestMethod.GET)
	public void downloadCertificate(@PathVariable String serialNumber,
			HttpServletResponse response, @CurrentUser PrismaUserDetails user) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			String pem = caManagementService.downloadCertificate(user
					.getActiveWorkgroupMembership().getWorkgroupId(),
					serialNumber);
			X509Certificate cert = null;
			StringReader reader = new StringReader(pem);
			PEMReader pr = new PEMReader(reader);
			cert = (X509Certificate) pr.readObject();
			pr.close();
			String file = cert.getSubjectDN().toString().replace("CN=", "")
					+ ".pem";
			if (pem != null) {
				InputStream is = new ByteArrayInputStream(pem.getBytes());

				response.reset();
				// InputStream is = new FileInputStream(new File(file));
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + file + "\"");// \"" + file +
																	// "\"");

				org.apache.commons.io.IOUtils.copy(is,
						response.getOutputStream());
				response.flushBuffer();
			}
			// return prismaResponse;
		} catch (Exception e) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			LOG.error("Impossible to download the certificate: " + e);
		}
	}
}
