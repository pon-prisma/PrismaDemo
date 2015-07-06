package it.prisma.presentationlayer.webui.controllers;

import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.argumentresolver.RestWSParamsResolver;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.datavalidation.forms.IDeployForm;
import it.prisma.presentationlayer.webui.security.userdetails.CurrentUserHandlerMethodArgumentResolver;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * 
 * The <code>IPrismaController</code> component
 * <br/><br/>
 * <b>Use annotations in the concrete classes</b>
 *
 * @author Reply
 *
 * @param <T> the IPrismaController type of the service to be created
 * @param <E> the form type used to create the service
 */
public interface IPrismaController<T, E extends IDeployForm> {

	/**
	 * It shows the main page of the service
	 * 
	 * @param user of type {@link PrismaUserDetails} filled by the {@link CurrentUserHandlerMethodArgumentResolver} using the annotation {@link PrismaRestWSParams}
	 * @return the main page
	 */
	public String listServicePage(PrismaUserDetails user, Model model);
	
	
	/**
	 * Retreive the list of created service. Call this method to fill a {@link BootstrapTable}
	 * 
	 * @param params of type {@link RestWSParamsContainer} filled by the {@link RestWSParamsResolver} using the annotation {@link PrismaRestWSParams}
	 * @param user of type {@link PrismaUserDetails} filled by the {@link CurrentUserHandlerMethodArgumentResolver} using the annotation {@link PrismaRestWSParams}
	 * @param response of type {@link HttpServletResponse}
	 * @return {@link BootstrapTable<T>}
	 */
	public BootstrapTable<T> getAllServicesTableRepresentation(RestWSParamsContainer params, PrismaUserDetails user, HttpServletResponse response);

	
	/**
	 * It shows the page of creation
	 * 
	 * @param user of type {@link PrismaUserDetails} filled by the {@link CurrentUserHandlerMethodArgumentResolver} using the annotation {@link PrismaRestWSParams}
	 * @param model of type {@link Model}
	 * @return the page of creation
	 */
	public String createServicePage(PrismaUserDetails user, Model model);
	
	
	/**
	 * Retreive the Representation of the service
	 * 
	 * @param workgroup of the user
	 * @param id of the service
	 * @param user of type {@link PrismaUserDetails} filled by the {@link CurrentUserHandlerMethodArgumentResolver} using the annotation {@link PrismaRestWSParams}
	 * @return
	 */
	public PrismaJSONResponse getServiceRepresentation(Long workgroup, Long id,  PrismaUserDetails user);

	
	/**
	 * It shows the dashboard page of the service
	 * 
	 * @param model of type {@link Model}
	 * @param id of the service
	 * @param user of type {@link PrismaUserDetails} filled by the {@link CurrentUserHandlerMethodArgumentResolver} using the annotation {@link PrismaRestWSParams}
	 * @return the dashboard page 
	 */
	public String getServicePageById(Long id, PrismaUserDetails user, Model model);
	
	
	/**
	 * Create a service
	 * 
	 * @param form of type {@link E}
	 * @param bindingResult
	 * @param user of type {@link PrismaUserDetails} filled by the {@link CurrentUserHandlerMethodArgumentResolver} using the annotation {@link PrismaRestWSParams}
	 * @return the serviceRepresentation
	 */
	public PrismaJSONResponse deployService(E form, BindingResult bindingResult, PrismaUserDetails user);

	
	/**
	 * Delete a service
	 * 
	 * @param workgroup of the service
	 * @param id of the service
	 * @param user of type {@link PrismaUserDetails} filled by the {@link CurrentUserHandlerMethodArgumentResolver} using the annotation {@link PrismaRestWSParams}
	 * @return
	 */

	public PrismaJSONResponse undeployService(Long workgroup, Long id, PrismaUserDetails user);

	
}
