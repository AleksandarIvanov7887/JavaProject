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

public class InputFile {

	private Path pathToFile;
	
	public InputFile(Path path) {
		pathToFile = path;
	}
	
	public DataProcessor readData() {
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		
		try (InputStream in = Files.newInputStream(pathToFile);
			    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	String [] words = line.split("[ \t]");
			    	ArrayList<String> listWords = new ArrayList<String>(Arrays.asList(words));
			    	lines.add(listWords);
			    }
			    System.out.println(Messages.CONVERT_SUCCESSFUL);
			} catch (IOException x) {
			    System.err.println(x);
			    System.exit(0);
			}
		
		return new DataProcessor(lines);
	}
	
	public void writeData(DataProcessor holder) {
		try (OutputStream out = Files.newOutputStream(pathToFile);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
				
				for (ArrayList<String> line : holder.getData()) {
					for(int i = 0 ; i < line.size() - 1; i++) {
						writer.write(line.get(i) + " ");
					}
					writer.write(line.get(line.size()-1));
					writer.newLine();
				}
				System.out.println(Messages.WRITE_SUCCESSFUL);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
