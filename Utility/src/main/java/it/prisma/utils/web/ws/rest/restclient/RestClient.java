package it.prisma.utils.web.ws.rest.restclient;

import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;
import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecoder;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * A general RestClient to make Rest requests. <br/>
 * This client is meant to decode automatically the JSON response body using the
 * {@link RestResponseDecoder} system.
 * 
 * @author l.biava
 * 
 * @param <T>
 */
public interface RestClient<RestResponseResultType extends BaseRestResponseResult> {

	public enum RestMethod {
		GET, POST, PUT, DELETE, HEAD;
	}
	
	public class ProxyOptions {
		private String hostname;
		private int port;
		private String protocol;

		public ProxyOptions(String hostname, int port, String protocol) {
			super();
			this.hostname = hostname;
			this.port = port;
			this.protocol = protocol;
		}

		public String getHostname() {
			return hostname;
		}

		public void setHostname(String hostname) {
			this.hostname = hostname;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
	}

	public class RequestOptions {
		private int timeout;
		private ProxyOptions proxy;

		// TODO Other useful fields like HTTPS
		// Authentication/Keystore/Truststore etc
		public int getTimeout() {
			return timeout;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

		public ProxyOptions getProxy() {
			return proxy;
		}

		public void setProxy(ProxyOptions proxy) {
			this.proxy = proxy;
		}
	}

	/**
	 * 
	 * @return the default request timeout in ms.
	 */
	public int getDefaultTimeout();

	/**
	 * 
	 * @param defaultTimeout
	 *            the default request timeout in ms.
	 */
	public void setDefaultTimeout(int defaultTimeout);

	/***************************** GET Requests ******************************/

	/**
	 * Executes a Restful GET Request to the given URL, with the given headers
	 * and body (with the given media type) and <b>tries to decode the JSON
	 * result with the given {@link RestResponseDecoder} and/or
	 * {@link RestResponseDecodeStrategy}</b>.
	 * 
	 * @param URL
	 * @param headers
	 * @param rrd
	 *            A Rest Result Decoder.
	 * @param strategy
	 *            Can be null if the decoder's default strategy has to be used.
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return The Rest result decoded (of the type passed to the class).
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 * @throws NoMappingModelFoundException
	 *             {@link NoMappingModelFoundException}
	 * @throws ServerErrorResponseException
	 *             if the server responded with a custom error not using the
	 *             application protocol (ie. 404)
	 */
	public RestResponseResultType getRequest(String URL,
			MultivaluedMap<String, Object> headers, RequestOptions reqOptions,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * See
	 * {@link RestClient#getRequest(String, MultivaluedMap, RequestOptions, RestResponseDecoder, RestResponseDecodeStrategy )}
	 * . Without RequestOptions param.
	 */
	public RestResponseResultType getRequest(String URL,
			MultivaluedMap<String, Object> headers,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * Executes a Restful GET Request to the given URL, with the given headers
	 * and body (with the given media type) and returns the response in a
	 * {@link RestMessage}.
	 * 
	 * @param URL
	 * @param headers
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return the response in a {@link RestMessage}.
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 */
	public RestMessage getRequest(String URL,
			MultivaluedMap<String, Object> headers, RequestOptions reqOptions)
			throws RestClientException;

	/**
	 * See {@link RestClient#getRequest(String, MultivaluedMap, RequestOptions)}
	 * . Without RequestOptions param.
	 */
	public RestMessage getRequest(String URL,
			MultivaluedMap<String, Object> headers) throws RestClientException;

	/***************************** HEAD Requests ******************************/

	/**
	 * Executes a Restful HEAD Request to the given URL, with the given headers
	 * and body (with the given media type) and <b>tries to decode the JSON
	 * result with the given {@link RestResponseDecoder} and/or
	 * {@link RestResponseDecodeStrategy}</b>.
	 * 
	 * @param URL
	 * @param headers
	 * @param rrd
	 *            A Rest Result Decoder.
	 * @param strategy
	 *            Can be null if the decoder's default strategy has to be used.
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return The Rest result decoded (of the type passed to the class).
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 * @throws NoMappingModelFoundException
	 *             {@link NoMappingModelFoundException}
	 * @throws ServerErrorResponseException
	 *             if the server responded with a custom error not using the
	 *             application protocol (ie. 404)
	 */
	public RestResponseResultType headRequest(String URL,
			MultivaluedMap<String, Object> headers, RequestOptions reqOptions,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * See
	 * {@link RestClient#headRequest(String, MultivaluedMap, RequestOptions, RestResponseDecoder, RestResponseDecodeStrategy)}
	 * . Without RequestOptions param.
	 */
	public RestResponseResultType headRequest(String URL,
			MultivaluedMap<String, Object> headers,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * Executes a Restful HEAD Request to the given URL, with the given headers
	 * and body (with the given media type) and returns the response in a
	 * {@link RestMessage}.
	 * 
	 * @param URL
	 * @param headers
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return the response in a {@link RestMessage}.
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 */
	public RestMessage headRequest(String URL,
			MultivaluedMap<String, Object> headers, RequestOptions reqOptions)
			throws RestClientException;

	/**
	 * See
	 * {@link RestClient#headRequest(String, MultivaluedMap, RequestOptions)}.
	 * Without RequestOptions param.
	 */
	public RestMessage headRequest(String URL,
			MultivaluedMap<String, Object> headers) throws RestClientException;

	/***************************** POST Requests ******************************/

	/**
	 * Executes a Restful POST Request to the given URL, with the given headers
	 * and body (with the given media type) and <b>tries to decode the JSON
	 * result with the given {@link RestResponseDecoder} and/or
	 * {@link RestResponseDecodeStrategy}</b>.
	 * 
	 * @param URL
	 * @param headers
	 * @param body
	 *            An entity representing the body of the request ({@see
	 *            RestClientHelper}).
	 * @param bodyMediaType
	 *            The media type of the body entity.
	 * @param rrd
	 *            A Rest Result Decoder.
	 * @param strategy
	 *            Can be null if the decoder's default strategy has to be used.
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return The Rest result decoded (of the type passed to the class).
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 * @throws NoMappingModelFoundException
	 *             {@link NoMappingModelFoundException}
	 * @throws ServerErrorResponseException
	 *             if the server responded with a custom error not using the
	 *             application protocolo (ie. 404)
	 */
	public <E> RestResponseResultType postRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType, RequestOptions reqOptions,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * See
	 * {@link RestClient#postRequest(String, MultivaluedMap, GenericEntity, MediaType, RequestOptions, RestResponseDecoder, RestResponseDecodeStrategy)}
	 * Without RequestOptions param.
	 */
	public <E> RestResponseResultType postRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * Executes a Restful POST Request to the given URL, with the given headers
	 * and body (with the given media type) and returns the response in a
	 * {@link RestMessage}.
	 * 
	 * @param URL
	 * @param headers
	 * @param body
	 *            An entity representing the body of the request ({@see
	 *            RestClientHelper}).
	 * @param bodyMediaType
	 *            The media type of the body entity.
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return the response in a {@link RestMessage}.
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 */
	public <E> RestMessage postRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType, RequestOptions reqOptions)
			throws RestClientException;

	/**
	 * See
	 * {@link RestClient#postRequest(String, MultivaluedMap, GenericEntity, MediaType, RequestOptions)}
	 * . Without RequestOptions param.
	 */
	public <E> RestMessage postRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType) throws RestClientException;

	/***************************** PUT Requests ******************************/

	/**
	 * Executes a Restful PUT Request to the given URL, with the given headers
	 * and body (with the given media type) and <b>tries to decode the JSON
	 * result with the given {@link RestResponseDecoder} and/or
	 * {@link RestResponseDecodeStrategy}</b>.
	 * 
	 * @param URL
	 * @param headers
	 * @param body
	 *            An entity representing the body of the request ({@see
	 *            RestClientHelper}).
	 * @param bodyMediaType
	 *            The media type of the body entity.
	 * @param rrd
	 *            A Rest Result Decoder.
	 * @param strategy
	 *            Can be null if the decoder's default strategy has to be used.
	 * @return The Rest result decoded (of the type passed to the class).
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 * @throws NoMappingModelFoundException
	 *             {@link NoMappingModelFoundException}
	 * @throws ServerErrorResponseException
	 *             if the server responded with a custom error not using the
	 *             application protocol (ie. 404)
	 */
	public <E> RestResponseResultType putRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType, RequestOptions reqOptions,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * See
	 * {@link RestClient#putRequest(String, MultivaluedMap, GenericEntity, MediaType, RequestOptions, RestResponseDecoder, RestResponseDecodeStrategy)}
	 * . Without RequestOptions param.
	 */
	public <E> RestResponseResultType putRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * Executes a Restful PUT Request to the given URL, with the given headers
	 * and body (with the given media type) and returns the response in a
	 * {@link RestMessage}.
	 * 
	 * @param URL
	 * @param headers
	 * @param body
	 *            An entity representing the body of the request ({@see
	 *            RestClientHelper}).
	 * @param bodyMediaType
	 *            The media type of the body entity.
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return the response in a {@link RestMessage}.
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 */
	public <E> RestMessage putRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType, RequestOptions reqOptions)
			throws RestClientException;

	/**
	 * See
	 * {@link RestClient#putRequest(String, MultivaluedMap, GenericEntity, MediaType, RequestOptions)}
	 * . Without RequestOptions param.
	 */
	public <E> RestMessage putRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType) throws RestClientException;

	/***************************** DELETE Requests ******************************/

	/**
	 * Executes a Restful DELETE Request to the given URL, with the given
	 * headers and body (with the given media type) and <b>tries to decode the
	 * JSON result with the given {@link RestResponseDecoder} and/or
	 * {@link RestResponseDecodeStrategy}</b>.
	 * 
	 * @param URL
	 * @param headers
	 * @param rrd
	 *            A Rest Result Decoder.
	 * @param strategy
	 *            Can be null if the decoder's default strategy has to be used.
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return The Rest result decoded (of the type passed to the class).
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 * @throws NoMappingModelFoundException
	 *             {@link NoMappingModelFoundException}
	 * @throws ServerErrorResponseException
	 *             if the server responded with a custom error not using the
	 *             application protocol (ie. 404)
	 */
	public <E> RestResponseResultType deleteRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType, RequestOptions reqOptions,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * See
	 * {@link RestClient#deleteRequest(String, MultivaluedMap, RequestOptions, RestResponseDecoder, RestResponseDecodeStrategy)}
	 * . Without RequestOptions param.
	 */
	public <E> RestResponseResultType deleteRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;

	/**
	 * Executes a Restful DELETE Request to the given URL, with the given
	 * headers and body (with the given media type) and returns the response in
	 * a {@link RestMessage}.
	 * 
	 * @param URL
	 * @param headers
	 * @param reqOptions
	 *            Request options like timeout, proxy, etc.
	 * @return the response in a {@link RestMessage}.
	 * @throws RestClientException
	 *             if a problem with the request has occurred (not an error of
	 *             the remote application, just for the HTTP request).
	 */
	public <E> RestMessage deleteRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType, RequestOptions reqOptions)
			throws RestClientException;

	/**
	 * See
	 * {@link RestClient#deleteRequest(String, MultivaluedMap, RequestOptions)}.
	 * Without RequestOptions param.
	 */
	public <E> RestMessage deleteRequest(String URL,
			MultivaluedMap<String, Object> headers, GenericEntity<E> body,
			MediaType bodyMediaType) throws RestClientException;
	
	/*	GENERIC REQUESTS */
	
	/**
	 * 	
	 */
	public <E> RestResponseResultType doRequest(RestMethod method, String URL,
			MultivaluedMap<String, Object> headers, MultivaluedMap<String, Object> queryParams, 
			GenericEntity<E> body,
			MediaType bodyMediaType,
			RequestOptions reqOptions,
			RestResponseDecoder<RestResponseResultType> rrd,
			RestResponseDecodeStrategy strategy) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;
	
	public <E> RestMessage doRequest(RestMethod method, String URL,
			MultivaluedMap<String, Object> headers, MultivaluedMap<String, Object> queryParams, 
			GenericEntity<E> body,
			MediaType bodyMediaType,
			RequestOptions reqOptions) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;
	
	public <E> RestMessage doRequest(RestMethod method, String URL,
			MultivaluedMap<String, Object> headers, MultivaluedMap<String, Object> queryParams, 
			GenericEntity<E> body,
			MediaType bodyMediaType,
			RequestOptions reqOptions, Class<?> entityClass) throws RestClientException,
			NoMappingModelFoundException, MappingException,
			ServerErrorResponseException;
}
