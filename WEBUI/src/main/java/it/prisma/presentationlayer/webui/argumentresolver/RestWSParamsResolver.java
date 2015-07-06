package it.prisma.presentationlayer.webui.argumentresolver;

import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSPaginationParams;
import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class RestWSParamsResolver implements HandlerMethodArgumentResolver {
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(RestWSParamsContainer.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {

		//TODO add search
		// String search = webRequest.getParameter("search");
		// String sortString = webRequest.getParameter("sort");
		// String orderString = webRequest.getParameter("order");

		String offset = webRequest
				.getParameter(RestWSPaginationParams.QUERY_PARAM_OFFSET);
		String limit = webRequest
				.getParameter(RestWSPaginationParams.QUERY_PARAM_LIMIT);

		RestWSPaginationParams pagination = new RestWSPaginationParams(
				(limit != null ? new Long(limit) : null),
				(offset != null ? new Long(offset) : null));

		return new RestWSParamsContainer(null, pagination, null);
	}
}