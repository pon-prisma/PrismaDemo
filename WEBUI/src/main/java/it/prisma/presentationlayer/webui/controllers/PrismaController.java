package it.prisma.presentationlayer.webui.controllers;

import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaErrorCode;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.exceptions.PrismaError;
import it.prisma.presentationlayer.webui.datavalidation.forms.IDeployForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * The <code>PrismaController</code> abstract class
 * <br/><br/>
 * <b>Use annotations in the concrete class methods</b>
 * 
 * <br/><br/>Example:
 * <pre>
 *   &#64;Controller
 *   {@code public class VMController extends PrismaController<VMRepresentation, VMForm>}
 * </pre>
 * @author Reply
 *
 * @param <T> the serviceRepresentation type of the service to be created
 * @param <E> the form type used to create the service
 */
public abstract class PrismaController<T, E extends IDeployForm> implements IPrismaController<T, E> {

	protected Logger LOG = LogManager.getLogger();
	
	@Override
	public abstract String listServicePage(PrismaUserDetails user, Model model);

	@Override
	@ResponseBody
	public abstract BootstrapTable<T> getAllServicesTableRepresentation(@PrismaRestWSParams RestWSParamsContainer params, PrismaUserDetails user, HttpServletResponse response);

	@Override
	public abstract String createServicePage(PrismaUserDetails user, Model model);

	@Override
	@ResponseBody
	public abstract PrismaJSONResponse getServiceRepresentation(Long workgroup,Long id, PrismaUserDetails user);

	@Override
	public abstract String getServicePageById(Long id, PrismaUserDetails user, Model model);

	@Override
	@ResponseBody
	public abstract PrismaJSONResponse deployService(@Valid E form, BindingResult bindingResult, PrismaUserDetails user);

	@Override
	@ResponseBody
	public abstract PrismaJSONResponse undeployService(Long workgroup, Long id, PrismaUserDetails user);
	
	protected PrismaJSONResponse getFormErrors(BindingResult bindingResult){
		
		PrismaJSONResponse response = new PrismaJSONResponse(true, true);
		List<PrismaError> errors = new ArrayList<PrismaError>();
		response.setSuccess(false);
		for (Object object : bindingResult.getAllErrors()) {
			if (object instanceof FieldError) {
				FieldError fieldError = (FieldError) object;
				errors.add(new PrismaError(PrismaErrorCode.FIELD_ERROR, fieldError.getCode(), fieldError.getField()));					
			}
		}
		LOG.debug("Deploy form has some error");
		LOG.debug(Arrays.toString(errors.toArray()));
		response.setResult(errors);
		response.setError(new PrismaError(PrismaErrorCode.BAD_REQUEST, "Errore nei parametri \n" + Arrays.toString(errors.toArray())));
		return response;
	}
}
