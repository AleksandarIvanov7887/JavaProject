package file.processing.app;

import java.util.Scanner;

public class FileProcessingApplication {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		FileNameInputHandler nameInputHandler = new FileNameInputHandler(scanner);
		
		DataTransformator transformator = nameInputHandler.inputFileName();
		DataHolder holder = transformator.getDataHolder();
		
		
		UserMenu userMenu = new UserMenu(holder, transformator, scanner);
		userMenu.startMenu();
		scanner.close();
		
//		/Users/aleksandarivanov/git/JavaProject/file.processing.app/example
	}
}
