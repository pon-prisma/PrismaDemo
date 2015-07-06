package it.prisma.businesslayer.bizws.paas.services.appaas.apprepo;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.paas.services.appaas.response.ApplicationRepositoryEntryRepresentation;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.response.AddAppSrcFileResponse;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

/**
 * This WS manages the Application Source File Repository.
 * 
 * @author l.biava
 *
 */
@Path("apprepo")
@PrismaWrapper
public interface AppRepoWS {

	/**
	 * Upload a source file to be put in the application source file repository.
	 * 
	 * @return the data of the newly created entry using
	 *         {@link AddAppSrcFileResponse}.
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("appsrcfiles/workgroups/{workgroupId}")
	public AddAppSrcFileResponse addAppSrcFile(
			@PathParam("workgroupId") final Long workgroupId,
			@MultipartForm AppSrcFileMultipartForm form) throws Exception;

	/**
	 * Upload a source file to be put in the application source file repository.
	 * The file is retrieved from the given URL.
	 * 
	 * @return the data of the newly created entry using
	 *         {@link AddAppSrcFileResponse}.
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("appsrcfiles/workgroups/{workgroupId}/url")
	public AddAppSrcFileResponse addAppSrcFileFromURL(
			@PathParam("workgroupId") final Long workgroupId,
			AddAppSrcFileRequest request) throws Exception;

	/**
	 * Returns a list of private workgroup application source files, with
	 * pagination enabled.
	 * 
	 * @return a {@link Collection} of
	 *         {@link ApplicationRepositoryEntryRepresentation}.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("appsrcfiles/private/workgroups/{workgroupId}")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<ApplicationRepositoryEntryRepresentation> getPrivateAppSrcFiles(
			@PathParam("workgroupId") final Long workgroupId);

	/**
	 * Returns a list of public application source files, with pagination
	 * enabled.
	 * 
	 * @return a {@link Collection} of
	 *         {@link ApplicationRepositoryEntryRepresentation}.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("appsrcfiles/public")
	@PrismaWrapper(restParamsEnabled = true)
	public Collection<ApplicationRepositoryEntryRepresentation> getPublicAppSrcFiles();
}