package de.unikoblenz.west.lda.input;

import java.io.IOException;
import java.io.InputStream;

/*
 *@author Danniene, Jonas 
 */
public interface Input {
	
	public InputStream getInputStream(String fileLocation) throws IOException;

}
