package it.prisma.presentationlayer.webui.core.accounting;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupApprovationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupCreationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupMembershipRequest;
import it.prisma.domain.dsl.prisma.AccountingErrorCode;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.UserAccountNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupMembershipNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupOperationException;
import it.prisma.presentationlayer.webui.core.organization.OrganizationManagementService;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserCompleteProfile;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.AbstractPrismaAPIClient.PrismaMetaData;
import it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer.PrismaBizAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WorkgroupManagementService {

	// Logger
	static final Logger LOG = LogManager
			.getLogger(WorkgroupManagementService.class);

	@Autowired
	private PrismaBizAPIClient prismaBizAPIClient;

	@Value("${DEFAULT_AUTH_TOKEN}")
	String DEFAULT_AUTH_TOKEN;	

	@Autowired
	UserManagementService userManagementService;

	@Autowired
	IdPManagementService idPManagementService;

	@Autowired
	OrganizationManagementService organizationManagementService;


	public WorkgroupRepresentation createWorkgroup(final String label,
			final String description, final Long createdByUserAccountId)
					throws WorkgroupOperationException, IOException {
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();			

			return prismaBizAPIClient
					.workgroupManagerCreate(new WorkgroupCreationRequest() {
						{
							this.setLabel(label);
							this.setDescription(description);
							this.setCreatedByUserAccountId(createdByUserAccountId);
						}
					}, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public WorkgroupRepresentation getWorkgroupById(Long workgroupId)
			throws WorkgroupNotFoundException, WorkgroupOperationException,
			IOException {
		try {
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();			

			return prismaBizAPIClient
					.workgroupManagerGetWorkgroupById(workgroupId, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupRepresentation> getAllWorkgroups()
			throws WorkgroupOperationException, IOException {
		try {

			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();			

			return prismaBizAPIClient.workgroupManagerGetAllWorkgroups(meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public WorkgroupMembershipRepresentation getMembershipByWorkgroupIdAndUserAccountId(
			Long workgroupId, Long userAccountId)
					throws WorkgroupNotFoundException, WorkgroupNotFoundException,
					UserAccountNotFoundException, WorkgroupOperationException,
					IOException {
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient
					.workgroupManagerGetMembershipByWorkgroupIdAndUserAccountId(
							workgroupId, userAccountId, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllMembershipsByWorkgroupId(
			Long workgroupId, RestWSParamsContainer params, PrismaMetaData meta) 
					throws WorkgroupNotFoundException, WorkgroupOperationException, IOException 
	{
		try {
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient
					.workgroupManagerGetAllMembershipsByWorkgroupId(workgroupId, params, meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}


	public BootstrapTable<PrismaUserCompleteProfile> getAllMembershipsByWorkgroupIdForWebUI(
			Long workgroupId, RestWSParamsContainer params) throws Exception 
	{

		
		PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
		List<WorkgroupMembershipRepresentation> workgroupUsersList = this.getAllMembershipsByWorkgroupId(workgroupId, params, meta);

		BootstrapTable<PrismaUserCompleteProfile> prismaUserCompleteProfiles = new BootstrapTable<PrismaUserCompleteProfile>();

		List<PrismaUserCompleteProfile> prismaUserCompleteProfileList = new ArrayList<PrismaUserCompleteProfile>();
		for (WorkgroupMembershipRepresentation workgroupUser : workgroupUsersList)
			prismaUserCompleteProfileList.add(
					new PrismaUserCompleteProfile(
							workgroupUser, userManagementService, idPManagementService, organizationManagementService));

		prismaUserCompleteProfiles.setEnvironments(prismaUserCompleteProfileList);		
		prismaUserCompleteProfiles.setTotal(meta.getMeta().getPagination().getTotalCount());

		return prismaUserCompleteProfiles;

	}



	public List<WorkgroupMembershipRepresentation> getAllMembershipsApplicationsByWorkgroupId(
			Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupOperationException, IOException {
		try {

			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient
					.workgroupManagerGetAllMembershipsApplicationsByWorkgroupId(workgroupId, meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllApprovedMembershipsByWorkgroupId(
			Long workgroupId) throws WorkgroupNotFoundException,
			WorkgroupOperationException, IOException {
		try {

			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient
					.workgroupManagerGetAllApprovedMembershipsByWorkgroupId(workgroupId, meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllMemberships()
			throws WorkgroupOperationException, IOException {
		try {
			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient.workgroupManagerGetAllMemberships(meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllMembershipsByUserAccountId(
			Long userAccountId) throws UserAccountNotFoundException,
			WorkgroupOperationException, IOException {
		try {
			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();

			return prismaBizAPIClient.workgroupManagerGetAllMembershipsByUserAccountId(userAccountId, meta, DEFAULT_AUTH_TOKEN);
		} catch (APIErrorException e) {
			e.printStackTrace();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllMembershipApplicationsByUserAccountId(
			Long userAccountId) throws UserAccountNotFoundException, WorkgroupMembershipNotFoundException,
			WorkgroupOperationException, IOException {
		try {

			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient.workgroupManagerGetAllMembershipApplicationsByUserAccountId(userAccountId, meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllApprovedMembershipsByUserAccountId(
			Long userAccountId) throws UserAccountNotFoundException, WorkgroupMembershipNotFoundException,
			WorkgroupOperationException, IOException {
		try {

			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient.workgroupManagerGetAllApprovedMembershipsByUserAccountId(userAccountId, meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllMembershipsApplications()
			throws WorkgroupOperationException, WorkgroupMembershipNotFoundException, IOException {
		try {
			
			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient.workgroupManagerGetAllMembershipsApplications(meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public List<WorkgroupMembershipRepresentation> getAllApprovedMemberships()
			throws WorkgroupOperationException, IOException {
		try {

			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient.workgroupManagerGetAllApprovedMemberships(meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_MEMBERSHIP_NOT_FOUND
					.getCode())
				throw new WorkgroupMembershipNotFoundException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public void applicateForMembership(Long workgroupId,
			Long applicantUserAccountId) throws WorkgroupNotFoundException,
			UserAccountNotFoundException, WorkgroupOperationException,
			IOException {
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			prismaBizAPIClient.workgroupManagerApplicateForMembership(
					workgroupId, applicantUserAccountId, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public void addWorkgroupAdminMembership(final Long workgroupId, final Long applicantUserAccountId,
			final Long approvedByUserAccountId)
					throws UserAccountNotFoundException, WorkgroupNotFoundException,
					WorkgroupOperationException, IOException {
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			prismaBizAPIClient.workgroupManagerAddWorkgroupAdminMembership(new WorkgroupMembershipRequest() {{
				this.setWorkgroupId(workgroupId);
				this.setApprovedByUserAccountId(approvedByUserAccountId);
				this.setApplicantUserAccountId(applicantUserAccountId);
			}}, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public void addWorkgroupUserMembership(final Long workgroupId, final Long applicantUserAccountId,
			final Long approvedByUserAccountId)
					throws UserAccountNotFoundException, WorkgroupNotFoundException,
					WorkgroupOperationException, IOException {
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			prismaBizAPIClient.workgroupManagerAddWorkgroupAdminMembership(new WorkgroupMembershipRequest() {{
				this.setWorkgroupId(workgroupId);
				this.setApprovedByUserAccountId(approvedByUserAccountId);
				this.setApplicantUserAccountId(applicantUserAccountId);
			}}, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}

	public void addWorkgroupGuestMembership(final Long workgroupId, final Long applicantUserAccountId,
			final Long approvedByUserAccountId)
					throws UserAccountNotFoundException, WorkgroupNotFoundException,
					WorkgroupOperationException, IOException {
		try {
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			prismaBizAPIClient.workgroupManagerAddWorkgroupAdminMembership(new WorkgroupMembershipRequest() {{
				this.setWorkgroupId(workgroupId);
				this.setApprovedByUserAccountId(approvedByUserAccountId);
				this.setApplicantUserAccountId(applicantUserAccountId);
			}}, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}



	public void removeMembership(Long workgroupId, Long memberUserAccountId)
			throws UserAccountNotFoundException, WorkgroupNotFoundException,
			WorkgroupOperationException, IOException {
		throw new UnsupportedOperationException();
	}


	public void unapproveMembership(Long workgroupId,
			Long applicantUserAccountId) throws UserAccountNotFoundException,
			WorkgroupNotFoundException, WorkgroupOperationException,
			IOException {
		throw new UnsupportedOperationException();
	}


	public void approveWorkgroup(final Long workgroupId, final Long approvedByUserAccountId)
			throws UserAccountNotFoundException, WorkgroupNotFoundException,
			WorkgroupOperationException, IOException {
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			prismaBizAPIClient
			.workgroupManagerApproveWorkgroup(new WorkgroupApprovationRequest() {
				{
					this.setWorkgroupId(workgroupId);
					this.setApprovedByUserAccountId(approvedByUserAccountId);
				}
			}, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}


	public List<UserRepresentation> getWorkgroupReferentsByWorkgroupId(final Long workgroupId) throws IOException
	{
		try {

			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient.workgroupManagerGetWorkgroupReferentsByWorkgroupId(workgroupId, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}
	
	
	public List<WorkgroupRepresentation> getReferencedWorkgroupsByUserAccountId(final Long userAccountId) throws IOException, WorkgroupNotFoundException
	{
		try {

			PrismaMetaData meta = prismaBizAPIClient.new PrismaMetaData();
			
			AuthTokenRepresentation authToken = ( (PrismaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).getUserToken();

			return prismaBizAPIClient.workgroupManagerGetReferencedWorkgroupsByUserAccountId(userAccountId, meta, authToken.getTokenId());
		} catch (APIErrorException e) {
			if (e.getAPIError().getErrorCode() == OrchestratorErrorCode.ORC_ITEM_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_WG_NOT_FOUND
					.getCode())
				throw new WorkgroupNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_USER_NOT_FOUND
					.getCode())
				throw new UserAccountNotFoundException();
			else if (e.getAPIError().getErrorCode() == AccountingErrorCode.ACC_BAD_REQUEST
					.getCode())
				throw new WorkgroupOperationException();
			else
				throw new IOException();
		} catch (RestClientException e) {
			throw new IOException();
		} catch (ServerErrorResponseException e) {
			throw new IOException();
		} catch (MappingException | NoMappingModelFoundException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new IOException();
		}
	}
	
	public void unapproveWorkgroup(Long workgroupId)
			throws WorkgroupNotFoundException, WorkgroupOperationException,
			IOException {
		throw new UnsupportedOperationException();
	}



}
