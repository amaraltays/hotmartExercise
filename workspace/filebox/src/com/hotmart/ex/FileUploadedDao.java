package com.hotmart.ex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUploadedDao {
	private static final String STORAGE_FILE_NAME = "FilesUploaded.dat";
	public List<FileUploaded> getAll () {
		List<FileUploaded> fileList = null;
	      try {
	         File file = new File(STORAGE_FILE_NAME);
	         if (!file.exists()) {
	            fileList = new ArrayList<FileUploaded>();
	         }
	         else{
	            FileInputStream fis = new FileInputStream(file);
	            ObjectInputStream ois = new ObjectInputStream(fis);
	            fileList = (List<FileUploaded>) ois.readObject();
	            ois.close();
	         }
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (ClassNotFoundException e) {
	         e.printStackTrace();
	      }		
	      return fileList;
	}

	public boolean save(FileUploaded fileUp){
	      try {
	         File file = new File(STORAGE_FILE_NAME);
	         FileOutputStream fos = new FileOutputStream(file);
	         ObjectOutputStream oos = new ObjectOutputStream(fos);
	         oos.writeObject(fileUp);
	         oos.close();
	         return true;
	      } catch (FileNotFoundException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	      return false;
	   }   
}
