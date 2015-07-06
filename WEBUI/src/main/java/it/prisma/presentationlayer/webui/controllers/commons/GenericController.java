package it.prisma.presentationlayer.webui.controllers.commons;

import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.event.Events;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.commons.GenericManagementService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.uploader.FilesResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class GenericController {

	static final Logger LOG = LogManager.getLogger(GenericController.class.getName());

	@Autowired
	private GenericManagementService<DBaaSRepresentation> genericService;
	
	
	@RequestMapping(value = "service/workgroups/{workgroup}/{serviceType}/{service}/{id}/{action}", method = RequestMethod.POST)
	public @ResponseBody boolean startAndStopService(@PathVariable String workgroup, @PathVariable String serviceType, @PathVariable String service,
			@PathVariable String id, @PathVariable String action) {
		try {
			return genericService.startAndStopVM(workgroup, serviceType, service, id, action);
		} catch (Exception e) {
			return false;
		}
	}
	
//	@RequestMapping(value = "service/workgroups/{workgroup}/{serviceType}/{service}/{id}", method = RequestMethod.DELETE)
//	public @ResponseBody boolean delete(@PathVariable String workgroup, @PathVariable String serviceType, @PathVariable String service,
//			@PathVariable String id) {
//		
//		return genericService.deleteService(workgroup, serviceType, service, id, null);
//	}
	
	@RequestMapping(value = "service/async/{service}/{serviceType}/{id}/events", method = RequestMethod.GET)
	public @ResponseBody Events getEventsJson(@PathVariable String service, @PathVariable String serviceType, @PathVariable String id, @PrismaRestWSParams RestWSParamsContainer params) {
		
		LOG.debug("Update Events for service: " + serviceType + " with ID: " + id);

		//TODO creare il metodo con paramentro di ricerca
		return genericService.getEvents(service, serviceType, Long.parseLong(id), params);

	}
		
//	@RequestMapping(value = "async/file/upload", method = RequestMethod.POST)
//	public @ResponseBody
//	FilesResponse appSrcFileUpload(@RequestParam("file") MultipartFile file, 
//		// @CurrentUser PrismaUserDetails user,
//		HttpServletRequest request) {
//
//		
//		AddAppSrcFileRequest apiRequest = new AddAppSrcFileRequest();
//		apiRequest.setAppDescription("descrizione");
//		apiRequest.setAppName(file.getName());
//		apiRequest.setFileName(file.getName());
//		apiRequest.setPublic(false);
//		apiRequest.setTag("hfjjjhb");
//		apiRequest.setUserId(1L);
//		apiRequest.setWorkgroupId(1L);
//		
//		List<Response> listOfResponse = new ArrayList<Response>();
//		FilesResponse fileResponse = new FilesResponse();
//		try {
//			AddAppSrcFileResponse uploadedFile = genericService.uploadFile(apiRequest, convert(file), null);
//			
//			Response resp = new Response(uploadedFile.getAppSrcFileId(), file.getOriginalFilename());
//			
//			listOfResponse.add(resp);
//			Response[] statusArray = new Response[listOfResponse.size()];
//			fileResponse.files = listOfResponse.toArray(statusArray);
//			return fileResponse;
//			
//		} catch (Exception e){
//			Response resp = new Response("Errore durante l'upload",
//					file.getSize(), "Error" + e.getMessage());
//			listOfResponse.add(resp);
//			Response[] statusArray = new Response[listOfResponse.size()];
//			fileResponse.files = listOfResponse.toArray(statusArray);
//			return fileResponse;
//		}
//	}

	@RequestMapping(value = "async/file/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Boolean> deleteFile(
	// @CurrentUser PrismaUserDetails user,
			@RequestParam("filename") String name) {

		// LOG.debug(user.getUsername() + " wants delete " + name);

		// Preparo la risposta generica
		Map<String, Boolean> success = new HashMap<>();
		success.put("success", false);

	
		//TODO richedere al backend di cancellare il certificato
		success.put("success", true);
		return success;
	}
	
	private File convert(MultipartFile file) throws IOException {    
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "async/cert/upload", method = RequestMethod.POST)
	public @ResponseBody FilesResponse certUpload(@RequestParam("file") MultipartFile file,
	// @CurrentUser PrismaUserDetails user,
			HttpServletRequest request) {
		
		//TODO
		return null;
//		AddAppSrcFileRequest apiRequest = new AddAppSrcFileRequest();
//		apiRequest.setAppDescription("descrizione");
//		apiRequest.setAppName(file.getName());
//		apiRequest.setFileName(file.getName());
//		apiRequest.setPublic(false);
//		apiRequest.setTag("hfhb");
//		apiRequest.setUserId(1L);
//		apiRequest.setWorkgroupId(1L);
//		
//		List<Response> listOfResponse = new ArrayList<Response>();
//		FilesResponse fileResponse = new FilesResponse();
//		try {
//			AddAppSrcFileResponse uploadedFile = genericService.uploadFile(apiRequest, convert(file), null);
//			
//			Response resp = new Response(file.getOriginalFilename(),
//					file.getSize(), "String url", "String thumb",
//					"String deleteURL");
//			
//			listOfResponse.add(resp);
//			Response[] statusArray = new Response[listOfResponse.size()];
//			fileResponse.files = listOfResponse.toArray(statusArray);
//			return fileResponse;
//			
//		} catch (Exception e){
//			Response resp = new Response("Errore durante l'upload",
//					file.getSize(), "Error" + e.getMessage());
//			listOfResponse.add(resp);
//			Response[] statusArray = new Response[listOfResponse.size()];
//			fileResponse.files = listOfResponse.toArray(statusArray);
//			return fileResponse;
//		}
	}

	@RequestMapping(value = "async/cert/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Boolean> deleteCert(
	// @CurrentUser PrismaUserDetails user,
			@RequestParam("filename") String name) {

		// LOG.debug(user.getUsername() + " wants delete " + name);

		// Preparo la risposta generica
		Map<String, Boolean> success = new HashMap<>();
		success.put("success", false);

	
		//TODO richedere al backend di cancellare il certificato
		success.put("success", true);
		return success;
	}
	
	/**
	 * Used in synchronous validation
	 * 
	 * @param name
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "async/name/available", method = RequestMethod.GET)
	public @ResponseBody boolean checkNameAvailability(@RequestParam("name") String name, @CurrentUser PrismaUserDetails user){
		return genericService.checkNameAvailable(user.getActiveWorkgroupMembership().getWorkgroupId(), name);
	}

	/**
	 *  Used in asynchronous validation with jquery validation: method remote
	 * 
	 * @param name
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "async/name/available", method = RequestMethod.POST)
	public @ResponseBody boolean checkNameAvailable(@RequestParam("name") String name, @CurrentUser PrismaUserDetails user){
		return genericService.checkNameAvailable(user.getActiveWorkgroupMembership().getWorkgroupId(), name);
	}
	
	/**
	 * Used in synchronous validation
	 * 
	 * @param dn
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "async/dns/available", method = RequestMethod.GET)
	public @ResponseBody boolean checkDNSAvailability(@RequestParam("dn") String dn, @CurrentUser PrismaUserDetails user){
		
		return genericService.checkDNSAvailable(user.getActiveWorkgroupMembership().getWorkgroupId(), dn);
	}
	
	/**
	 * Used in asynchronous validation with jquery validation: method remote
	 *
	 * @param dns
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "async/dns/available", method = RequestMethod.POST)
	public @ResponseBody boolean checkDNSAvailable(@RequestParam("dns") String dns, @CurrentUser PrismaUserDetails user){
		return genericService.checkDNSAvailable(user.getActiveWorkgroupMembership().getWorkgroupId(), dns);
	}
}