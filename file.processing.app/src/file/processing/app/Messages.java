package file.processing.app;

public class Messages {

	private Messages() {
	}
	
	public static final String EXIT_MESSAGE_MENU = "Type in '0' to save the cahnges into the file and exit the application.";
	public static final String VALIDATE_MESSAGE_MENU = "Type in '1' to validate the data before or after changes.";
	public static final String SWITCH_LINES_MESSAGE_MENU = "Type in '2' to switch two lines.";
	public static final String SWITCH_WORDS_MENU = "Type in '3' to switch two numbers from different lines.";
	public static final String REVERSE_LINE_MENU = "Type in '4' to reverse a certain line.";
	public static final String REVERSE_LINES_MENU = "Type in 5' to reverse a all lines.";
	public static final String DOC = "Type in 6' to validate the data and save to file.";
	
	public static final String SWITCH_LINES_MESSAGE = "Type in the two lines you want to switch like this: '<first_line_index> <second_line_index>'";
	public static final String SWITCH_WORDS_MESSAGE = "Type in the two lines and the two numbers likes this: '<first_line_index> <first_line_number_index> <second_line_index><second_line_number_index>'";
	public static final String REVERSE_LINE_MESSAGE = "Type in the index of the line you want to reverse: ";
}
