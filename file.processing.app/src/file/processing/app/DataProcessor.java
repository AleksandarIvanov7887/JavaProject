package file.processing.app;

import java.util.ArrayList;
import java.util.Collections;

public class DataProcessor {
	
	private ArrayList<ArrayList<String>> lines;
	
	public DataProcessor(ArrayList<ArrayList<String>> mapLines) {
		this.lines = mapLines;
	}
	
	public ArrayList<ArrayList<String>> getData() {
		return lines;
	}
	
	public boolean validateData() {
		boolean validateSuccessful = true;
		for (ArrayList<String> line : lines) {
			if (line.contains("")) {
				validateSuccessful = false;
				System.err.println("Line " + line + "starts with whitespace. Your file is not valid.");
			}
			for (String word : line) {
				if (!word.matches("[0-9]*")) {
					validateSuccessful = false;
					System.err.println("There is invalid character in " + word + " in line " + line);
				}
				if (word.startsWith("0")) {
					validateSuccessful = false;
					System.err.println("Number " + word + " starts with '0' in line " + line );
				}
			}
		}
		if (validateSuccessful) {
			System.out.println(Messages.VALIDATION_SUCCESSFUL);
		} else {
			System.err.println(Messages.VALIDATION_NOT_SUCCESSFUL);
		}
		return validateSuccessful;
	}
	
	public void switchLines(int firstLineIndex, int secondLineIndex) {
		if (firstLineIndex >= lines.size() || secondLineIndex >= lines.size() || 
				firstLineIndex < 0 || secondLineIndex < 0) {
			System.err.println(Messages.INVALID_LINE_NUMBERS);
		} else {
			ArrayList<String> tempList = lines.get(secondLineIndex);
			lines.set(secondLineIndex, lines.get(firstLineIndex));
			lines.set(firstLineIndex, tempList);
			System.out.println(Messages.SWITCH_LINE_SUCCESSFUL);
		}
		
	}
	
	public void switchWords(int firstLineIndex, int firstWordIndex, int secondLineIndex, int secondWordIndex) {
		if (firstLineIndex >= lines.size() || secondLineIndex >= lines.size() || 
				firstWordIndex >= lines.get(firstLineIndex).size() ||secondWordIndex >= lines.get(secondLineIndex).size() ||
				firstLineIndex < 0 || secondLineIndex < 0 || firstWordIndex < 0 || secondWordIndex < 0) {
			System.err.println(Messages.SWITCH_WORDS_FAILED);
		} else {
			String tempWord = lines.get(firstLineIndex).get(firstWordIndex);
			lines.get(firstLineIndex).set(firstWordIndex, lines.get(secondLineIndex).get(secondWordIndex));
			lines.get(secondLineIndex).set(secondWordIndex, tempWord);
			System.out.println(Messages.SWITCH_WORDS_SUCCESSFUL);
		}
	}
	
	public void reverseLine(int lineIndex) {
		if (lineIndex > lines.size() || lineIndex < 0) {
			System.err.println(Messages.REVERSE_LINE_FAILED);
		} else {
			Collections.reverse(lines.get(lineIndex));
			System.out.println(Messages.REVERSE_LINE_SUCCESSFUL);
		}
	}
	
	public void reverseLines() {
		for (ArrayList<String> line : lines) {
			Collections.reverse(line);
		}
		Collections.reverse(lines);
		System.out.println(Messages.REVERSE_LINES_SUCCESSFUL);
	}
}
