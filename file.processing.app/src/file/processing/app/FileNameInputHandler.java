package file.processing.app;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileNameInputHandler {
	
	private Scanner scanner;

	public FileNameInputHandler(Scanner scanner) {
		super();
		this.scanner = scanner;
	}

	public DataTransformator inputFileName() {
		System.out.println("Please type in the name of your file: ");
		String fileName = scanner.nextLine();
		
		Path pathToFile = Paths.get(fileName);
		File file = pathToFile.toFile();
		while (!file.exists()) {
			System.out.println("The file doesn't exist. Please check if the file exists and it's name and type it again: ");
			fileName = scanner.nextLine();
			pathToFile = Paths.get(fileName);
			file = pathToFile.toFile();
		}
		
		return new DataTransformator(pathToFile);
	}
}
