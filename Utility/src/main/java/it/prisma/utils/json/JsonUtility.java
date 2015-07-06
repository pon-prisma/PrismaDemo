package it.prisma.utils.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
 
 
/**
 * JsonUtility Class that provides facilities for manipulating JSON strings
 * 
 * @author <a href="mailto:g.demusso@reply.it">Giulio Vito de Musso</a>
 * @version 0.1.0
 * @since 0.1.0
 * @see <a
 *      href="http://www.ponsmartcities-prisma.it/">Progetto PRISMA</a>
 */
public class JsonUtility {
	
	/**
	 * Static method that makes deserializing of a JSON string into an object of class <b>outClass</b>
	 * @param jsonString
	 *            JSON string to deserialize
	 * @param outClass
	 *            Class of return. Is instantiated an object of class <b>outClass</b> that are introduced into the encoded data in the JSON string. 
	 * @return Returns a class instance <b>outClass</b> containing encoded data in the JSON string.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */	
	public static <T> Object deserializeJson(String jsonString, Class<T> outClass) throws JsonParseException, JsonMappingException, IOException 
	{
		ObjectMapper om = new ObjectMapper();
		Object result = null;
		
		result = om.readValue(jsonString, outClass);

		return result;
		
	}
	
	/**
	 * Static method that performs the serialization of an object into a JSON string
	 * @param obj
	 *            Object to serialize 
	 * @return Returns a JSON String.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static String serializeJson(Object obj) throws JsonParseException, JsonMappingException, IOException 
	{
		ObjectMapper om = new ObjectMapper();
		String result = "";

		result = om.writeValueAsString(obj);

		return result; 
		
	} 
} 