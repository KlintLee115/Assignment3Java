package appDomain;

import implementations.BSTree;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class WordTracker implements Serializable {

    private Scanner fileReader;
    private String filename;
    private ArrayList<Word> Words;

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

    //Function to add to the frequency and lines if they're the same
    public boolean compareWords(String currentWord, int lineNumber, String filename) {
        if (Words == null) {
            return false;
        }
        for (Word word : Words) {
            String newWord = currentWord.toLowerCase();
            String wordInList = word.getWord().toLowerCase();
            int difference = newWord.compareTo(wordInList);
            //System.out.println("Word in list: " + wordInList);
            //System.out.println("New Word: " + newWord);
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

    //Checks if the word already exists before adding
    private void addWord(String word, String filename, int lineNumber) {
        boolean wordExists = compareWords(word, lineNumber, filename);
        //System.out.println("Word Exists? : " + wordExists);
        if (!wordExists) {
            Word newWord = new Word(word, filename, lineNumber);
            Words.add(newWord);
        }
    }

    //Reads file and adds all the words to the arraylist
    public ArrayList<Word> readFile() {
        int lineNumber = 1;
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            String[] wordList = line.split("\\s+|(?<!\\w)'|'(?!\\w)|(?<=\\w)[,.;?!](?=\\s|$)");

            for (String word : wordList) {
                if (word != null || word != "") {
                    //System.out.println("==="+word+"===");
                    addWord(word, filename, lineNumber);
                }
            }
            lineNumber++;
        }
        return Words;
    }

    //Main
    public static void buildBinarySearchTree(BSTree tree, WordTracker tracker) throws IOException, ClassNotFoundException {
        ArrayList<Word> array = new ArrayList();
        array = tracker.readFile();

        if (repoExists() == false) {
            for (Word word : array) {
                tree.add(word);
            }
        } else {
            tree = deserializeTree();
            for (Word word : array) {
                tree.add(word);
            }
        }

        FileOutputStream fileOut = new FileOutputStream("src/repository.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(tree);

        out.close();
        fileOut.close();

        System.out.println("---Tree Complete---");

        System.out.println("---Results---");
        System.out.println(tree);

    }

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

    public static BSTree deserializeTree() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("repository.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);

        BSTree tree = (BSTree) in.readObject();

        in.close();
        fileIn.close();

        return tree;
    }

    //A function to test the functionality of the tracker and the serialization
    public static void listWords(WordTracker tracker, String option) throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<Word> repoArray = new ArrayList();
        ArrayList<Word> newArray = new ArrayList();

        newArray = tracker.readFile();

        if (repoExists() == true) {
            repoArray = deserializeWords(repoArray);
            //System.out.println("-- This deserializaed array has: " + repoArray + " --");

            //adds new words to the existing list
            int repoArraySize = repoArray.size();
            int newArraySize = newArray.size();

            try {
                for (int i = 0; i < repoArraySize; i++) {
                    for (int j = 0; j < newArraySize; j++) {
                        Word repoWord = repoArray.get(i);
                        Word newWord = newArray.get(j);

                        //For objects with the same name but different files.
                        int difference = repoWord.compareTo(newWord);
                        if (difference != 0) {

                            //For objects with different names but the same files.
                            difference = repoWord.getFilename().compareTo(newWord.getFilename());
                            if (difference != 0) {
                                repoArray.add(newWord);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        } else {
            for (Word newWord : newArray) {
                repoArray.add(newWord);
                //System.out.println(newWord);
            }
        }

        System.out.println("--------Results--------");
        for (Word repoWord : repoArray) {
            switch (option) {
                case "-pf":
                    System.out.println(repoWord.toString_pf());
                    break;
                case "-pl":
                    System.out.println(repoWord.toString_pl());
                    break;
                case "-po":
                    System.out.println(repoWord);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        FileOutputStream fileOut = new FileOutputStream("repository.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(repoArray);

        out.close();
        fileOut.close();

        System.out.println("---Listing Complete---");
    }

    public static ArrayList<Word> deserializeWords(ArrayList<Word> array) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("repository.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);

        array = (ArrayList<Word>) in.readObject();

        in.close();
        fileIn.close();

        return array;
    }

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
        }

        System.out.println("Filename is: " + fileName);

        WordTracker tracker = null;
        try {
            tracker = new WordTracker(fileName);
            listWords(tracker, sortOption);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }
    }

}
