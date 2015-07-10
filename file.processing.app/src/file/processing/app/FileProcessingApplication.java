package file.processing.app;

import java.util.Scanner;

public class FileProcessingApplication {

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in);) {
			FileNameInputHandler nameInputHandler = new FileNameInputHandler(scanner);
			
			InputFile transformator = nameInputHandler.typeInFileName();
			DataProcessor holder = transformator.readData();
			
			UserMenu userMenu = new UserMenu(holder, transformator, scanner);
			userMenu.startMenu();
			
		} catch (Exception exc){
			exc.printStackTrace();
		}
		
//		/Users/aleksandarivanov/git/JavaProject/file.processing.app/example.txt
	}
}
