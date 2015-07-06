package it.prisma.businesslayer.bizws.paas.services.appaas.apprepo;

import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.utils.json.JsonUtility;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AppSrcFileMultipartForm {

	private byte[] filedata;
	private String requestJSON;

	public AppSrcFileMultipartForm() {
	}

	public byte[] getFileData() {
		return filedata;
	}

	
	@FormParam("file")
	@PartType(MediaType.APPLICATION_OCTET_STREAM)
	public void setFileData(final byte[] filedata) {
		this.filedata = filedata;
	}
	
	public String getSetRequestJSON() {
		return requestJSON;
	}

	@FormParam("requestJSON")
	@PartType(MediaType.APPLICATION_JSON)
	public void setRequestJSON(final String requestJSON) {
		this.requestJSON = requestJSON;
	}
	
	public AddAppSrcFileRequest getAddAppSrcFileRequest() throws JsonParseException, JsonMappingException, IOException {
		return (AddAppSrcFileRequest) JsonUtility.deserializeJson(requestJSON,AddAppSrcFileRequest.class);
	}
}