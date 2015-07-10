package file.processing.app;

public class Messages {

	private Messages() {
	}
	
	public static final String EXIT_MESSAGE_MENU = "Type in '0' to exit the application.";
	public static final String VALIDATE_MESSAGE_MENU = "Type in '1' to validate the data.";
	public static final String SWITCH_LINES_MESSAGE_MENU = "Type in '2' to switch two lines.";
	public static final String SWITCH_WORDS_MENU = "Type in '3' to switch two numbers from different lines.";
	public static final String REVERSE_LINE_MENU = "Type in '4' to reverse a certain line.";
	public static final String REVERSE_LINES_MENU = "Type in '5' to reverse all lines.";
	public static final String DOC = "Type in '6' to validate the data and save to file.";
	
	public static final String SWITCH_LINES_MESSAGE = "Type in the two lines you want to switch like this: '<first_line_index> <second_line_index>'";
	public static final String SWITCH_WORDS_MESSAGE = "Type in the two lines and the two numbers likes this: '<first_line_index> <first_line_number_index> <second_line_index> <second_line_number_index>'";
	public static final String REVERSE_LINE_MESSAGE = "Type in the index of the line you want to reverse: ";
	public static final String EXIT_MESSAGE = "Bye!";
	public static final String INPUT_MISMATCH = "Error in your input. You did not enter numbers!";
	public static final String NO_MATCH_CHOICE = "Your choice did not match the options in the menu.";
	public static final String SAVE_FAILED = "Saving the changes was discarded.";
	public static final String ENTER_FILENAME = "Please type in the name of your file. Remember that the delimiter for path in Widnows is '\\\\'.";
	public static final String FILE_NOT_FOUND = "The file doesn't exist. Please check if the file exists and it's name and type it again: ";
	
	public static final String WRITE_SUCCESSFUL = "Writing to file successful.";
	public static final String CONVERT_SUCCESSFUL = "Converting file data successful.";
	
	public static final String VALIDATION_SUCCESSFUL = "The validation was successful.";
	public static final String VALIDATION_NOT_SUCCESSFUL = "The validation was not successfull.";
	public static final String INVALID_LINE_NUMBERS = "You entered invalid line indexes. The procedure was not executed.";
	public static final String SWITCH_LINE_SUCCESSFUL = "The lines were switched successfully.";
	public static final String SWITCH_WORDS_FAILED = "You entered invalid line or number indexes. The procedure was not executed.";
	public static final String SWITCH_WORDS_SUCCESSFUL = "The switching of numbers was successful.";
	public static final String REVERSE_LINE_FAILED = "You entered invalid line index. The procedure was not executed.";
	public static final String REVERSE_LINE_SUCCESSFUL = "The lines was reversed successfully.";
	public static final String REVERSE_LINES_SUCCESSFUL = "The reversing of lines was successful.";
	
}
