package file.processing.app;

import java.util.ArrayList;
import java.util.Collections;

public class DataHolder {
	
	private ArrayList<ArrayList<String>> mapLines;
	
	public DataHolder(ArrayList<ArrayList<String>> mapLines) {
		this.mapLines = mapLines;
	}
	
	public ArrayList<ArrayList<String>> getData() {
		return mapLines;
	}
	
	public boolean validateData() {
		boolean validateSuccessful = true;
		for (ArrayList<String> line : mapLines) {
			if (line.contains("")) {
				validateSuccessful = false;
				System.out.println("Line " + line + "starts with whitespace. Your file is not valid.");
			}
			for (String word : line) {
				if (!word.matches("([0-9])\\w+")) {
					validateSuccessful = false;
					System.out.println("There is invalid character in " + word + " in line " + line);
				}
				if (word.startsWith("0")) {
					validateSuccessful = false;
					System.out.println("NUmber " + word + " starts with '0' in line " + line );
				}
			}
		}
		if (validateSuccessful) {
			System.out.println("The validation was successful.");
		} else {
			System.out.println("the validation was not successfull. Check the errors from above.");
		}
		return validateSuccessful;
	}
	
	public void switchLines(int firstLine, int secondLine) {
		if (firstLine >= mapLines.size() || secondLine >= mapLines.size() || 
				firstLine < 0 || secondLine < 0) {
			System.out.println("You entered invalid line numbers. The procedure was not executed.");
		} else {
			ArrayList<String> tempList = mapLines.get(secondLine);
			mapLines.set(secondLine, mapLines.get(firstLine));
			mapLines.set(firstLine, tempList);
			System.out.println("The lines were switched successfully.");
		}
		
	}
	
	public void switchWords(int firstLine, int firstWord, int secondLine, int secondWord) {
		if (firstLine >= mapLines.size() || secondLine >= mapLines.size() || 
				firstWord >= mapLines.get(firstLine).size() ||secondWord >= mapLines.get(secondLine).size() ||
				firstLine < 0 || secondLine < 0 || firstWord < 0 || secondWord < 0) {
			System.out.println("You entered invalid lines and index of numbers. The procedure was not executed.");
		} else {
			String tempWord = mapLines.get(firstLine).get(firstWord);
			mapLines.get(firstLine).set(firstWord, mapLines.get(secondLine).get(secondWord));
			mapLines.get(secondLine).set(secondWord, tempWord);
			System.out.println("The switching of words was successful.");
		}
	}
	
	public void reverseLine(int line) {
		if (line > mapLines.size() || line < 0) {
			System.out.println("You entered invalid line number. The procedure was not executed.");
		} else {
			Collections.reverse(mapLines.get(line));
			System.out.println("The lines was reversed successfully.");
		}
	}
	
	public void reverseLines() {
		for (ArrayList<String> line : mapLines) {
			Collections.reverse(line);
		}
		Collections.reverse(mapLines);
		System.out.println("The reversing of lines was successfully.");
	}
}
