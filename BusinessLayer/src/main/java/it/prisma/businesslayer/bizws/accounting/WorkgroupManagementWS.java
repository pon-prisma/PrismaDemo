package it.prisma.businesslayer.bizws.accounting;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.accounting.users.UserRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupMembershipRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.WorkgroupRepresentation;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupApprovationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupCreationRequest;
import it.prisma.domain.dsl.accounting.workgroups.requests.WorkgroupMembershipRequest;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/accounting")
@PrismaWrapper
public interface WorkgroupManagementWS {

	
	
	// Create

	@PUT
	@Path("/workgroups/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public WorkgroupRepresentation create(WorkgroupCreationRequest workgroupCreationrequest);

	// Read

	@GET
	@Path("/workgroups/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public WorkgroupRepresentation getWorkgroupById(@PathParam("id") Long workgroupId);

	@GET
	@Path("/workgroups/")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupRepresentation> getAllWorkgroups();

	@GET
	@Path("/workgroups/{id}/memberships/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public WorkgroupMembershipRepresentation getMembershipByWorkgroupIdAndUserAccountId(
			@PathParam("id") Long workgroupId,
			@PathParam("user-id") Long userAccountId);
	
	@GET
	@Path("/workgroups/{id}/memberships/")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsByWorkgroupId(@PathParam("id") Long workgroupId);
	
	@GET
	@Path("/workgroups/{id}/memberships/applications")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsApplicationsByWorkgroupId(@PathParam("id") Long workgroupId);
	
	@GET
	@Path("/workgroups/{id}/memberships/approved")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllApprovedMembershipsByWorkgroupId(@PathParam("id") Long workgroupId);
	

	@GET
	@Path("/workgroups/{id}/referents")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<UserRepresentation> getWorkgroupReferentsByWorkgroupId(@PathParam("id") Long workgroupId);
	
	@GET
	@Path("/workgroups/memberships/")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllMemberships();
	
	@GET
	@Path("/workgroups/memberships/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsByUserAccountId(@PathParam("user-id") Long userAccountId);

	@GET
	@Path("/workgroups/memberships/{user-id}/applications")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipApplicationsByUserAccountId(@PathParam("user-id") Long userAccountId);
	
	@GET
	@Path("/workgroups/memberships/{user-id}/approved")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllApprovedMembershipsByUserAccountId(@PathParam("user-id") Long userAccountId);
	

	@GET
	@Path("/workgroups/referents/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupRepresentation> getReferencedWorkgroupsByUserAccountId(@PathParam("user-id") Long userAccountId);
	
	
	@GET
	@Path("/workgroups/memberships/applications")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllMembershipsApplications();
	
	@GET
	@Path("/workgroups/memberships/approved")
	@Produces(MediaType.APPLICATION_JSON)
//	@PrismaWrapper(restParamsEnabled = true)
	public Collection<WorkgroupMembershipRepresentation> getAllApprovedMemberships();
	
	// Update
	
	@PUT
	@Path("/workgroups/{id}/memberships/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void applicateForMembership(@PathParam("id") Long workgroupId,
			@PathParam("user-id") Long userAccountId);
	
	@PUT
	@Path("/workgroups/memberships/wg-admin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void addWorkgroupAdminMembership(WorkgroupMembershipRequest WorkgroupMembershipRequest);
	
	@PUT
	@Path("/workgroups/memberships/wg-user")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void addWorkgroupUserMembership(WorkgroupMembershipRequest WorkgroupMembershipRequest);
	
	@PUT
	@Path("/workgroups/memberships/wg-guest")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void addWorkgroupGuestMembership(WorkgroupMembershipRequest WorkgroupMembershipRequest);
	
	@DELETE
	@Path("/workgroups/{id}/memberships/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void removeMembership(@PathParam("id") Long workgroupId,
			@PathParam("user-id") Long userAccountId);

	@DELETE
	@Path("/workgroups/{id}/memberships/{user-id}/unapprove")
	@Produces(MediaType.APPLICATION_JSON)
	public void unapproveMembership(@PathParam("id") Long workgroupId,
			@PathParam("user-id") Long userAccountId);
	
	@PUT
	@Path("/workgroups/approve")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void approveWorkgroup(WorkgroupApprovationRequest workgroupApprovationRequest);
	
	@DELETE
	@Path("/workgroups/{id}/unpprove")
	@Produces(MediaType.APPLICATION_JSON)
	public void unapproveWorkgroup(@PathParam("id") Long workgroupId);
	
}
