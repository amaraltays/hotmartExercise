package com.hotmart.ex;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlRootElement(name = "fileuploaded")
public class FileUpload implements Serializable {

	/** UID */
	private static final long serialVersionUID = 2040090388674770342L;
	private String userID;
	private String fileName;
	private UploadStatus status;
	private int uploadTime;
	private int chunksCount;
	private String downloadLink;

	public FileUpload() {}
	
	public FileUpload(String userID, String fileName, String downloadLink, UploadStatus status, int uploadTime, int chunksCount) {
		this.userID = userID;
		this.status = status;
		this.uploadTime = uploadTime;
		this.chunksCount = chunksCount;
		this.fileName = fileName;
		this.downloadLink = downloadLink;
	}
	public String getFileName() {
		return fileName;
	}
	@XmlElement
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUserID() {
		return userID;
	}
	@XmlElement
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public UploadStatus getStatus() {
		return status;
	}
	@XmlElement
	public void setStatus(UploadStatus status) {
		this.status = status;
	}
	public int getUploadTime() {
		return uploadTime;
	}
	@XmlElement
	public void setUploadTime(int uploadTime) {
		this.uploadTime = uploadTime;
	}
	public int getChunksCount() {
		return chunksCount;
	}
	@XmlElement
	public void setChunksCount(int chunksCount) {
		this.chunksCount = chunksCount;
	}
	public String getDownloadLink() {
		return downloadLink;
	}
	@XmlElement
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
	public String getKey() {
		String thisKey = this.userID + this.fileName;
		return thisKey;
	}
	@Override
	public boolean equals(Object otherFile) {
		return this.getKey().equals(((FileUpload)otherFile).getKey());
	}

	@Override
	public int hashCode() {
		return this.getKey().hashCode();
	}
}
