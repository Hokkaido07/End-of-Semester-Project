
/**
 * This is the end of semester project - Hokkaido's Cipher
 * Author: Alexis Chiu
 * Date:12-22-2023
 * This is Hokkaido's cipher, an encrypt and decipher program using the Hokkaido cipher, as well as a game which brings
 * the player along on a journey while learning the importance of using ciphers in real world situations.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {
    /**
     * Declaring all ANSI color codes
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BOLD = "\u001B[1m";

    public static void main(String[] args) {
        introToProgram();
        determineIntention();
    }


    public static void introToProgram() { //Introduces the Program
        System.out.println(ANSI_BLUE + ANSI_BOLD +
                "  _  _     _   _        _    _     _       ___ _      _            \n" +
                " | || |___| |_| |____ _(_)__| |___( )___  / __(_)_ __| |_  ___ _ _ \n" +
                " | __ / _ \\ / / / / _` | / _` / _ \\/(_-< | (__| | '_ \\ ' \\/ -_) '_|\n" +
                " |_||_\\___/_\\_\\_\\_\\__,_|_\\__,_\\___/ /__/  \\___|_| .__/_||_\\___|_|  \n" +
                "                                                |_|             " + ANSI_RESET);
        System.out.println("Welcome to Hokkaido's Cipher. This is a encrypting and deciphering program which allows you enter a word to encrypt or have a word deciphered from a file of words. You are also able to play a game which allows you to learn about encrypting and deciphering words.");
        getStringInput("Would you like to start the program? If so, press" + ANSI_RED + ANSI_BOLD + " Enter" + ANSI_RESET);
    }

    public static void determineIntention() { //Determines what the user would like to do in this program
        int intention = getIntInput("What would you like to do in this program today?\n" +
                "If you would like to encrypt a word or decipher a word, press" + ANSI_CYAN + " 1\n" + ANSI_RESET +
                "If you would like to play the game, press" + ANSI_PURPLE + " 2" + ANSI_RESET +
                "\nif you would like to exit the program, press " + ANSI_RED + "3" + ANSI_RESET + ".");
        if (intention == 1) {
            modeOne();
        } else if (intention == 2) {
            modeTwo();
        } else if (intention == 3) {
            System.exit(1);
        } else {
            System.out.println(ANSI_RED + "Error" + ANSI_RESET);
            determineIntention();
        }
    }

    /**
     * This method will be used to get any integer input from the player in this program
     *
     * @param message printed message to player for input
     * @return user input
     */

    public static int getIntInput(String message) {
        var string = getStringInput(message);
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Error. This is not a number, please try again: " + ANSI_RESET);
            return getIntInput(message);
        }
    }

    /**
     * This method will be used to get any string input from the player in this program
     *
     * @param message printed message to player for input
     * @return user input
     */

    public static String getStringInput(String message) {
        System.out.println(message);
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().toLowerCase();
        while (!checkInappropriate(input)) {
            input = getStringInput(ANSI_RED + "\nError. This word is either inappropriate. Please re-enter a word: " + ANSI_RESET);
        }
        return input;
    }

    /**
     * This is to encrypt the word
     *
     * @param input user input
     * @return encrypted word
     */

    public static String encryptingTheWord(String input) {
        String[] alphabetGroups = {"abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwx", "yz"};
        String codeword = "snowflake";

        String encryptedWord = "";
        for (int i = 0; i < input.length(); i++) {
            char letter = Character.toLowerCase(input.charAt(i));
            if (Character.isLetter(letter)) {
                int groupIndex = (letter - 'a') / 3;
                int codewordIndex = (letter - 'a') % 3 + 1;
                char codewordLetter = codeword.charAt(groupIndex);
                encryptedWord += codewordLetter + Integer.toString(codewordIndex);
            } else {
                encryptedWord += letter;
            }
        }

        return encryptedWord;

    }

    /**
     * This is to decipher the word
     *
     * @param input user input
     * @return decrypted word
     */

    public static String decipheringTheWord(String input) {
        String[] alphabetGroups = {"abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwx", "yz"};
        String codeword = "snowflake";

        String decryptedWord = "";
        for (int i = 0; i < input.length(); i += 2) {
            char codewordLetter = input.charAt(i);
            int codewordIndex = Character.getNumericValue(input.charAt(i + 1));
            int groupIndex = codeword.indexOf(codewordLetter);
            String group = alphabetGroups[groupIndex];
            char decryptedLetter = group.charAt(codewordIndex - 1);
            decryptedWord += decryptedLetter;
        }

        return decryptedWord;

    }

    public static List<String> loadInappropriateWords() { //Load words from the inappropriateWords file into a list of strings
        List<String> inappropriateWords = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Path.of("inappropriateWords"));
            for (String line : lines) {
                inappropriateWords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inappropriateWords;
    }


    /**
     * these are the methods which will be used for the encrypt and decipher modes
     */

    public static void modeOne() {
        int userChoice = getIntInput("\nYou have chosen to encrypt or decipher a word. To confirm, press " + ANSI_CYAN + "1. " + ANSI_RESET + "If you would like to change to the game mode, press " + ANSI_PURPLE + "2." + ANSI_RESET);
        if (userChoice > 2) {
            getIntInput(ANSI_RED + "\nError" + ANSI_RESET + " Enter a valid number to continue: ");
            modeOne();
        } else if (userChoice == 1) {
            encryptOrDecipher();
        } else {
            modeTwo();

        }
    }


    public static void encryptOrDecipher() { //Asks the user if they would like to encrypt or decipher
        int input = getIntInput("\nWould you like to encrypt or decipher a word? If you would like to encrypt, press " + ANSI_GREEN + "1." + ANSI_RESET + " If you would like to decipher, press " + ANSI_YELLOW + "2." + ANSI_RESET);

        if (input == 1) {
            doEncrypt();
        } else if (input == 2) {
            doDecipher();
        } else {
            System.out.println(ANSI_RED + "\nError. This is not a valid number. Please try again." + ANSI_RESET);
            encryptOrDecipher();

        }
    }


    public static void doEncrypt() {
        String encryptInput = getInputEncrypt();
        while (!checkValid(encryptInput)) {
            encryptInput = getStringInput(ANSI_RED + "\nError. This word is either inappropriate or contains numbers or special characters. Please re-enter a word: " + ANSI_RESET);
        }
        System.out.println(encryptingTheWord(encryptInput));
        if (getIntInput("\nIf you would like to go again, press " + ANSI_BLUE + "1" + ANSI_RESET + ". If you would like to exit the program, press " + ANSI_YELLOW + "2" + ANSI_RESET) == 1) {
            determineIntention();
        } else {
            System.out.println("Thank you for using this program.");
            System.exit(1);
        }
    }

    public static void doDecipher() {
        String decipherInput = getDecipherEncrypt();
        System.out.println(decipheringTheWord(decipherInput));
        if (getIntInput("\nIf you would like to go again, press " + ANSI_BLUE + "1" + ANSI_RESET + ". If you would like to exit the program, press " + ANSI_YELLOW + "2" + ANSI_RESET) == 1) {
            determineIntention();
        } else {
            System.out.println("Thank you for using this program.");
            System.exit(1);
        }
    }


    public static String getInputEncrypt() { //asks for user input for encrypting
        return getStringInput("Enter a word you would like to encrypt, please do not enter any numbers or special characters: ");
    }

    public static String getDecipherEncrypt() { //asks for user input for deciphering
        return getStringInput("Enter a word you would like to decipher, please do not enter any numbers or special characters: ");
    }

    /**
     * This checks whether the word inputs have any numbers or special characters
     *
     * @param wordToCheck user input word
     * @return {@code true} the word is valid with no numbers or special characters {@code false} the word includes numbers or special characters
     */

    public static boolean checkValid(String wordToCheck) {

        for (int i = 0; i < wordToCheck.length(); i++) {
            char c = wordToCheck.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * This method reads the inappropriateWords file to see whether the words input by the user are inappropriate
     *
     * @param wordToCheck user input
     * @return {@code true} if  there is no inappropriate words {@code false} if there is an inappropriate word
     */
    public static boolean checkInappropriate(String wordToCheck) {
        List<String> inappropriateWords = loadInappropriateWords();
        return !inappropriateWords.contains(wordToCheck);
    }


    /**
     * these are the methods which will be used for the game
     */

    public static void modeTwo() { //This method runs the 2nd mode - game mode
        if (getIntInput("\nYou have chosen to play the game. To confirm, press " + ANSI_CYAN + "1." + ANSI_RESET + " If you would like to change to the encrypt/decipher mode, press " + ANSI_PURPLE + "2." + ANSI_RESET) == 2) {
            modeOne();
        }
        String username = getUsername();
        if (rulesForGame(username) == 2) {
            determineIntention();
        } else {
            storyLine(username);
        }

    }

    /**
     * This allows the user to input a username used for the game
     *
     * @return String input of username
     */

    public static String getUsername() {
        String username = getStringInput("Please enter a username you would like to use for the game: ");
        while (getIntInput("Please confirm that you would like the username " + ANSI_BLUE + username + ANSI_RESET + " for the game by pressing " + ANSI_CYAN + "1" + ANSI_RESET + ". If not, press " + ANSI_PURPLE + "2" + ANSI_RESET + " to pick another name.") == 2) {
            username = getStringInput("Please enter a username you would like to use for the game: ");
        }
        return username;
    }

    /**
     * This displays the rules of the game
     *
     * @param username the user's username
     * @return 1 if the user accepts the rules, 2 if they do not
     */

    public static int rulesForGame(String username) {
        return getIntInput("Welcome to the game mode " + ANSI_CYAN + username + ANSI_RESET + "! \nThere are some rules for this game to give you the best experience possible.\n1. No inappropriate language should be used in this game. If it is used, an error will be returned.\n2. Do not enter words with symbols or numbers.\nPress " + ANSI_BLUE + "1" + ANSI_RESET + " to accept the rules. If you do not agree with the rules, press " + ANSI_PURPLE + "2" + ANSI_RESET);
    }

    /**
     * This is the general storyline for the game
     *
     * @param username player's username
     */

    public static void storyLine(String username) {
        System.out.println("Hello " + ANSI_CYAN + username + ANSI_RESET + ", welcome to my space ship! My name is Hokkaido, I'm the AI controlling this space ship, its nice to have you onboard.\n" +
                "Let me tell you something about this ship: everything on this ship is encrypted in a special code called \"Hokkaido's Cipher\". Ciphers are simply secret codes. You take a message and transform it using specific rules or patterns to make it look like gibberish. Then, only the people who knows the specific rules or patterns can decode the message. This is used" +
                "to help me keep the information on this ship safe, only the people who know the code word would be able to get into the system and understand how to get past the security barriers.");
        delayPrint();
        System.out.println("It seems that you are an experienced coder, I might need help on this ship someday, I think you will be a good fit! Here, I'll let you into the secret of this cipher. I'll let you know the secret of this cipher: " +
                "The secret word of this cipher is: " + ANSI_BLUE + " snowflake" + ANSI_RESET + ". The cipher is very simple, every three letters of the refers to one character of the secret word. To know which of the three letter it equals from the group, the number 1 - 3 will be coded respectively" +
                "\nFor example, the letters '" + ANSI_PURPLE + "a, b, c" + ANSI_RESET + "' are the first three letters of the alphabet. This would be encoded into the letter '" + ANSI_BLUE + "s1, s2, or s3" + ANSI_RESET + "' the first letter of the code word." +
                "\nThen, the next three letters '" + ANSI_CYAN + "d, e, f" + ANSI_RESET + "' are encoded into the letter '" + ANSI_BLUE + "n1, n2, n3" + ANSI_RESET + "', the second letter of the code word." +
                "\nIt goes on in the same pattern, every three letters is one letter from the code word! There is one exception, which is the last group of letters in the alphabet, '" + ANSI_YELLOW + "y, z" + ANSI_RESET + "'. Since there aren't enough letters to make 9 perfect groups, these two letters will represent the last letter of the code word.");
        delayPrint();
        String input = getStringInput("Why don't you try out using the code. Here, theres a door in front of you which allows you to access the elevators to the upper level of the space ship, why don't you try deciphering the word." +
                "\nThe encrypted word is: " + ANSI_BLUE + "n3l3f3a1a2e1" + ANSI_RESET + ". \nInput your answer here: "); //frosty is the answer

        while (comparingAnswers(input, "frosty")) {
            input = getStringInput(ANSI_RED + "Uh oh, it is the wrong answer. Try again: " + ANSI_RESET);
        }
        String inputTwo = getStringInput("Good job! You've entered the elevator. I want to show you the motherboard of the ship, its on level three. To get there, you need to encrypt the word " + ANSI_BLUE + "three" + ANSI_RESET + ": "); //answer is a2o2l3n2n2

        while (comparingAnswers(inputTwo, "a2o2l3n2n2")) {
            inputTwo = getStringInput(ANSI_RED + "Uh oh, it is the wrong answer. Try again: " + ANSI_RESET);
        }
        System.out.println("Perfect you've gotten a hang of it now!");
        delayPrint();
        System.out.println("uh oh... I think I'm glitching out... a hacker must've entered the system...");
        delayPrint();
        System.out.println("You need to help fix this, Yoshi is the hacker...she used to be a helper on this ship and knows the cipher too..., it'll be hard to beat her. Now that you're at the motherboard, you should be able to reset the system by encrypting and deciphering four sentences. However, the hacker is fast, so you only have 45 seconds to finish each task... hurry up!");
        System.out.println(ANSI_CYAN + ANSI_BOLD + "\nTask 1 - encrypt the sentence to access the motherboard: ");
        encryptWord("sentenceToEncrypt");
        System.out.println("Congrats you got into the motherboard system! Oh no, Yoshi sent you a message...");
        delayPrint();
        System.out.println(ANSI_YELLOW + ANSI_BOLD + "\nTask 2 - decipher this sentence: ");
        decipherWord("sentenceToDecipher");
        System.out.println("Yikes... You're almost there, right, you just need to encrypt this message to reset the system");
        delayPrint();
        System.out.println(ANSI_PURPLE + ANSI_BOLD + "\nTask 3 - encrypt this sentence: ");
        encryptWord("sentenceToEncrypt");
        System.out.println("Perfect, now you've reset the system, we should be good to go!");
        delayPrint();
        System.out.println("Oh look... one last message from Yoshi... I wonder what it says?");
        System.out.println(ANSI_GREEN + ANSI_BOLD + "\nTask 4 - decipher this sentence: ");
        decipherWord("sentenceToDecipher");
        System.out.println("Phew! Thanks to you the ship is safe again, you should stay with us, we'd love to have you onboard!");
        if (getIntInput("\nIf you would like to use this program again, press " + ANSI_BLUE + "1" + ANSI_RESET + ". If you would like to exit, press " + ANSI_YELLOW + "2" + ANSI_RESET) == 1) {
            determineIntention();
        } else {
            System.out.println("Thank you for using this program.");
            System.exit(1);
        }

    }

    public static int previousEncryptIndex = -1; //Variable used to store the index of the line used in the first task

    /**
     * This is the encrypting method for the game which reads sentences from the sentenceToEncrypt file
     *
     * @param filePath file to read
     */


    public static void encryptWord(String filePath) {
        List<String> list = readLinesFromFile(filePath);
        List<String> nonEncrypted = getOddNumberedLines(list);
        List<String> encrypted = getEvenNumberedLines(list);

        Random random = new Random();

        String randomLine;
        String answerLine;
        int randomIndex;

        do {
            randomIndex = random.nextInt(nonEncrypted.size());
        } while (randomIndex == previousEncryptIndex);
        previousEncryptIndex = randomIndex;

        randomLine = nonEncrypted.get(randomIndex);
        answerLine = encrypted.get(randomIndex);
        System.out.println(ANSI_BLUE + randomLine + ANSI_RESET);

        boolean userInput = promptForAnswer("Enter your answer: ", answerLine, 60);
        System.out.println(userInput ? ANSI_GREEN + "Correct!" + ANSI_RESET : ANSI_RED + "You have ran out of time.");
    }


    private static int previousDecipherIndex = -1; //Variable used to store the index of the line used in the first task

    /**
     * This is the decipher method for the game which reads sentences from the sentenceToEncrypt file
     *
     * @param filePath file to read from
     */

    public static void decipherWord(String filePath) {
        List<String> list = readLinesFromFile(filePath);
        List<String> nonEncrypted = getOddNumberedLines(list);
        List<String> encrypted = getEvenNumberedLines(list);

        Random random = new Random();

        String randomLine;
        String answerLine;
        int randomIndex;

        do {
            randomIndex = random.nextInt(nonEncrypted.size());
        } while (randomIndex == previousDecipherIndex);
        previousDecipherIndex = randomIndex;

        randomLine = nonEncrypted.get(randomIndex);
        answerLine = encrypted.get(randomIndex);
        System.out.println(ANSI_BLUE + randomLine + ANSI_RESET);

        boolean userInput = promptForAnswer("Enter your answer: ", answerLine, 60);
        System.out.println(userInput ? ANSI_GREEN + "Correct!" + ANSI_RESET : ANSI_RED + "You have ran out of time.");

    }


    public static List<String> readLinesFromFile(String filePath) { //Reads lines from the file
        List<String> lines = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return lines;
    }


    public static List<String> getOddNumberedLines(List<String> lines) { //Reads a file and stores only the odd numbered lines
        List<String> oddLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 2) {
            oddLines.add(lines.get(i));
        }

        return oddLines;
    }

    public static List<String> getEvenNumberedLines(List<String> lines) { //Reads a file and stores only the even numbered lines
        List<String> evenLines = new ArrayList<>();

        for (int i = 1; i < lines.size(); i += 2) {
            evenLines.add(lines.get(i));
        }

        return evenLines;
    }

    /**
     * Prompts the user for an answer, giving them as many attempts as they like to answer, however they must answer
     * within the time limit.
     *
     * @param prompt  the message prompt.
     * @param answer  the expected answer.
     * @param seconds the time limit in seconds.
     * @return {@code true} if they answered correctly, {@code false} if they ran out of time.
     */
    private static boolean promptForAnswer(String prompt, String answer, int seconds) {
        System.out.println(prompt);
        try {
            CompletableFuture.supplyAsync(() -> getCorrectAnswer(answer))
                    .get(seconds, TimeUnit.SECONDS);
            return true;
        } catch (ExecutionException | TimeoutException | InterruptedException ex) {
            return false;
        }
    }

    private static boolean getCorrectAnswer(String expectedAnswer) {
        Scanner in = new Scanner(System.in);
        while (true) {
            String answer = in.nextLine();
            if (answer.equals(expectedAnswer)) {
                return true;
            } else {
                System.out.println("Incorrect! Try again!");
            }
        }
    }


    public static void delayPrint() { //This method is used to delay the printing of the next line to allow the user enough time to read the message.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean comparingAnswers(String input, String answer) { //This will be used to compare the answers from the user input to the correct answer
        return !input.equals(answer);
    }


}









