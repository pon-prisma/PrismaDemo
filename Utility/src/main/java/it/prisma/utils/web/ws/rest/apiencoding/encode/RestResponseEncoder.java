package it.prisma.utils.web.ws.rest.apiencoding.encode;

public interface RestResponseEncoder {

	@SuppressWarnings("rawtypes")
	public String encode(Object model, Class modelClass);
	
}
