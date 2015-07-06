package it.prisma.presentationlayer.webui.security.userdetails;

import it.prisma.domain.dsl.accounting.organizations.IdentityProviderRepresentation;
import it.prisma.domain.dsl.accounting.organizations.OrganizationRepresentation;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.presentationlayer.webui.core.accounting.IdPManagementService;
import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;
import it.prisma.presentationlayer.webui.core.organization.OrganizationManagementService;

public class PrismaUserCompleteProfile {

	private UserRepresentation user;
	private IdentityProviderRepresentation idpInfo;
	private OrganizationRepresentation organization;
	

	public PrismaUserCompleteProfile(
			WorkgroupMembershipRepresentation workgroupUser,
			UserManagementService userManagementService,
			IdPManagementService idPManagementService,
			OrganizationManagementService organizationManagementService)
					throws Exception {

		this.user = userManagementService.getUserById(workgroupUser.getMemberUserAccountId());
		this.idpInfo = idPManagementService.getIdpInfoWithIdPId(user.getIdentityProviderId());
		this.organization = organizationManagementService.getOrganizationById(String.valueOf(idpInfo.getOrganizationId())); 		
	}

	public Long getUserAccountId()
	{
		return this.user.getUserAccountId();
	}
	
	public String getIdPOrganizationName() {
		return this.organization.getName();
	}

	public String getMemberUsername() {
		return this.user.getNameIdOnIdentityProvider();
	}

	public String getMemberFullName() {
		
		return 	this.user.getFirstName() + " " + 
				(this.user.getMiddleName() == null ? "" : this.user.getMiddleName()) + " " +
				this.user.getLastName();
	}

	public String getEmployer() {
		return this.user.getEmployer();
	}

	public String getMemberEmail() {
		return this.user.getEmail();
	}

}
