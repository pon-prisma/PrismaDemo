package it.prisma.presentationlayer.webui.security.userdetails;

import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.core.AuthenticationException;

public class PrismaWorkgroup {

	private WorkgroupRepresentation workgroup;
	private UserManagementService userManagementService;
	private UserRepresentation workgroupCreator;
	private UserRepresentation workgroupApprover;
	private ArrayList<UserRepresentation> workgroupReferents;

	public PrismaWorkgroup(WorkgroupRepresentation workgroup,
			UserManagementService userManagementService)
			throws AuthenticationException, IOException {
		this.workgroup = workgroup;
		this.userManagementService = userManagementService;
		this.workgroupCreator = this.userManagementService
				.getUserById(workgroup.getCreatedByUserAccountId());
		this.workgroupApprover = this.userManagementService
				.getUserById(workgroup.getApprovedByUserAccountId());
		this.workgroupReferents = new ArrayList<UserRepresentation>(workgroup.getReferentUsers());
	}

	public long getWorkgroupId() {
		return this.workgroup.getWorkgroupId();
	}

	public String getWorkgroupLabel() {
		return this.workgroup.getLabel();
	}

	public String getWorkgroupDescription() {
		return this.workgroup.getDescription();
	}

	public boolean isWorkgroupApproved() {
		return this.workgroup.getApproved();
	}
	
	public long countWorkgroupMembers() {
		return this.workgroup.getTotalMembersNumber();
	}

	public long countWorkgroupApplicantNumber() {
		return this.workgroup.getApplicantsNumber();
	}
	
	public long countWorkgroupApprovedMembersNumber() {
		return this.workgroup.getApprovedMembersNumber();
	}	
	
	public String getWorkgroupApproverFullName() {
		return 
				this.workgroupApprover.getFirstName() + " " + 
				((workgroupApprover.getMiddleName().equals("")) ? "" : workgroupApprover.getMiddleName()) + " " + 
				this.workgroupApprover.getLastName();
	}

	public String getWorkgroupApproverEmail() {
		return this.workgroupApprover.getEmail();
	}

	public String getWorkgroupApproverRefEmail() {
		return this.workgroupApprover.getEmailRef();
	}
	
	public String getWorkgroupApproverEmployer() {
		return this.workgroupApprover.getEmployer();
	}
	
	public String getWorkgroupApproverPersonalNumber() {
		return this.workgroupApprover.getPersonalPhone();
	}
	
	public String getWorkgroupApproverWorkNumber() {
		return this.workgroupApprover.getWorkPhone();
	}
	
	
	public String getWorkgroupCreatorFullName() {
		return 
				this.workgroupCreator.getFirstName() + " " +
				((workgroupCreator.getMiddleName().equals("")) ? "" : workgroupCreator.getMiddleName()) + " " +
				this.workgroupCreator.getLastName();
	}

	public String getWorkgroupCreatorEmail() {
		return this.workgroupCreator.getEmail();
	}

	public String getWorkgroupCreatorRefEmail() {
		return this.workgroupCreator.getEmailRef();
	}
	
	public String getWorkgroupCreatorEmployer() {
		return this.workgroupCreator.getEmployer();
	}
	
	public String getWorkgroupCreatorPersonalNumber() {
		return this.workgroupCreator.getPersonalPhone();
	}
	
	public String getWorkgroupCreatorWorkNumber() {
		return this.workgroupCreator.getWorkPhone();
	}

	public ArrayList<UserRepresentation> getWorkgroupReferents() {
		return workgroupReferents;
	}
	
	
	
}
