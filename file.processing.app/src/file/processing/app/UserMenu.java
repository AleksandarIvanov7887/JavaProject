package file.processing.app;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserMenu {

	private DataProcessor holder;
	private InputFile transformator;
	private Scanner scanner;
	
	public UserMenu(DataProcessor holder, InputFile transformator, Scanner scanner) {
		this.holder = holder;
		this.transformator = transformator;
		this.scanner = scanner;
	}
	
	public void startMenu() {
		String choice = null;
		while(!"0".equals(choice)) {
			printDoc();
			choice = scanner.nextLine();
			
			switch (choice) {
				case "0":
					break;
				case "1":
					holder.validateData();
					break;
				case "2":
					switchLines();
					break;
				case "3":
					switchWords();
					break;
				case "4":
					reverseLine();
					break;
				case "5":
					holder.reverseLines();
					break;
				case "6":
					validateAndSave();
					break;
				default:
					System.out.println(Messages.NO_MATCH_CHOICE);
					break;
			}
		}
		System.out.println(Messages.EXIT_MESSAGE);
	}

	private void validateAndSave() {
		if (holder.validateData()) {
			transformator.writeData(holder);
		} else {
			System.err.println(Messages.SAVE_FAILED);
		}
	}

	private void reverseLine() {
		System.out.println(Messages.REVERSE_LINE_MESSAGE);
		try {
			int line = scanner.nextInt();
			holder.reverseLine(line - 1);
		} catch (InputMismatchException exc) {
			System.err.println(Messages.INPUT_MISMATCH);
		}
		scanner.nextLine();
	}
	

	private void switchWords() {
		System.out.println(Messages.SWITCH_WORDS_MESSAGE);
		try {
			int firstLine = scanner.nextInt();
			int firstWord = scanner.nextInt();
			int secondLine = scanner.nextInt();
			int secondWord = scanner.nextInt();
			holder.switchWords(firstLine - 1, firstWord - 1, secondLine - 1, secondWord - 1);
		} catch (InputMismatchException exc) {
			System.err.println(Messages.INPUT_MISMATCH);
		}
		scanner.nextLine();
	}

	private void switchLines() {
		System.out.println(Messages.SWITCH_LINES_MESSAGE);
		try {
			int firstLine = scanner.nextInt();
			int secondLine = scanner.nextInt();
			holder.switchLines(firstLine - 1, secondLine - 1);
		} catch (InputMismatchException exc) {
			System.err.println(Messages.INPUT_MISMATCH);
		}
		scanner.nextLine();
	}

	private void printDoc() {
		System.out.println();
		System.out.println(Messages.VALIDATE_MESSAGE_MENU);
		System.out.println(Messages.SWITCH_LINES_MESSAGE_MENU);
		System.out.println(Messages.SWITCH_WORDS_MENU);
		System.out.println(Messages.REVERSE_LINE_MENU);
		System.out.println(Messages.REVERSE_LINES_MENU);
		System.out.println(Messages.DOC);
		System.out.println(Messages.EXIT_MESSAGE_MENU);
		System.out.println();
	}
	
}
