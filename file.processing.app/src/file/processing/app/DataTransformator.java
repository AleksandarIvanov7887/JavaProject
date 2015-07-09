package file.processing.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class DataTransformator {

	private Path pathToFile;
	
	public DataTransformator(Path path) {
		pathToFile = path;
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
			    while ((line = reader.readLine()) != null) {
			    	String [] words = line.split("[ \t]");
			    	ArrayList<String> listWords = new ArrayList<String>(Arrays.asList(words));
			    	mapLines.add(listWords);
			    }
			} catch (IOException x) {
			    System.err.println(x);
			}
		System.out.println("Converting file data successful.");
		return mapLines;
	}
	
	public void writeData(DataHolder holder) {
		try (OutputStream out = Files.newOutputStream(pathToFile);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
				
				for (ArrayList<String> line : holder.getData()) {
					for(int i = 0 ; i < line.size() - 1; i++) {
						writer.write(line.get(i) + " ");
					}
					writer.write(line.get(line.size()-1));
					writer.newLine();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Writing to file successful");
	}
}
