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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/FileUploadedService")
public class FileUploadedService {

	@Context
	private ServletContext context; 

	@GET
	@Path("/filesUp")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FileUpload> listFile() {
		return new FileManager(context.getRealPath("/WEB-INF")).getAllFilesUpload();
	}

	@POST  
    @Path("/uploadFile")  
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadFile(
    		@FormDataParam ("userId") String userID,
            @FormDataParam("file") InputStream inputStream,  
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
		new FileManager(context.getRealPath("/WEB-INF")).saveFile(inputStream, fileDetail.getFileName(), userID);
            String output = "File successfully uploaded to : ";  
            return Response.status(200).entity(output).build();  
     }	
	@POST  
    @Path("/uploadLargeFile")  
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadLargeFile(
    		@FormDataParam ("chunks") Integer chunks,
    		@FormDataParam ("chunk") Integer chunk,
    		@FormDataParam ("userId") String userID,
            @FormDataParam("file") InputStream uploadedInputStream,  
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
		new FileManager(context.getRealPath("/WEB-INF")).saveFile(uploadedInputStream, fileDetail.getFileName(), userID, chunks, chunk);
		String output = "File successfully uploaded!";
        return Response.status(200).entity(output).build();
	}
}
