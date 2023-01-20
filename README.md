# Hangman Game Project

This project was built in Java and it allows users to play a hangman game in a Java console.

<img src="https://github.com/tomokawaguchi/hangman-project/blob/main/src/project-snapshot.png" width="auto" height="400" />

## Project Brief

### Aims

The aim of this project is to reinforce my foundamental learning in Java by writing code that runs in Java console, to understand the object types and basic loops and conditional logics in Java.

### MVP (conducted as Nology course work)

**Basic Requirements**

- Recreate a version of the game Hangman to be played in a Java console application.
- The game should randomly select a word from the provided list
- When the word is selected, a row of underscores representing letters in the word should be printed to the console, for example:

```
_ _ _ _ _
```

for "hello"

- the user should be asked to enter a letter
- if the letter entered by the user is in the word, the letter should be revealed, for example

```
H _ _ _ _
```

when the user enters "H"

- if the letter is not in the word, a part of the hangman should be printed to the console, for example:

```
___|___
```

after the first wrong letter is entered,

```
   |
   |
   |
   |
   |
   |
   |
___|___
```

after the second incorrect letter is entered,

```
____________
   |
   |
   |
   |
   |
   |
   |
___|___
```

after the third incorrect letter is entered.

- the user should be able to guess wrong 7 times before they loose
- they should be given the option to guess a letter or the whole word

**Bonuses**

- Read the word list directly from the file when the application starts
- Create a history file that keeps track of user wins/losses and how many letters they guessed it in
- Give user the option to verse the computer - the computer should select a letter, if the letter selected is in the word, it should be revealed, just like with user guesses. The computer should also try to guess the whole word.

## Technical Implementation

Throughout the game process, the code is able to communicate with a user by utilising `Scanner` class. The prompt message ensures a user what to enter in a console and the inputs from the user will take consideration into the conditional logics in the code.

Some of the featured functionalities are:

**1. Reading a words list & selecting a random word**

Once the game is started, the question word is selected from a local txt file randomly. I have used `File` class to read a data from specified path to the file.

**2. Logging game results in a file**

Once you run the Java console and until you exit the game, all of the game will be logged in a separate txt file with the game results information such as question word, result of the game and number of guess made. This was achieved with `FileWriter` class.

**3. Player's guess check handling**

Either of player's selection in guessing style, player inputs will be stored in `ArrayList<ArrayList<Character>>`. From there, depending on the game style, I have provided if else conditions. I have decided to use this data type as it will allows me to check and reveal the correct letter when a player is guessing by a whole word.

## Refection

- This was my first Java project through Nology and it was challenging at the beginning due to the Java's nature of being type strict language. In order not to loose my logic, it was very helpful to write pseudo code as I often got stuck issues relating to the type mismatch or limited methods available to specific data types.

- Through this project, I have managed to deepen my understanding of multi-dimensional ArrayList.

- When I completed this project initially, I had `Hangman` class and `HangmanUtils` class holding static methods. This was not the Java way to organise the codes, more of JavaScript way. So I have refactor my project structure so that there is only `Hangman` class containing `main` method for the main logic and supporting methods underneath.

## Future Goals

- Making a game to be multi-player or playing against a computer.
