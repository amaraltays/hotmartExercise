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
	private static final String UPLOAD_DIR = "uploads";
	private static final String CHUNK_DIR = "chunks";
	private String baseDownloadUri;
	private String workingDir;
	private static final Map<String, FileUpload> filesMap = new HashMap<>();

	public FileManager(String workingDir, String baseDownloadUri) {
		this.workingDir = workingDir;
		this.baseDownloadUri = baseDownloadUri;
	}

	public boolean saveFile(InputStream fileInputStream, String fileName, String userID, Integer chunks, Integer chunk,
			Long uploadTime) {
		FileUpload fileUpload = null;
		File fileSaved = null;
		try {
			if (chunks != null && chunk != null) {
				String fileChunkLocation = getFilePath(userID, CHUNK_DIR);
				fileSaved = this.saveFile(fileInputStream, fileChunkLocation, fileName, true);
				if (fileSaved == null) {
					return false;
				}
				fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileSaved), UploadStatus.LOADING,
						uploadTime, chunks);
				if (chunks.intValue() - 1 == chunk.intValue()) {
					String fileLocation = getFilePath(userID, UPLOAD_DIR);
					File fileDir = createDirIfNotExists(fileLocation);
					File fileDest = new File(fileDir, fileName);
					if (fileDest.isFile()) {
						FileUtils.forceDelete(fileDest);
					}
					FileUtils.moveFile(new File(fileChunkLocation, fileName), fileDest);
					fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileDest), UploadStatus.SUCCESS,
							uploadTime, chunks);
				}
			} else {
				fileSaved = this.saveFile(fileInputStream, fileName, userID, uploadTime);
				if (fileSaved != null) {
					fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileSaved), UploadStatus.SUCCESS,
							uploadTime, chunks);
				}
				fileUpload = new FileUpload(userID, fileName, null, UploadStatus.FAIL, uploadTime, chunks);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			fileUpload = new FileUpload(userID, fileName, null, UploadStatus.FAIL, uploadTime, chunks);
			return false;
		} finally {
			if (fileUpload != null) {
				filesMap.put(fileUpload.getKey(), fileUpload);
			}
		}
		return true;

	}

	public static List<FileUpload> getAllFilesUpload() {
		ArrayList<FileUpload> allFiles = new ArrayList<FileUpload>();
		allFiles.addAll(filesMap.values());
		return allFiles;
	}

	private String getFilePath(String userID, String downloadDir) {
		return workingDir + File.separatorChar + downloadDir + File.separatorChar + userID;
	}

	private File saveFile(InputStream fileInputStream, String fileName, String userID, long uploadTime)
			throws Exception {
		String fileLocation = getFilePath(userID, UPLOAD_DIR);
		File fileSaved = this.saveFile(fileInputStream, fileLocation, fileName, false);
		if (fileSaved != null) {
			FileUpload fileUpload = new FileUpload(userID, fileName, this.getFileLink(fileSaved), UploadStatus.SUCCESS,
					uploadTime, 1);
			filesMap.put(fileUpload.getKey(), fileUpload);
			return fileSaved;
		}
		return null;
	}

	private String getFileLink(File fileSaved) throws Exception {
		URL url = new URL(this.baseDownloadUri + UPLOAD_DIR + "/" + fileSaved.getName());
		URI uri = new URI(url.getProtocol(), url.getAuthority(), url.getPath(), url.getQuery(), null);
		return uri.toASCIIString();
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
}
