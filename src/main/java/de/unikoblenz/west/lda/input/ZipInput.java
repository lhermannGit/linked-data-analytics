package de.unikoblenz.west.lda.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.*;


/*
 * This class implements the Input Interface
 * and should be used when the data to stream
 * resides in an Zip-File
 */
public class ZipInput implements Input {

	private String fileLocation;
	
	
	
	public ZipInput(){
		
	}
	
	
	
	public ZipInput(String fileLocation){
		this.fileLocation = fileLocation;
	}
	
	
	
	public String getFileLocation() {
		return fileLocation;
	}





	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}





	public InputStream getInputStream(String fileLocation) throws IOException {
		
		File file = new File(fileLocation);
		
		if(!file.exists())
			throw new FileNotFoundException("File " + fileLocation + " does not exist.");
		
		if(!fileLocation.contains(".gz") || !fileLocation.contains(".gzip"))
			throw new IOException("File " + fileLocation + " ist not in GZip-Format");
		
		InputStream in = new GZIPInputStream(new FileInputStream(file));
			
		return in;
	}

}
