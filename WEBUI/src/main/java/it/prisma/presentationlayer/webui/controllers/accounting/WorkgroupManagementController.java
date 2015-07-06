package it.prisma.presentationlayer.webui.controllers.accounting;

import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;
import it.prisma.presentationlayer.webui.core.accounting.WorkgroupManagementService;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.UserAccountNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupMembershipNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupOperationException;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.accounting.WGEditForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserCompleteProfile;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserWorkgroupMembership;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaWorkgroup;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.accounting.WGPrivileges;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.presentationlayer.webui.vos.platformconfig.SelectHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WorkgroupManagementController {

	static final Logger LOG = LogManager
			.getLogger(WorkgroupManagementController.class);

	@Autowired
	WorkgroupManagementService workgroupManagementService;

	@Autowired
	UserManagementService userManagementService;

	@RequestMapping(value = "/workgroups", method = RequestMethod.GET)
	public String showWorkgroups(@CurrentUser PrismaUserDetails user,
			Model model) {
		if (user.getActiveWorkgroupMembership() == null)
			LOG.info("The user <" + user.getUserData().getUserAccountId()
					+ "> has no active workgroups");
		else
			LOG.info("The user <" + user.getUserData().getUserAccountId()
					+ "> is currently active as member of the workgroup <"
					+ user.getActiveWorkgroupMembership().getWorkgroupId()
					+ ">");
		
		try {
			// Current user's workgroups

			Collection<WorkgroupMembershipRepresentation> rawMemberships = workgroupManagementService
					.getAllMembershipsByUserAccountId(user.getUserData()
							.getUserAccountId());

			List<PrismaUserWorkgroupMembership> memberships = this
					.populatePrismaUserWorkgroupMembershipObjects(rawMemberships);

			
			if (memberships.size() == 1)
				LOG.info("There is " + memberships.size()
						+ " membership for user: <"
						+ user.getUserData().getUserAccountId() + ">");
			else
				LOG.info("There are " + memberships.size()
						+ " memberships for user: <"
						+ user.getUserData().getUserAccountId() + ">");

				
			model.addAttribute("memberships", memberships);

			List<PrismaWorkgroup> allReferencedWorkgroups = this.populatePrismaWorkgroupObjects(
					workgroupManagementService.getReferencedWorkgroupsByUserAccountId(user.getUserData().getUserAccountId()));
			
			model.addAttribute("allReferencedWorkgroups", allReferencedWorkgroups);

		}
		catch (WorkgroupNotFoundException e) {
			LOG.info("There aren't referenced workgroups for user: <"
					+ user.getUserData().getUserAccountId() + ">");
			model.addAttribute("allReferencedWorkgroups", null);
		}
		catch (WorkgroupMembershipNotFoundException e) {
			LOG.info("There aren't membership for user: <"
					+ user.getUserData().getUserAccountId() + ">");
			model.addAttribute("memberships", null);
		} catch (UserAccountNotFoundException | WorkgroupOperationException
				| IOException e) {
			return "redirect:/500";
		}

		String userWorkgroupPrivilege = user.getActiveWorkgroupMembership().getWorkgroupPrivilege().getWorkgroupPrivilegeDescription();

		switch (WGPrivileges.getPrivilegeFromPrivilegeString(userWorkgroupPrivilege)) {

		case WG_ADMIN:
		{
			return "pages/accounting/workgroups/admin-workgroups-management";
		}
		case WG_USER:
		{
			return "pages/accounting/workgroups/user-workgroups-management";
		}
		default:
			return "redirect:/500";
		}

	}
	
	
	@RequestMapping(value = "/workgroups/workgroup-details/{workgroupId}", method = RequestMethod.GET)
	public String workgroupDetails(
			@PathVariable Long workgroupId, @CurrentUser PrismaUserDetails user, Model model) 
	{
		
		try {
			
			WorkgroupRepresentation rawWG = workgroupManagementService.getWorkgroupById(workgroupId);
			
			PrismaWorkgroup prismaWorkgroup = new PrismaWorkgroup(rawWG, userManagementService);
			
			model.addAttribute("prismaWorkgroup", prismaWorkgroup);
			
		} catch (WorkgroupMembershipNotFoundException e) {
			LOG.info("There aren't membership for user: <"
					+ user.getUserData().getUserAccountId() + ">");
			model.addAttribute("memberships", null);
		} catch (UserAccountNotFoundException | WorkgroupOperationException
				| IOException e) {
			return "redirect:/500";
		}

		String userWorkgroupPrivilege = user.getActiveWorkgroupMembership().getWorkgroupPrivilege().getWorkgroupPrivilegeDescription();

		switch (WGPrivileges.getPrivilegeFromPrivilegeString(userWorkgroupPrivilege)) {

		case WG_ADMIN:
		{
			return "pages/accounting/workgroups/workgroup-details";
		}
		case WG_USER: // TODO Only the Admin can view the WG infos?
		default:
			return "redirect:/500";
		}

	}
	
	
	@RequestMapping(value = "/workgroups/workgroup-edit/{workgroupId}", method = RequestMethod.GET)
	public String workgroupEdit(
			@PathVariable Long workgroupId, @CurrentUser PrismaUserDetails user, Model model)
	{
		
		try {
			
			WorkgroupRepresentation rawWG = workgroupManagementService.getWorkgroupById(workgroupId);
			
			PrismaWorkgroup prismaWorkgroup = new PrismaWorkgroup(rawWG, userManagementService);
			
			model.addAttribute("prismaWorkgroup", prismaWorkgroup);

			String referentsString = ""; 
			ArrayList<UserRepresentation> referentsList = prismaWorkgroup.getWorkgroupReferents();
			for (UserRepresentation userRepresentation : referentsList) {
				referentsString += userRepresentation.getUserAccountId() + 
								   " (" + userRepresentation.getFirstName() + " "
								   		+ ((userRepresentation.getMiddleName() != null) ? userRepresentation.getMiddleName() : "") + " "
								   		+ userRepresentation.getLastName() + ") - "; 
			}
			
			referentsString = referentsString.substring(0,  referentsString.length() - 3);
			
			model.addAttribute("referents", SelectHelper
					.generateSelectElementsWithoutKeys(referentsString));
			
			
			String usersString = ""; 
			
			ArrayList<UserRepresentation> usersList = userManagementService.getAllUsers();
			
			for (UserRepresentation userRepresentation : usersList) {
				usersString += userRepresentation.getUserAccountId() + 
						   		" (" + userRepresentation.getFirstName() + " "
						   		+ ((userRepresentation.getMiddleName() != null) ? userRepresentation.getMiddleName() : "") + " "
						   		+ userRepresentation.getLastName() + ") - "; 
			}
			
			usersString = usersString.substring(0,  usersString.length() - 3);
			
			model.addAttribute("users", SelectHelper
					.generateSelectElementsWithoutKeys(usersString));
			

		} catch (WorkgroupMembershipNotFoundException e) {
			LOG.info("There aren't membership for user: <"
					+ user.getUserData().getUserAccountId() + ">");
			model.addAttribute("memberships", null);
		} catch (UserAccountNotFoundException | WorkgroupOperationException
				| IOException e) {
			return "redirect:/500";
		}

		String userWorkgroupPrivilege = user.getActiveWorkgroupMembership().getWorkgroupPrivilege().getWorkgroupPrivilegeDescription();

		switch (WGPrivileges.getPrivilegeFromPrivilegeString(userWorkgroupPrivilege)) {

		case WG_ADMIN:
		{
			return "pages/accounting/workgroups/workgroup-edit";
		}
		case WG_USER: // TODO Only the Admin can edit the WG infos?
		default:
			return "redirect:/500";
		}

	}
	
	

	@RequestMapping(value = "/async/accounting/workgroup/{workgroupId}/users", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<PrismaUserCompleteProfile> getWorkgroupUsersForWorkgroupId(
			@PathVariable String workgroupId, @PrismaRestWSParams RestWSParamsContainer params, HttpServletResponse response) 
			{	

		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) 
		{			
			try {

				BootstrapTable<PrismaUserCompleteProfile> workgroupUsersList = null;
				workgroupUsersList = workgroupManagementService.getAllMembershipsByWorkgroupIdForWebUI(Long.valueOf(workgroupId), params);
				
				return workgroupUsersList;

			} catch (Exception e) {
				response.setStatus(500);
				return null;
			}
		} else {

			LOG.trace("Trying to access the AJAX /async/accounting/workgroup/{workgroupId}/users endpoint but "
					+ "the user is not authenticated. Returning null.");
			return null;
		}

	}

	@RequestMapping(value = "/async/accounting/workgroup/{workgroupId}/edit", method = RequestMethod.POST)
	public @ResponseBody PrismaJSONResponse deployEnv(@Valid WGEditForm form, @PathVariable String workgroupID, 
			BindingResult bindingResult, Model model, @CurrentUser PrismaUserDetails user) 
	{
		
		/*
		 * 
		 * 		APPaaSEnvironmentRepresentation envRap = null;
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		List<PrismaError> errors = new ArrayList<PrismaError>();

		if (bindingResult.hasErrors()) {
			response.setSuccess(false);
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {
					FieldError fieldError = (FieldError) object;
					errors.add(new PrismaError(PrismaErrorCode.GENERIC,
							fieldError.getCode(), fieldError.getField()));
				}
			}
			response.setResult(errors);
			return response;
		} else {

			Account account = new Account();
			account.setUserId(1L);
			account.setWorkgroupId(1L);
			Environment env = new Environment();
			env.setIaaSFlavor(form.getFlavorSelect());
			env.setQos(form.getQosSelect());
			env.setAvailabilityZone(form.getZoneSelect());
			
			ServiceDetails svcDet = new ServiceDetails();
			svcDet.setEnvironment(env);
			svcDet.setName(form.getServiceName());
			svcDet.setDescription(form.getDescription());
			svcDet.setDomainName(form.getUrl() + form.getDomainName());
			svcDet.setNotificationEmail(form.getNotificationEmail());
			svcDet.setPublicVisibility(true);
			
			//TODO
			svcDet.setSecureConnection(null);

			EnvironmentDetails envDetails = new EnvironmentDetails();
			envDetails.setAppId(appId);
			envDetails.setServerType(form.getServerName());

			APPaaSEnvironmentDeployRequest appEnvReq = new APPaaSEnvironmentDeployRequest();
			appEnvReq.setAccount(account);
			appEnvReq.setServiceDetails(svcDet);
			appEnvReq.setEnvironmentDetails(envDetails);
			
			switch (form.getSource()) {
			case "localApp":
				envDetails.setAppFileId(form.getFileuploadId());
				break;
			case "prismaApp":
				envDetails.setAppFileId(form.getFileURLId());
				break;
			case "privateApp":
				envDetails.setAppFileId(form.getInputPrivateId());
				break;
			case "publicApp":
				envDetails.setAppFileId(form.getInputPublicId());
				break;
			}


			try {
				IstanceNumber scalingNumber = IstanceNumber.lookupFromName(form
						.getScalingSelect());
				
				svcDet.setLoadBalancingInstances(scalingNumber.getNumber());
			} catch (IllegalArgumentException e) {
				LOG.error("Unable to find number of istance in enum, used default value");
				svcDet.setLoadBalancingInstances(1);
			}

			try {
				envRap = appEnvManagementService.deployEnv(appEnvReq);

				EnvDeployResponse envResp = new EnvDeployResponse();
				envResp.setAppId(envRap.getAppaaSId());
				envResp.setEnvId(envRap.getId());
				envResp.setName("appaas");
				response.setResult(envResp);
				return response;
			} catch (Exception e) {
				return ExceptionHelper.getPrismaJSONResponse(e,
						AppEnvController.class);
			}
		}
		
		 */
		/*
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) 
		{			
			try {

				BootstrapTable<PrismaUserCompleteProfile> workgroupUsersList = null;
				workgroupUsersList = workgroupManagementService.getAllMembershipsByWorkgroupIdForWebUI(Long.valueOf(workgroupId), params);
				
				return workgroupUsersList;

			} catch (Exception e) {
				response.setStatus(500);
				return null;
			}
		} else {

			LOG.trace("Trying to access the AJAX /async/accounting/workgroup/{workgroupId}/users endpoint but "
					+ "the user is not authenticated. Returning null.");
			return null;
		}*/
		
		return null;

	}




	private List<PrismaUserWorkgroupMembership> populatePrismaUserWorkgroupMembershipObjects(
			final Collection<WorkgroupMembershipRepresentation> memberships) {
		List<PrismaUserWorkgroupMembership> objs = new ArrayList<PrismaUserWorkgroupMembership>();
		for (WorkgroupMembershipRepresentation m : memberships)
			try {
				objs.add(new PrismaUserWorkgroupMembership(m,
						userManagementService, workgroupManagementService));


			} catch (WorkgroupNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WorkgroupOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return objs;
	}

	private List<PrismaWorkgroup> populatePrismaWorkgroupObjects(
			final Collection<WorkgroupRepresentation> workgroups) {
		List<PrismaWorkgroup> objs = new ArrayList<PrismaWorkgroup>();
		for (WorkgroupRepresentation w : workgroups)
			try {
				objs.add(new PrismaWorkgroup(w, userManagementService));
			} catch (WorkgroupNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WorkgroupOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return objs;
	}


	//	private BootstrapTable<PrismaUserCompleteProfile> populatePrismaUserCompleteProfileObjects(
	//			final List<WorkgroupMembershipRepresentation> workgroupUsersList) throws Exception 
	//			
	//	{
	//		List<PrismaUserCompleteProfile> objs = new ArrayList<PrismaUserCompleteProfile>();
	//		for (WorkgroupMembershipRepresentation workgroupUser : workgroupUsersList)
	//			objs.add(new PrismaUserCompleteProfile(workgroupUser, userManagementService, idPManagementService, organizationManagementService));
	//
	//		return objs;
	//	}


}
