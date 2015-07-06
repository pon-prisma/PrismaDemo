package it.prisma.presentationlayer.webui.controllers.accounting;

import it.prisma.domain.dsl.accounting.AuthTokenRepresentation;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;
import it.prisma.presentationlayer.webui.core.PrismaJSONResponse;
import it.prisma.presentationlayer.webui.core.accounting.AuthTokenManagementService;
import it.prisma.presentationlayer.webui.core.accounting.UserManagementService;
import it.prisma.presentationlayer.webui.core.exceptions.ExceptionHelper;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;
import it.prisma.presentationlayer.webui.stereotypes.PrismaRestWSParams;
import it.prisma.presentationlayer.webui.stereotypes.accounting.CurrentUser;
import it.prisma.presentationlayer.webui.vos.paas.appaas.BootstrapTable;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {

	static final Logger LOG = LogManager.getLogger(TokenController.class
			.getName());

	@Autowired
	private AuthTokenManagementService tokenService;
	@Autowired
	private UserManagementService userService;

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public String getTokenPage(Model model) {	
		return "pages/token/token";
	}

	@RequestMapping(value = "/async/token", method = RequestMethod.GET)
	public @ResponseBody BootstrapTable<AuthTokenRepresentation> getAllToken (@CurrentUser PrismaUserDetails user, @PrismaRestWSParams RestWSParamsContainer params, HttpServletResponse response) {
		try {
			Long userId=user.getUserData().getUserAccountId();
			return tokenService.getAuthTokenForUser(userId, params);
		} catch (Exception e) {
			response.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value = "/async/token", method = RequestMethod.PUT)
	public @ResponseBody PrismaJSONResponse createToken(@CurrentUser PrismaUserDetails user) {

		try {
			String userId=String.valueOf(user.getUserData().getUserAccountId());
			AuthTokenRepresentation token= tokenService.createToken(userId);
			return new PrismaJSONResponse(true, true, token, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, TokenController.class);
		}
	}
	
	@RequestMapping(value = "/async/token/{token}", method = RequestMethod.DELETE)
	public @ResponseBody PrismaJSONResponse revokeToken(@CurrentUser PrismaUserDetails user, @PathVariable String token) {
		try {
			tokenService.revokeAuthTokenForUser(token);
			return new PrismaJSONResponse(true, true, null, null);
		} catch (Exception e) {
			return ExceptionHelper.getPrismaJSONResponse(e, TokenController.class);
		}
	}
	
}
