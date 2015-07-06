package it.prisma.presentationlayer.webui.uploader;

/**
 * 
 * https://github.com/blueimp/jQuery-File-Upload/wiki/Setup
 * 
 * This POJO represents the response after an upload
 * 
 * 
 * @author Matteo Bassi
 *
 */
public class Response {

	private long id;
	private String name;
	private long size;
	private String url;
	private String thumbnailUrl;
	private String deleteUrl;
	private String deleteType;
	private String error;

	public Response(Long id, String name, long size, String url, String thumb,
			String deleteURL) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.url = url;
		this.thumbnailUrl = thumb;
		this.deleteUrl = deleteURL;
		this.deleteType = "DELETE";
		this.error = "No Error";
	}

	public Response(String name, long size, String error) {
		this.name = name;
		this.size = size;
		this.url = "url";
		this.thumbnailUrl = "thumburl";
		this.deleteUrl = "deleteurl";
		this.deleteType = "deletetype";
		this.error = error;
	}

	public Response(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Response(Long id, String name, String error) {
		this.id = id;
		this.name = name;
		this.error = error;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "Response [name=" + name + ", size=" + size + ", url=" + url
				+ ", thumbnailUrl=" + thumbnailUrl + ", deleteUrl=" + deleteUrl
				+ ", deleteType=" + deleteType + ", error=" + error + "]";
	}

}
