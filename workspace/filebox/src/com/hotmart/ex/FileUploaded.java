package com.hotmart.ex;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fileuploaded")
public class FileUploaded implements Serializable {

	private static final long serialVersionUID = -581431395214319765L;

	private int userID;
	private UploadStatus status;
	private int uploadTime;
	private int chunksCount;
	private String downloadLink;

	public FileUploaded() {}
	
	public FileUploaded(int userID, UploadStatus status, int uploadTime, int chunksCount) {
		this.userID = userID;
		this.status = status;
		this.uploadTime = uploadTime;
		this.chunksCount = chunksCount;
	}
	public int getUserID() {
		return userID;
	}
	@XmlElement
	public void setUserID(int userID) {
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
}
