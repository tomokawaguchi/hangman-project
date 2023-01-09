package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class HangmanUtils {
	/**
	 * @return an ArrayList with data read by a given file (words list .txt file)
	 * @throws Exception 
	 */
	public static ArrayList<String> getFileData(String filePath) throws Exception {
		// Creating File instance with path to the file in a computer
		File wordsFile = new File(filePath);
		
		ArrayList<String> array = new ArrayList<String>();
		
		if(wordsFile.exists()) {
			// Creating Scanner instance to read a file
			Scanner wordFileScanner = new Scanner(wordsFile);
			
			
			while(wordFileScanner.hasNext()) {
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
	 *  Return a randomly selected element from the given array list. 
	 *  The random index is generated within a size of given ArrayList.
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
	 * @param wrongCount - number of inputs that a user made mistake
	 * 
	 * It prints the different level of hangman on the console based on the number of wrong inputs made
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
	 * @param questionWord - the question word that a user is guessing
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
}
