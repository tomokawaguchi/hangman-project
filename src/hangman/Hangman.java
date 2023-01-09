package hangman;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Hangman {

	public static void main(String[] args) throws Exception {
		
		// ---- FILE related classes/variables ---- 
		// Create a txt file to store the game results details
		final File resultsFile = new File("/Users/tomoko/Documents/NOLOGY/projects/hangman-project/src/data-files/results.txt");			
		FileWriter writer = new FileWriter("/Users/tomoko/Documents/NOLOGY/projects/hangman-project/src/data-files/results.txt");
		
		// Read and file and store the data in an array list
		ArrayList<String> wordList = new ArrayList<String>();
		wordList = HangmanUtils.getFileData("/Users/tomoko/Documents/NOLOGY/projects/hangman-project/src/data-files/words.txt");
		
		
		// ---- SCANNER related classes/variable ----
		// Scanner class for setting selection input to read
		Scanner settingInput = new Scanner(System.in);
		// Scanner class for game input to read
		Scanner playerInput = new Scanner(System.in);
		// Scanner class for play again input to read
		Scanner playAgainInput = new Scanner(System.in);
		
		
		// ---- local variables for the game logics ----
		char guessOption = 'l';  // guess a letter by default
		String questionWord = "";
		int wrongCounter = 0;
		boolean isReady = false;
		String guessType = "";
		
		int arrTracker = 0;
		boolean hasWon = false;
		boolean playAgain = true;
		
		// 2D ArrayList ==> [["word"], ["word"], ["word"]]
		ArrayList<ArrayList<Character>> playerInputs = new ArrayList<ArrayList<Character>>();
		
		
		// Game settings
		while(playAgain) {
			// Reset the variables 
			playerInputs.clear();
			playerInputs.add(new ArrayList<Character>());
			wrongCounter = 0;
			arrTracker = 0;
			
			
			// Game options
			while(!isReady) {
				System.out.println("Hi there!\nHow would you like to play the Hangman game?");
				System.out.println(" Enter l for guessing a letter\n Enter w for guessing a word");
				guessOption = settingInput.next().charAt(0);
				
				guessType = (guessOption == 'w') ? "word" : "letter"; 
				
				System.out.println();
				System.out.println(String.format("Your Selection: Playing single-player by guessing a %s", guessType));
				System.out.println();
				System.out.println("Are you ready?\n Enter y for YES to start a game\n Enter n for NO to select the game settings again");
				isReady = (settingInput.next().charAt(0) == 'y') ? true : false;			
			}
			
			System.out.println();
			
			if(isReady) {
				// Generate a random word from the array list --> question of the game 
				questionWord = HangmanUtils.selectRandomWord(wordList);
				
				// Print "_" to indicate the length of word for a player
				for(int i = 0; i < questionWord.length(); i++) {
					System.out.print("_ ");
				}
				
				
				// A player can keep guessing unless a player makes 7 mistakes
				while(wrongCounter < 7) {
					int correctlettersCounter = 0;
					
					// Prompt a player to enter the answer
					System.out.println();
					System.out.println();
					System.out.println(String.format("Enter a %s", guessType));
					
					
					// Read and store the player input - lower cased here
					String currentInput = playerInput.nextLine().toLowerCase(); 
					
					// when non alphabetics inputs are entered, including spaces and symbols --> Finish this turn
					if(!currentInput.matches("^[a-z]+")) {
						wrongCounter++;	
						HangmanUtils.printHangMan(wrongCounter, "Invalid input! Please enter only alphabets");
						continue;
					}
					
					// Guess By a LETTER: currentInput is a single letter
					if(guessOption == 'l') {
						// Check if the input is a single input
						if(currentInput.length() == 1) {
							playerInputs.get(0).add(currentInput.charAt(0));
							
							// Check early to let a user know if a letter matches in a message
							if(questionWord.contains(currentInput)) {
								System.out.println("Nice guess!");
							} else {
								wrongCounter++;	
								HangmanUtils.printHangMan(wrongCounter, "Wrong guess!");
								
								if(wrongCounter == 7) continue;
							}
						} else {
							wrongCounter++;	
							HangmanUtils.printHangMan(wrongCounter, "Please enter only 1 letter at a time!");
							continue;
						}				
					} else {
						// Guess By a WORD: check the input is the same length as question word
						if(currentInput.length() == questionWord.length()) {
							// Ensure that extra spot is available in the ArrayList
							if(arrTracker > 0) {
								playerInputs.add(new ArrayList<Character>());
							}
							// Store player input in array list - only valid input as character
							for (int i = 0; i < currentInput.length(); i++) {
								playerInputs.get(arrTracker).add(currentInput.charAt(i));
							}
							arrTracker++;
							
							// When the length is the same, but guessed wrong partially or entirely
							if(!currentInput.equals(questionWord)) {
								wrongCounter++;
								HangmanUtils.printHangMan(wrongCounter, "Wrong guess!");
							}
						} else {
							wrongCounter++;	
							String message = String.format("Wrong word length! You should guess a word that consists of %d letters", questionWord.length());
							HangmanUtils.printHangMan(wrongCounter, message);
							continue;
						}				
					}
					
					
					System.out.println();
					
					// Output the current result		
					for(int i = 0; i < questionWord.length(); i++) {
						String current = "_ ";
						
						for(int j = 0; j < playerInputs.size(); j++) {
							// For guessing a letter
							if(guessOption == 'l') {
								// Letter: always only 1 index/element [['a', 'b', 'c'...etc]] 
								if(playerInputs.get(j).contains(questionWord.charAt(i))) {
									//For guessing a letter
									current = questionWord.charAt(i) + " ";
									correctlettersCounter++;
								}						
							} else {
								
								// Word: Loop through each [] at the same index to check if the same letter is exist or not
								if(playerInputs.get(j).get(i).equals(questionWord.charAt(i))) {
									current = questionWord.charAt(i) + " ";   
								}
								
								// When the whole input is correct
								if(currentInput.equals(questionWord)) {
									correctlettersCounter = questionWord.length();
								}
							}
						}
						
						System.out.print(current);
					}
					
					
					// When all the inputs are correct, exit the loop
					if (correctlettersCounter == questionWord.length()) {
						System.out.println();
						System.out.println();
						System.out.println("You have guesseed all correctly!");
						break;
					}
				}
				
				hasWon = wrongCounter < 7;
				HangmanUtils.showFinalResult(hasWon, questionWord);
				
			}
			
			// Logging a results on the file
			HangmanUtils.logResults(hasWon, wrongCounter, questionWord, writer);
			
			
			System.out.println();
			System.out.println("Would you like to play again?");
			System.out.println(" Enter y for starting a new game\n Enter n for resting");
			playAgain = playAgainInput.next().charAt(0) == 'y' ? true : false; // everything except 'y' exit the game
			
			if(!playAgain) {
				writer.close();
				System.out.println("Bye for now!");
			}
		}
	}
}
