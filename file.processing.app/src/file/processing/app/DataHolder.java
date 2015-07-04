package file.processing.app;

import java.util.ArrayList;
import java.util.Collections;

public class DataHolder {
	
	private ArrayList<ArrayList<String>> mapLines;
	private DataValidator validator;
	
	public DataHolder(ArrayList<ArrayList<String>> mapLines) {
		this.mapLines = mapLines;
		validator = new DataValidator();
	}
	
	public void validateData() {
		for (ArrayList<String> line : mapLines) {
			if (line.contains("")) {
				
			}
			for (String word : line) {
				if (word.startsWith("0") || !word.matches("[0-9]")) {
					
				}
			}
		}
	}
	
	public void switchLines(int firstLine, int secondLine) {
		ArrayList<String> tempList = mapLines.get(secondLine);
		mapLines.set(secondLine, mapLines.get(firstLine));
		mapLines.set(firstLine, tempList);
	}
	
	public void switchWords(int firstLine, int firstWord, int secondLine, int secondWord) {
		String tempWord = mapLines.get(firstLine).get(firstWord);
		mapLines.get(firstLine).set(firstWord, mapLines.get(secondLine).get(secondWord));
		mapLines.get(secondLine).set(secondWord, tempWord);
	}
	
	public void reverseLine(int line) {
		Collections.reverse(mapLines.get(line));
	}
	
	public void reverseLines() {
		for (ArrayList<String> line : mapLines) {
			Collections.reverse(line);
		}
		Collections.reverse(mapLines);
	}
}
