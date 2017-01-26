package com.hotmart.ex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class FileManager {
	private final String filesUploadDirMask;
	private final String filesChunkDirMask;
	private URI appPath;
	private static final Map<String, FileUpload> filesMap = new HashMap<>();

	public FileManager(String workingDir, URI uri) {
		this.filesUploadDirMask = workingDir + "/uploads/%s";
		this.filesChunkDirMask = workingDir + "/chunks/%s";
		this.appPath = uri;
	}
	public boolean saveFile(InputStream fileInputStream, String fileName, String userID, Integer chunks, Integer chunk) {
		FileUpload fileUpload = null;
		File fileSaved = null;
		try {
			if (chunks != null && chunk != null) {
				String fileChunkLocation = String.format(filesChunkDirMask, userID, fileName);
				fileSaved = this.saveFile(fileInputStream, fileChunkLocation, fileName, true);
				if (fileSaved == null) {
					return false;
				}
				fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileSaved),UploadStatus.LOADING, 0, chunks);
				if (chunks.intValue() - 1 == chunk.intValue()) {
					String fileLocation = String.format(filesUploadDirMask, userID, fileName);
					File fileDir = createDirIfNotExists(fileLocation);
					File fileDest = new File(fileDir, fileName);
					if (fileDest.isFile()) {
						FileUtils.forceDelete(fileDest);
					}
					FileUtils.moveFile(new File(fileChunkLocation, fileName), fileDest);
					fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileDest),UploadStatus.SUCCESS, 0, chunks);
				}
			} else {
				fileSaved = this.saveFile(fileInputStream, fileName, userID);
				if (fileSaved != null) {
					fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileSaved),UploadStatus.SUCCESS, 0, chunks);
				}
				fileUpload = new FileUpload(userID, fileName, null, UploadStatus.FAIL, 0, chunks);
				return false;
			} 
		} catch (Exception e) {
			e.printStackTrace();
			fileUpload = new FileUpload(userID, fileName, null, UploadStatus.FAIL, 0, chunks);
			return false;
		} finally {
			if (fileUpload != null) {
				filesMap.put(fileUpload.getKey(), fileUpload);
			}
		}
		return true;
		
	}
	private File saveFile(InputStream fileInputStream, String fileName, String userID) throws Exception {
		String fileLocation = String.format(filesUploadDirMask, userID);
		File fileSaved = this.saveFile(fileInputStream, fileLocation, fileName, false);
		if (fileSaved != null) {
			FileUpload fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileSaved), UploadStatus.SUCCESS, 0, 1);
			filesMap.put(fileUpload.getKey(), fileUpload);
			return fileSaved;
		}
		return null;
	}

	private String getFileLink(File fileSaved) throws Exception {
		String urlStr = "http://abc.dev.domain.com/0007AC/ads/800x480 15sec h.264.mp4";
		URL url = new URL(urlStr);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		url = uri.toURL();
		return null;
	}
	private File saveFile(InputStream fileInputStream, String fileLocation, String fileName, boolean append) {
		try {  
            File fileDir = createDirIfNotExists(fileLocation);
			File file = new File(fileDir, fileName);
			FileOutputStream out = new FileOutputStream(file, append);  
            int read = 0;  
            byte[] bytes = new byte[1024];  
            while ((read = fileInputStream.read(bytes)) != -1) {  
                out.write(bytes, 0, read);  
            }  
            out.flush();  
            out.close();
            return file;
        } catch (IOException e) {
        	e.printStackTrace();
        }
		return null;
	}
	private File createDirIfNotExists(String fileLocation) {
		File fileDir = new File(fileLocation);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		return fileDir;
	}

	public static List<FileUpload> getAllFilesUpload() {
		ArrayList<FileUpload> allFiles = new ArrayList<FileUpload>();
		allFiles.addAll(filesMap.values());
		return allFiles;
	}
}
