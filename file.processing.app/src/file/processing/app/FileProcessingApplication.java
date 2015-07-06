package file.processing.app;

import java.util.Scanner;

public class FileProcessingApplication {

	public static void main(String[] args) {
		DataTransformator transformator = new DataTransformator("E://git//JavaProject//file.processing.app//example");
		DataHolder holder = transformator.getDataHolder();
		
		Scanner scanner = new Scanner(System.in);
//		System.out.println("Please type in the name of your file: ");
//		String name = userInput.nextLine();
		
		
		UserMenu userMenu = new UserMenu(holder, transformator, scanner);
		userMenu.startMenu();
		scanner.close();
	}
}
