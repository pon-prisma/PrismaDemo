package it.prisma.presentationlayer.webui.controllers.commons;

import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.response.AddAppSrcFileResponse;
import it.prisma.presentationlayer.webui.core.commons.GenericManagementService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.uploader.FilesResponse;
import it.prisma.presentationlayer.webui.uploader.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class FileController {

	static final Logger LOG = LogManager.getLogger(FileController.class);

	@Autowired
	private GenericManagementService<APPaaSEnvironmentRepresentation> genericService;

	@RequestMapping(value = "/fileupload", method = RequestMethod.GET)
	public String getPage() {
		return "pages/fileupload";
	}

	@RequestMapping(value = "/async/apprepo/upload", method = RequestMethod.POST)
	public @ResponseBody FilesResponse upload(
			MultipartHttpServletRequest request,
			@RequestParam("label") String label,
			@RequestParam("visibility") String visibility,
			@CurrentUser PrismaUserDetails user, HttpServletResponse response) {

		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		/**
		 * 
		 * Limit to 1 file. If multiple file iterare! while (itr.hasNext()) {
		 */

		mpf = request.getFile(itr.next());

		AddAppSrcFileRequest apiRequest = new AddAppSrcFileRequest();
		apiRequest.setAppDescription(label);
		apiRequest.setAppName(mpf.getOriginalFilename());
		apiRequest.setFileName(mpf.getOriginalFilename());

		if (!visibility.equals("public"))
			apiRequest.setPublic(false);
		else
			apiRequest.setPublic(true);

		apiRequest.setTag(label);
		apiRequest.setUserId(user.getUserData().getUserAccountId());
		apiRequest.setWorkgroupId(user.getActiveWorkgroupMembership().getWorkgroupId());

		List<Response> listOfResponse = new ArrayList<Response>();
		FilesResponse fileResponse = new FilesResponse();
		try {
			AddAppSrcFileResponse uploadedFile = genericService.uploadFile(
					apiRequest, convert(mpf));

			Response resp = new Response(uploadedFile.getAppSrcFileId(),
					mpf.getOriginalFilename());

			listOfResponse.add(resp);
			Response[] statusArray = new Response[listOfResponse.size()];
			fileResponse.files = listOfResponse.toArray(statusArray);
			return fileResponse;

		} catch (Exception e) {
			LOG.error("Exception during file upload", e);
			Response resp = new Response(-1L, "Errore durante l'upload",
					"Error " + e.getMessage());
			listOfResponse.add(resp);
			Response[] statusArray = new Response[listOfResponse.size()];
			fileResponse.files = listOfResponse.toArray(statusArray);
			return fileResponse;
		}
	}

	private File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

}
