package file.processing.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileProcessingApplication {

	public static void main(String[] args) {
		Path file = Paths.get("/Users/aleksandarivanov/Documents/workspace/file.processing.app/example");
		
		try (InputStream in = Files.newInputStream(file);
			    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	String [] words = line.split("[ \t]");
			    	ArrayList<String> listWords = new ArrayList<String>(Arrays.asList(words));
			    	System.out.println(listWords);
			    }
			} catch (IOException x) {
			    System.err.println(x);
			}
	}
}
