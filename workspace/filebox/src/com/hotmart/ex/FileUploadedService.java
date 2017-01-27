package com.hotmart.ex;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/FileUploadedService")
public class FileUploadedService {

	@Context
	private ServletContext context;
	@Context
	private UriInfo uriInfo;

	@GET
	@Path("/filesUp")
	@Produces(MediaType.APPLICATION_XML)
	public List<FileUpload> listFile() {
		return FileManager.getAllFilesUpload();
	}

	@POST
	@Path("/uploadLargeFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadLargeFile(@FormDataParam("chunks") Integer chunks, @FormDataParam("chunk") Integer chunk,
			@FormDataParam("userId") String userID, @FormDataParam("name") String fileName,
			@FormDataParam("uploadTime") Long uploadTime, @FormDataParam("file") InputStream uploadedInputStream) {
		final String uploadBaseDir = "/uploads";
		String baseUri = uriInfo.getBaseUri().toString();
		baseUri = baseUri.substring(0, baseUri.lastIndexOf('/'));
		String baseDownloadUri = baseUri.replace(baseUri.substring(baseUri.lastIndexOf('/')), uploadBaseDir) + '/';
		FileManager fileManager = new FileManager(context.getRealPath(uploadBaseDir), baseDownloadUri);
		if (fileManager.saveFile(uploadedInputStream, fileName, userID, chunks, chunk, uploadTime)) {
			return Response.status(200).build();
		}
		return Response.status(500).build();
	}
}
