package appDomain;

import implementations.BSTree;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import utilities.Iterator;

/**
 * WordTracker is responsible for processing a text file, tracking word
 * occurrences, and constructing a binary search tree to manage words and their
 * metadata. It supports serialization to persist the tree structure and
 * retrieval upon restart.
 */
public class WordTracker implements Serializable {

    // Iterator for in-order traversal
    private static Iterator Iterator;

    private Scanner fileReader; // Scanner to read input files
    private String filename;    // Name of the file being processed
    private ArrayList<Word> Words; // Stores words and their metadata

    /**
     * Constructor to initialize the WordTracker with a file to read. Attempts
     * to locate the file in multiple possible paths.
     *
     * @param textFile The name of the text file to process
     * @throws FileNotFoundException If the file cannot be found in the
     * specified paths
     */
    public WordTracker(String textFile) throws FileNotFoundException {
        //Getting file path
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        // Go up one directory from dist to find the project's res folder
        String a = s + "\\..\\res\\" + textFile;
        System.out.println("Attempting to read file from: " + a);

        File filePath = new File(a);

        if (!filePath.exists()) {
            System.out.println("File not found. Checking alternate path...");
            // Try alternate path
            a = s + "\\res\\" + textFile;
            System.out.println("Trying: " + a);
            filePath = new File(a);
        }

        this.fileReader = new Scanner(filePath);
        this.filename = textFile;
        this.Words = new ArrayList();

    }

    /**
     * Compares the provided word with existing words in the list. If a match is
     * found, updates the frequency and line numbers.
     *
     * @param currentWord The word to compare
     * @param lineNumber The line number where the word appears
     * @param filename The name of the file being processed
     * @return true if the word exists and is updated, false otherwise
     */
    public boolean compareWords(String currentWord, int lineNumber, String filename) {
        if (Words == null) {
            return false;
        }
        for (Word word : Words) {
            String newWord = currentWord.toLowerCase();
            String wordInList = word.getWord().toLowerCase();
            int difference = newWord.compareTo(wordInList);
            if (difference == 0) {
                difference = filename.compareTo(word.getFilename());
                if (difference == 0) {
                    word.addLine(lineNumber);
                    word.increaseFrequency();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a new word to the list if it does not already exist.
     *
     * @param word The word to add
     * @param filename The file name where the word appears
     * @param lineNumber The line number where the word appears
     */
    private void addWord(String word, String filename, int lineNumber) {
        boolean wordExists = compareWords(word, lineNumber, filename);
        if (!wordExists) {
            Word newWord = new Word(word, filename, lineNumber);
            Words.add(newWord);
        }
    }

    /**
     * Reads the input file line by line, splits lines into words, and adds them
     * to the word list.
     *
     * @return ArrayList of Word objects containing word metadata
     */
    public ArrayList<Word> readFile() {
        int lineNumber = 1;
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            String[] wordList = line.split("\\s+|(?<!\\w)'|'(?!\\w)|(?<=\\w)[,.;?!](?=\\s|$)");

            for (String word : wordList) {
                if (word != null) {
                    addWord(word, filename, lineNumber);
                }
            }
            lineNumber++;
        }
        return Words;
    }

    /**
     * Builds the binary search tree with words from the text file. Reads from a
     * repository if it exists, or starts a new tree otherwise.
     *
     * @param tree The BSTree to populate
     * @param tracker The WordTracker object to read words from
     * @param option The output format option
     * @throws IOException If an I/O error occurs
     * @throws ClassNotFoundException If deserialization fails
     */
    public static void buildBinarySearchTree(BSTree tree, WordTracker tracker, String option) throws IOException, ClassNotFoundException {
        ArrayList<Word> array = new ArrayList();
        array = tracker.readFile();

        if (repoExists() == false) {
            System.out.println("*--No Repository Found--*");
            System.out.println("");
            for (Word word : array) {
                tree.add(word);
            }
        } else {
            System.out.println("*--Repository Exists--*");
            System.out.println("");
            tree = deserializeTree();
            for (Word word : array) {
                tree.add(word);
            }
        }

        // Serialize the tree for future use
        FileOutputStream fileOut = new FileOutputStream("repository.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(tree);

        out.close();
        fileOut.close();

        // Display the tree in the specified format
        System.out.println("---Results---");
        System.out.println("");
        String[] s = option.split("\\W");
        System.out.println("Writing " + s[1] + " format");

        tree.inorderIterator();
        Iterator inOrderList = tree.inorderIterator();
        while (inOrderList.hasNext()) {
            Word word = (Word) inOrderList.next();
            System.out.println(word.toString(option));
        }
        System.out.println("---Tree Complete---");
    }

    /**
     * Checks if the serialized repository exists.
     *
     * @return true if the repository file exists, false otherwise
     * @throws IOException If an I/O error occurs
     */
    public static boolean repoExists() throws IOException {
        try {
            FileInputStream fileIn = new FileInputStream("repository.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            fileIn.close();
            in.close();
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    /**
     * Deserializes the binary search tree from the repository file.
     *
     * @return The deserialized BSTree object
     * @throws IOException If an I/O error occurs
     * @throws ClassNotFoundException If deserialization fails
     */
    public static BSTree deserializeTree() throws IOException, ClassNotFoundException {
        BSTree tree = null;
        FileInputStream fileIn = new FileInputStream("repository.ser");

        ObjectInputStream in = new ObjectInputStream(fileIn);

        tree = (BSTree) in.readObject();

        in.close();
        fileIn.close();

        return tree;
    }

    /**
     * Entry point for the WordTracker application. Parses command-line
     * arguments and initiates processing.
     *
     * @param args Command-line arguments
     * @throws IOException If an I/O error occurs
     * @throws ClassNotFoundException If deserialization fails
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Check if minimum required arguments are provided
        if (args.length < 2) {
            System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f <output.txt>]");
            return;
        }

        String fileName = args[0];         // First argument is the input file
        String sortOption = args[1];       // Second argument is the print option
        String outputFile = null;          // Optional output file

        // Check for output file option
        if (args.length >= 4 && args[2].equals("-f")) {
            outputFile = args[3];
            // Add this: Redirect System.out to the file
            PrintStream fileOut = new PrintStream(new FileOutputStream(outputFile));
            System.setOut(fileOut);
        }

        System.out.println("Filename is: " + fileName);

        WordTracker tracker = null;
        try {
            tracker = new WordTracker(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }

        BSTree tree = new BSTree();
        buildBinarySearchTree(tree, tracker, sortOption);

    }

}
