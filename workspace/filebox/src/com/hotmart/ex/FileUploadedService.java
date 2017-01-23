package com.hotmart.ex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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

	FileUploadedDao fileUpDAO = new FileUploadedDao();
	 private static final String SUCCESS_RESULT="<result>success</result>";
	 private static final String FAILURE_RESULT="<result>failure</result>";

	@GET
	@Path("/filesUp")
	@Produces(MediaType.APPLICATION_XML)
	public List<FileUploaded> listFile() {
		return fileUpDAO.getAll();
	}

	@POST  
    @Path("/uploadFile")  
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadFile(  
            @FormDataParam("file") InputStream uploadedInputStream,  
            @FormDataParam("file") FormDataContentDisposition fileDetail) {  
            String fileLocation = "c://files//" + fileDetail.getFileName();  
                    //saving file  
            try {  
                FileOutputStream out = new FileOutputStream(new File(fileLocation));  
                int read = 0;  
                byte[] bytes = new byte[1024];  
                out = new FileOutputStream(new File(fileLocation));  
                while ((read = uploadedInputStream.read(bytes)) != -1) {  
                    out.write(bytes, 0, read);  
                }  
                out.flush();  
                out.close();  
            } catch (IOException e) {e.printStackTrace();}  
            String output = "File successfully uploaded to : " + fileLocation;  
            return Response.status(200).entity(output).build();  
        }	
	/*@POST
	@Path("/uploadFile")
	@Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormParam ("userId") int userID, @FormParam("file") InputStream fileInputStream) {
		FileUploaded fileUp = new FileUploaded(userID, UploadStatus.SUCCESS, 0, 0, fileInputStream);
		if (fileUpDAO.save(fileUp)) {
			return SUCCESS_RESULT;
		}
		return FAILURE_RESULT;
	}*/
}
