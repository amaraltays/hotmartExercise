package com.hotmart.ex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

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
    @Path("/uploadLargeFile")  
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadLargeFile(
    		@FormDataParam ("chunks") Integer chunks,
    		@FormDataParam ("chunk") Integer chunk,
    		@FormDataParam ("userId") String userID,
    		@FormDataParam ("name") String fileName,
            @FormDataParam("file") InputStream uploadedInputStream) {
		new FileManager(context.getRealPath("/uploads")).saveFile(uploadedInputStream, fileName, userID, chunks, chunk);
		return Response.status(200).build();
	}
	@GET
    @Path("/downloadFile")
    public Response downloadPdfFile(@FormDataParam ("filePath") String filePath) {
        StreamingOutput fileStream =  new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
                    byte[] data = Files.readAllBytes(Paths.get(filePath));
                    output.write(data);
                    output.flush();
                } catch (Exception e) {
                    throw new WebApplicationException("File Not Found !!");
                }
			}
		};
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = myfile.pdf")
                .build();
    }
}
