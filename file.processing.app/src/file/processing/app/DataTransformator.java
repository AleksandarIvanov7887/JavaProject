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

public class DataTransformator {

	private Path pathToFile;
	
	public DataTransformator(String Path) {
		pathToFile = Paths.get("/Users/aleksandarivanov/Documents/workspace/file.processing.app/example");
	}
	
	public DataHolder getDataHolder() {
		ArrayList<ArrayList<String>> mapLines = transformData();
		
		DataHolder dataHolder = new DataHolder(mapLines);
		return dataHolder;
	}
	
	private ArrayList<ArrayList<String>> transformData() {
		ArrayList<ArrayList<String>> mapLines = new ArrayList<ArrayList<String>>();
		
		try (InputStream in = Files.newInputStream(pathToFile);
			    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			    String line = null;
			    int lineNumber = 0;
			    while ((line = reader.readLine()) != null) {
			    	String [] words = line.split("[ \t]");
			    	ArrayList<String> listWords = new ArrayList<String>(Arrays.asList(words));
			    	mapLines.set(lineNumber, listWords);
			    	lineNumber++;
			    }
			} catch (IOException x) {
			    System.err.println(x);
			}
		
		return mapLines;
	}
}
