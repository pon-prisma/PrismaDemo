package it.prisma.presentationlayer.webui.security.userdetails;

import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;
import it.prisma.presentationlayer.webui.core.accounting.WorkgroupManagementService;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupNotFoundException;
import it.prisma.presentationlayer.webui.core.accounting.exceptions.WorkgroupOperationException;

import java.io.IOException;

public class PrismaUserWorkgroupMembership {

	private WorkgroupMembershipRepresentation workgroupMembershipRepresentation;
	private WorkgroupRepresentation workgroupRepresentation;
	private UserRepresentation workgroupMember;
	private UserRepresentation workgroupCreator;
	private UserManagementService userManagementService;
	private WorkgroupManagementService workgroupManagementService;

	public PrismaUserWorkgroupMembership(
			final WorkgroupMembershipRepresentation workgroupMembershipRepresentation, UserManagementService userManagementService, WorkgroupManagementService workgroupManagementService)
			throws WorkgroupNotFoundException, WorkgroupOperationException,
			IOException {
		this.userManagementService = userManagementService;
		this.workgroupManagementService = workgroupManagementService;
		this.workgroupMembershipRepresentation = workgroupMembershipRepresentation;		
		this.workgroupRepresentation = this.workgroupManagementService
				.getWorkgroupById(workgroupMembershipRepresentation
						.getWorkgroupId());
		this.workgroupMember = this.userManagementService
				.getUserById(workgroupMembershipRepresentation
						.getMemberUserAccountId());
		this.workgroupCreator = this.userManagementService
				.getUserById(workgroupRepresentation
						.getCreatedByUserAccountId());
	}

	public UserRepresentation getWorkgroupMember() {
		return this.workgroupMember;
	}

	public Long getWorkgroupId() {
		return this.workgroupMembershipRepresentation.getWorkgroupId();
	}

	public String getWorkgroupLabel() {
		return this.workgroupRepresentation.getLabel();
	}

	public String getWorkgroupDescription() {
		return this.workgroupRepresentation.getDescription();
	}

	public boolean isWorkgroupApproved() {
		return this.workgroupRepresentation.getApproved();
	}

	public String getWorkgroupCreatorFullName() {
		return this.workgroupCreator.getFirstName() + " "
				+ this.workgroupCreator.getLastName();
	}

	public String getWorkgroupCreatorEmail() {
		return this.workgroupCreator.getEmail();
	}

	public Long getWorkgroupPrivilegeId() {
		if (this.workgroupMembershipRepresentation.getWorkgroupPrivilege() == null)
			return null;
		return this.workgroupMembershipRepresentation.getWorkgroupPrivilege()
				.getWorkgroupPrivilegeId();
	}

	public String getWorkgroupPrivilegeName() {
		if (this.workgroupMembershipRepresentation.getWorkgroupPrivilege() == null)
			return null;
		return this.workgroupMembershipRepresentation.getWorkgroupPrivilege()
				.getWorkgroupPrivilegeName();
	}

	public String getWorkgroupPrivilegeDescription() {
		if (this.workgroupMembershipRepresentation.getWorkgroupPrivilege() == null)
			return null;
		return this.workgroupMembershipRepresentation.getWorkgroupPrivilege()
				.getWorkgroupPrivilegeDescription();
	}

}
