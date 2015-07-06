package it.prisma.businesslayer.bizws;

import it.prisma.businesslayer.bizws.config.annotations.PrismaRestWSParams;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelperProvider;
import it.prisma.domain.dsl.prisma.ErrorCode;
import it.prisma.domain.dsl.prisma.OrchestratorErrorCode;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponse;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.utils.misc.StackTrace;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

@RequestScoped
public abstract class BaseWS {

	@Context
	private HttpServletResponse servletResponse;

	@Context
	private HttpServletRequest servletRequest;

	@Inject
	@PrismaRestWSParams
	private RestWSParamsContainer restWSParams;

	@Inject
	private MappingHelperProvider mhProvider;
	
	/**
	 * @return the {@link RestWSParamsContainer}, containing common RESTful
	 *         WebService parameters (pagination, filtering, ordering, etc), for
	 *         the current session.
	 */
	protected RestWSParamsContainer getSessionWSParams() {
		return restWSParams;
	}

	/**
	 * @return the {@link HttpServletRequest} for the current session.
	 */
	protected HttpServletRequest getSessionServletRequest() {
		return servletRequest;
	}

	/**
	 * @return the {@link HttpServletResponse} for the current session.
	 */
	protected HttpServletResponse getSessionServletResponse() {
		return servletResponse;
	}

	protected <ResultType> Response handleNotFoundException(String itemName) {
		return PrismaResponse
				.status(Status.NOT_FOUND,
						OrchestratorErrorCode.ORC_ITEM_NOT_FOUND, itemName)
				.build().build();
	}

	protected <ResultType> Response handleNotFoundException(
			ErrorCode errorCode, String verbose) {
		return PrismaResponse.status(Status.NOT_FOUND, errorCode, verbose)
				.build().build();
	}

	protected <ResultType> Response handleError(StatusType status,
			ErrorCode errorCode, String verbose) {
		return PrismaResponse.status(status, errorCode, verbose).build()
				.build();
	}

	protected <ResultType> Response handleError(StatusType status,
			ErrorCode errorCode, Exception e) {
		return PrismaResponse
				.status(status, errorCode, StackTrace.getStackTraceToString(e))
				.build().build();
	}

	protected <ResultType> Response handleResult(ResultType result) {
		return PrismaResponse.status(Status.OK, result).build().build();
	}

	protected Response handleGenericException(Exception e) {
		// Unexpected error occurred (probably server error)
		return PrismaResponse
				.status(Status.INTERNAL_SERVER_ERROR,
						OrchestratorErrorCode.ORC_GENERIC_ERROR,
						StackTrace.getStackTraceToString(e)).build().build();
		// return Response
		// .status(Status.OK)
		// .entity(PrismaResponseWrapper.error(
		// OrchestratorErrorCode.ORC_GENERIC_ERROR,
		// e.getStackTrace().toString()).build()).build();
	}
	
	/**
	 * Retrieve a {@link MappingHelper} suitable for the given <code>entityClass</code> and <code>dslClass</code>. 
	 * @see MappingHelperProvider#getMappingHelper(Class, Class)
	 * @param entityClass the Class of the Entity.
	 * @param dslClass the Class of the DSL.
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected  <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMH(Class<ENTITY> entityClass, Class<DSL> dslClass) throws ClassNotFoundException {
		return mhProvider.getMappingHelper(entityClass, dslClass);	
	}
	
	/**
	 * Retrieve a {@link MappingHelper} suitable for the given <code>entityClass</code> and <code>dslClass</code>. 
	 * @see MappingHelperProvider#getMappingHelper(Class, Class)
	 * @param entityClass the Class of the Entity.
	 * @param dslClass the DSL instance.
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected  <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMH(Class<ENTITY> entityClass, DSL dslClass) throws ClassNotFoundException {
		return mhProvider.getMappingHelper(entityClass, dslClass);	
	}
	
	/**
	 * Retrieve a {@link MappingHelper} suitable for the given <code>entityClass</code> and <code>dslClass</code>. 
	 * @see MappingHelperProvider#getMappingHelper(Class, Class)
	 * @param entityClass the instance of the Entity.
	 * @param dslClass the Class of the DSL.
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected  <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMH(ENTITY entityClass, Class<DSL> dslClass) throws ClassNotFoundException {
		return mhProvider.getMappingHelper(entityClass, dslClass);	
	}
	
	/**
	 * Retrieve a {@link MappingHelper} suitable for the given <code>entityClass</code> and <code>dslClass</code>. 
	 * @see MappingHelperProvider#getMappingHelper(Class, Class)
	 * @param entityClass the instance of the Entity.
	 * @param dslClass the instance of the DSL.
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected  <ENTITY, DSL> MappingHelper<ENTITY, DSL> getMH(ENTITY entityClass, DSL dslClass) throws ClassNotFoundException {
		return mhProvider.getMappingHelper(entityClass, dslClass);	
	}

}
