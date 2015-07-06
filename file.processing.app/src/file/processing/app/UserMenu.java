package file.processing.app;

import java.util.Scanner;

public class UserMenu {

	private DataHolder holder;
	private DataTransformator transformator;
	private Scanner scanner;
	
	public UserMenu(DataHolder holder, DataTransformator transformator, Scanner scanner) {
		this.holder = holder;
		this.transformator = transformator;
		this.scanner = scanner;
	}
	
	public void startMenu() {
		printDoc();
		int code = scanner.nextInt();
		while(code != 0) {
			switch (code) {
				case 1:
					holder.validateData();
					break;
				case 2:
					switchLines();
					break;
				case 3:
					switchWords();
					break;
				case 4:
					reverseLine();
					break;
				case 5:
					holder.reverseLines();
					break;
				case 6:
					printDoc();
					break;
			}
			code = scanner.nextInt();
		}
		
		transformator.writeData(holder);
	}

	private void reverseLine() {
		System.out.println(Messages.REVERSE_LINE_MESSAGE);
		int line = scanner.nextInt();
		holder.reverseLine(line - 1);
		
	}

	private void switchWords() {
		System.out.println(Messages.SWITCH_WORDS_MESSAGE);
		int firstLine = scanner.nextInt();
		int firstWord = scanner.nextInt();
		int secondLine = scanner.nextInt();
		int secondWord = scanner.nextInt();
		holder.switchWords(firstLine - 1, firstWord - 1, secondLine - 1, secondWord - 1);
		
	}

	private void switchLines() {
		System.out.println(Messages.SWITCH_LINES_MESSAGE);
		int firstLine = scanner.nextInt();
		int secondLine = scanner.nextInt();
		holder.switchLines(firstLine - 1, secondLine - 1);
	}

	private void printDoc() {
		System.out.println(Messages.VALIDATE_MESSAGE_MENU);
		System.out.println(Messages.SWITCH_LINES_MESSAGE_MENU);
		System.out.println(Messages.SWITCH_WORDS_MENU);
		System.out.println(Messages.REVERSE_LINE_MENU);
		System.out.println(Messages.REVERSE_LINES_MENU);
		System.out.println(Messages.DOC);
		System.out.println(Messages.EXIT_MESSAGE_MENU);
	}
}
