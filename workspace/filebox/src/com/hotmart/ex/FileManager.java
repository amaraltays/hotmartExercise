package com.hotmart.ex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;

public class FileManager {
	private final String filesUploadDirMask;
	private final String filesChunkDirMask;
	private FileUploadDao fileUploadDao = new FileUploadDao();

	public FileManager(String workingDir) {
		this.filesUploadDirMask = workingDir + "/uploads/%s";
		this.filesChunkDirMask = workingDir + "/chunks/%s";
	}
	public boolean saveFile(InputStream fileInputStream, String fileName, String userID, Integer chunks, Integer chunk) {
		if (chunks != null && chunk != null) {
			String fileChunkLocation = String.format(filesChunkDirMask, userID, fileName);
			if (this.saveFile(fileInputStream, fileChunkLocation, fileName, true)) {
				return true;
			}
			if (chunk == chunks - 1) {
				String fileLocation = String.format(filesUploadDirMask, userID, fileName);
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
		String fileLocation = String.format(filesUploadDirMask, userID);
		if (this.saveFile(fileInputStream, fileLocation, fileName, false)) {
			FileUpload fileUpload = new FileUpload(userID, UploadStatus.SUCCESS, 0, 0);
			return true;
		}
		return false;
	}
	private boolean saveFile(InputStream fileInputStream, String fileLocation, String fileName, boolean append) {
		try {  
            File fileDir = new File(fileLocation);
            if (!fileDir.exists()) {
            	fileDir.mkdirs();
            }
			FileOutputStream out = new FileOutputStream(new File(fileDir, fileName), append);  
            int read = 0;  
            byte[] bytes = new byte[1024];  
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
