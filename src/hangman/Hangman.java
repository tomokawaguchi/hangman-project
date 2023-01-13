package hangman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
	// ---- local variables for the game logics ----
	static char guessOption = 'l'; // guess a letter by default
	static String questionWord = "";
	static int wrongCounter = 0;
	static boolean isReady;
	static String guessType = "";

	static int arrTracker = 0;
	static boolean hasWon = false;
	static boolean playAgain = true;

	// 2D ArrayList ==> [["word"], ["word"], ["word"]]
	static ArrayList<ArrayList<Character>> playerInputs = new ArrayList<ArrayList<Character>>();
	
	// ---- SCANNER related classes/variable ----
	static Scanner userInputScanner = new Scanner(System.in);
	
	static ArrayList<String> wordList = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {
		// ---- FILE related classes/variables ----
		// Create a txt file to store the game results details
		final File resultsFile = new File("./src/data-files/results.txt");		
		FileWriter writer = new FileWriter("./src/data-files/results.txt");
		// Read and file and store the data in an array list
		wordList = getFileData("./src/data-files/words.txt");
		

		// Game settings
		while (playAgain) {
			// Reset the variables
			resetVariables();

			// Game options for a player to select
			while (!isReady) {
				isReady = handleGameSetting();
			}

			System.out.println();

			if (isReady) {
				// Generate a random word from the array list --> question of the game
				questionWord = selectRandomWord(wordList);
				
				System.out.println(questionWord + " is question word!");
				
				// Print "_" to indicate the length of word for a player
				printQuestion();
				

				// A player can keep guessing unless a player makes 7 mistakes
				while (wrongCounter < 7) {
					int correctlettersCounter = 0;

					// Prompt a player to enter the answer
					printMessage(String.format("Enter a %s", guessType));

					// Read and store the player input - lower cased here
					String currentInput = userInputScanner.nextLine().toLowerCase();

					// when non alphabetics inputs are entered, including spaces and symbols -->
					// Finish this turn
					if (!currentInput.matches("^[a-z]+")) {
						handleWrongGuess("Invalid input! Please enter only alphabets");
						continue;
					}

					// Guess By a LETTER: currentInput is a single letter
					if (guessOption == 'l') {
						// Check if the input is a single input
						if (currentInput.length() == 1) {
							playerInputs.get(0).add(currentInput.charAt(0));

							// Check early to let a user know if a letter matches in a message
							if (questionWord.contains(currentInput)) {
								System.out.println("Nice guess!");
							} else {
								handleWrongGuess("Wrong guess!");

								if (wrongCounter == 7) continue;
							}
						} else {
							handleWrongGuess("Please enter only 1 letter at a time!");
							continue;
						}
					} else {
						// Guess By a WORD: check the input is the same length as question word
						if (currentInput.length() == questionWord.length()) {
							// Ensure that extra spot is available in the ArrayList
							if (arrTracker > 0) {
								playerInputs.add(new ArrayList<Character>());
							}
							// Store player input in array list - only valid input as character
							for (int i = 0; i < currentInput.length(); i++) {
								playerInputs.get(arrTracker).add(currentInput.charAt(i));
							}
							arrTracker++;

							// When the length is the same, but guessed wrong partially or entirely
							if (!currentInput.equals(questionWord)) {
								handleWrongGuess("Wrong guess!");
							}
						} else {
							String message = String.format(
									"Wrong word length! You should guess a word that consists of %d letters",
									questionWord.length());
							handleWrongGuess(message);
							continue;
						}
					}

					System.out.println();

					// Output the current result
					for (int i = 0; i < questionWord.length(); i++) {
						String current = "_ ";

						for (int j = 0; j < playerInputs.size(); j++) {
							// For guessing a letter
							if (guessOption == 'l') {
								// Letter: always only 1 index/element [['a', 'b', 'c'...etc]]
								if (playerInputs.get(j).contains(questionWord.charAt(i))) {
									// For guessing a letter
									current = questionWord.charAt(i) + " ";
									correctlettersCounter++;
								}
							} else {

								// Word: Loop through each [] at the same index to check if the same letter is
								// exist or not
								if (playerInputs.get(j).get(i).equals(questionWord.charAt(i))) {
									current = questionWord.charAt(i) + " ";
								}

								// When the whole input is correct
								if (currentInput.equals(questionWord)) {
									correctlettersCounter = questionWord.length();
								}
							}
						}

						System.out.print(current);
					}

					// When all the inputs are correct, exit the loop
					if (correctlettersCounter == questionWord.length()) {
						printMessage("You have guesseed all correctly!");
						break;
					}
				}

				hasWon = wrongCounter < 7;
				showFinalResult(hasWon, questionWord);

			}

			// Logging a results on the file
			logResults(writer);
			
			
			
			// Handle player's willing to go another round of game or not
			playAgain = handleRepeat();
			
			if (!playAgain) {
				writer.close();
				System.out.println("Bye for now!");
			}
		}
	}
	
	/**
	 * To reset the variable for starting over the game
	 */
	public static void resetVariables() {
		playerInputs.clear();
		playerInputs.add(new ArrayList<Character>());
		wrongCounter = 0;
		arrTracker = 0;
	}

	/**
	 * @return boolean isReady - return true/false depending on the players answer
	 * 
	 * This is to handle the user prompt messages and game setting that user can select 
	 * at before starting a game 
	 */
	public static boolean handleGameSetting() {
		System.out.println("isReady is; " + isReady);
		System.out.println("Hi there!\nHow would you like to play the Hangman game?");
		System.out.println(" Enter l for guessing a letter\n Enter w for guessing a word");
		guessOption = userInputScanner.next().charAt(0);

		guessType = (guessOption == 'w') ? "word" : "letter";

		System.out.println();
		System.out.println(String.format("Your Selection: Playing single-player by guessing a %s", guessType));
		System.out.println();
		System.out.println(
				"Are you ready?\n Enter y for YES to start a game\n Enter n for NO to select the game settings again");
		
		return isReady = (userInputScanner.next().charAt(0) == 'y') ? true : false;
	}

	/**
	 * @return an ArrayList with data read by a given file (words list .txt file)
	 * @throws Exception
	 */
	public static ArrayList<String> getFileData(String filePath) throws Exception {
		// Creating File instance with path to the file in a computer
		File wordsFile = new File(filePath);

		ArrayList<String> array = new ArrayList<String>();

		if (wordsFile.exists()) {
			// Creating Scanner instance to read a file
			Scanner wordFileScanner = new Scanner(wordsFile);

			while (wordFileScanner.hasNext()) {
				array.add(wordFileScanner.nextLine());
			}
		} else {
			throw new Exception("File does not exist!");
		}

		return array;
	}
	

	/**
	 * @param wordList - ArrayList containing a list of words
	 * @return a string element at the random index from a given array list
	 * 
	 * Return a randomly selected element from the given array list. The
	 * random index is generated within a size of given ArrayList.
	 */
	public static String selectRandomWord(ArrayList<String> wordList) {
		// Initiate Random class
		Random random = new Random();
		// Generate the random number within a size of given ArrayList
		int randomIndex = random.nextInt(wordList.size());

		// Access the value of array list at the random index and return it
		return wordList.get(randomIndex);
	}
	
	
	/**
	 * Handle printing the "_ " to indicate the length of qustion word
	 */
	public static void printQuestion() {
		for (int i = 0; i < questionWord.length(); i++) {
			System.out.print("_ ");
		}
	}
	
	/**
	 * To print a prompt message for a player for the next turn
	 */
	public static void printMessage(String message) {
		System.out.println();
		System.out.println();
		System.out.println(message);
	}
	
	/**
	 * @param message will be used in printHangMan() to indicate what went wrong for a player
	 * This is to increase wrongCounter and print hangman by calling printHangMan() method
	 */
	public static void handleWrongGuess(String message) {
		wrongCounter++;
		printHangMan(wrongCounter, message);
	}
	
	/**
	 * @return true/false to indicate if a player is willing to play again
	 */
	public static boolean handleRepeat() {
		System.out.println();
		System.out.println("Would you like to play again?");
		System.out.println(" Enter y for starting a new game\n Enter n for resting");
		return playAgain = userInputScanner.next().charAt(0) == 'y' ? true : false; // everything except 'y' exit the game

	}

	/**
	 * @param wrongCount - number of inputs that a user made mistake
	 * 
	 * It prints the different level of hangman on the console
	 * based on the number of wrong inputs made
	 */
	public static void printHangMan(Integer wrongCount, String message) {
		System.out.println("");

		if (wrongCount == 1) {
			System.out.println(message);
			System.out.println();
			System.out.println("___|___");
		}

		if (wrongCount == 2) {
			System.out.println(message);
			System.out.println();
			System.out.print("   |\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n");
			System.out.println("___|___");
		}

		if (wrongCount == 3) {
			System.out.println(message);
			System.out.println();
			System.out.println("____________");
			System.out.print("   |\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n");
			System.out.println("___|___");
		}

		if (wrongCount == 4) {
			System.out.println(message);
			System.out.println();
			System.out.println("____________");
			System.out.print("   |    |\n" + "   |    O\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n");
			System.out.println("___|___");
		}

		if (wrongCount == 5) {
			System.out.println(message);
			System.out.println();
			System.out.println("____________");
			System.out
					.print("   |    |\n" + "   |    O\n" + "   |   \\ /\n" + "   |\n" + "   |\n" + "   |\n" + "   |\n");
			System.out.println("___|___");
		}

		if (wrongCount == 6) {
			System.out.println(message);
			System.out.println();
			System.out.println("____________");
			System.out.print("   |    |\n" + "   |    O\n" + "   |   \\ /\n" + "   |    |\n" + "   |    |\n" + "   |\n"
					+ "   |\n");
			System.out.println("___|___");
		}

		if (wrongCount == 7) {
			System.out.println(message);
			System.out.println();
			System.out.println("____________");
			System.out.print("   |    |\n" + "   |    O\n" + "   |   \\ /\n" + "   |    |\n" + "   |    |\n"
					+ "   |   / \\\n" + "   |\n");
			System.out.println("___|___");
		}

		System.out.println("");
	}

	/**
	 * @param correctCounter - number of correct letters they guessed
	 * @param questionWord   - the question word that a user is guessing
	 * 
	 * It shows the final result of game depending on the passed parameter
	 */
	public static void showFinalResult(boolean hasWon, String questionWord) {
		if (hasWon) {
			System.out.println("");
			System.out.println("-------- RESULT ----------");
			System.out.println("");
			System.out.println("        You won!");
			System.out.println("        \\(^o^)/  ");
			System.out.println("");
			System.out.println("---------------------------");
		} else {
			System.out.println("");
			System.out.println("-------- RESULT ----------");
			System.out.println("");
			System.out.println("     You lost (´;︵;`)  ");
			System.out.println("");
			System.out.println("     ANSWEAR: " + questionWord);
			System.out.println("--------------------------");
		}
	}

	/**
	 * @param hasWon       - boolean value to indicate if a player won or not
	 * @param wrongCounter - int value to indicate a number of mistakes made
	 * @param questionWord - String value of question word
	 * @param writer       - FileWriter class
	 * @throws IOException
	 * 
	 * It handles storing the results in an ArrayList<ArrayList<String>> and writing a results of the games in a file
	 */
	public static void logResults(FileWriter writer) throws IOException {
		ArrayList<ArrayList<String>> resultsArr = new ArrayList<ArrayList<String>>();
		String resultMsg = hasWon ? "WIN" : "LOSS";

		resultsArr.add(new ArrayList<String>());
		resultsArr.get(resultsArr.size() - 1).add(questionWord);
		resultsArr.get(resultsArr.size() - 1).add(resultMsg);
		resultsArr.get(resultsArr.size() - 1).add(Integer.toString(++wrongCounter));

		for (int i = 0; i < resultsArr.size(); i++) {
			// Writing a game results details in a file
			writer.write("Question Word: " + resultsArr.get(i).get(0) + "\n");
			writer.write("Result: " + resultsArr.get(i).get(1) + "\n");
			writer.write("Number of Guess: " + resultsArr.get(i).get(2) + "\n");
			writer.write("\n");
		}
	}
}
