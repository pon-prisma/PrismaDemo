package it.prisma.presentationlayer.webui.controllers.commons;

import it.prisma.presentationlayer.webui.core.PrismaErrorCode;
import it.prisma.presentationlayer.webui.core.exceptions.PrismaError;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
public class ErrorsController {

	static final Logger LOG = LogManager.getLogger(ErrorsController.class
			.getName());

	@RequestMapping(value = "/400")
	public String error400() {
		return "pages/error/400";
	}
	
	@RequestMapping(value = "/404")
	public String error404(HttpServletRequest request) {
		return "pages/error/404";
	}

	@RequestMapping(value = "/500")
	public String error500(HttpServletRequest request) {
		return "pages/error/500";
	}

	@RequestMapping(value = "/prisma/error")
	public String errorGeneric(Model model, NativeWebRequest webRequest, HttpServletRequest request) {
		
		String	code = webRequest.getParameter("code");
		String message = webRequest.getParameter("message");

		if(code == null){
			code = PrismaErrorCode.GENERIC.toString();
		}
		
		if(message == null){
			message = "";
		}
		
		model.addAttribute("error", new PrismaError(PrismaErrorCode.valueOf(code), message));

		return "pages/error/prisma-error";
	}

}
