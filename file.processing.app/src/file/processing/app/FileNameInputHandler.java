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

	public InputFile typeInFileName() {
		System.out.println(Messages.ENTER_FILENAME);
		String fileName = scanner.nextLine();
		
		Path pathToFile = Paths.get(fileName);
		File file = pathToFile.toFile();
		while (!file.exists()) {
			System.err.println(Messages.FILE_NOT_FOUND);
			fileName = scanner.nextLine();
			pathToFile = Paths.get(fileName);
			file = pathToFile.toFile();
		}
		
		return new InputFile(pathToFile);
	}
}
