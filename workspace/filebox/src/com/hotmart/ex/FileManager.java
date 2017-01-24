package com.hotmart.ex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileManager {
	private static final String FILES_UPLOAD_DIR = File.pathSeparator + "uploads" + File.pathSeparator + "%s" + File.pathSeparator + "%s";
	private static final String FILES_CHUNK_UPLOAD_DIR = File.pathSeparator + "chunks" + File.pathSeparator + "%s" + File.pathSeparator + "%s";
	private FileUploadDao fileUploadDao = new FileUploadDao();

	public boolean saveFile(InputStream fileInputStream, String fileName, String userID, Integer chunks, Integer chunk) {
		if (chunks != null && chunk != null) {
			String fileChunkLocation = String.format(FILES_CHUNK_UPLOAD_DIR, userID, fileName);
			if (this.saveFile(fileInputStream, fileChunkLocation, true)) {
				return true;
			}
			if (chunk == chunks - 1) {
				String fileLocation = String.format(FILES_UPLOAD_DIR, userID, fileName);
				try {
					FileUtils.moveFile(new File(fileChunkLocation), new File(fileLocation));
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
				FileUpload fileUpload = new FileUpload(userID, UploadStatus.SUCCESS, 0, 0);
			}
		} else {
			this.saveFile(fileInputStream, fileName, userID);
		}
		return false;
		
	}
	public boolean saveFile(InputStream fileInputStream, String fileName, String userID) {
		String fileLocation = String.format(FILES_UPLOAD_DIR, userID, fileName);
		if (this.saveFile(fileInputStream, fileLocation, false)) {
			FileUpload fileUpload = new FileUpload(userID, UploadStatus.SUCCESS, 0, 0);
			return true;
		}
		return false;
	}
	private boolean saveFile(InputStream fileInputStream, String fileLocation, boolean append) {
		try {  
            FileOutputStream out = new FileOutputStream(new File(fileLocation), append);  
            int read = 0;  
            byte[] bytes = new byte[1024];  
            out = new FileOutputStream(new File(fileLocation));  
            while ((read = fileInputStream.read(bytes)) != -1) {  
                out.write(bytes, 0, read);  
            }  
            out.flush();  
            out.close();
            return true;
        } catch (IOException e) {
        	e.printStackTrace();
        }
		return false;
	}

	public List<FileUpload> getAllFilesUpload() {
		return fileUploadDao.getAll();
	}
}
