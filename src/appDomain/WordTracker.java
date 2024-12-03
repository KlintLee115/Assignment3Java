package appDomain;

import implementations.BSTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class WordTracker{

    private Scanner fileReader;
    private String filename;
    private ArrayList<Word> Words;

    public WordTracker(String textFile) throws FileNotFoundException {
        //Getting file path
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String a = s + "\\res/" + textFile;
        System.out.println(a);

        File filePath = new File(a);

        this.fileReader = new Scanner(filePath);
        this.filename = textFile;
        this.Words = new ArrayList();

    }

    public boolean compareWords(String currentWord) {
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
                //System.out.println("===Duplicate spotted===");
                return true;
            }
        }
        return false;
    }

    public void addLineToWord(String currentWord, int lineNumber) {
        for (Word word : Words) {
            String newWord = currentWord.toLowerCase();
            String wordInList = word.getWord().toLowerCase();
            int difference = newWord.compareTo(word.getWord().toLowerCase());
            if (difference == 0) {
                word.addLine(lineNumber);
                //System.out.println("---" + word.getWord() + " spotted at line: " + lineNumber + "---");
            }
        }
    }

    public ArrayList<Word> readFile() {
        int lineNumber = 1;
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            String[] wordList = line.split("\\s+|(?<!\\w)'|'(?!\\w)|(?<=\\w)[,.;?!](?=\\s|$)");

            for (String word : wordList) {
                if (word != null) {
                    //System.out.println("==="+word+"===");
                    boolean wordExists = compareWords(word);
                    //System.out.println("Word Exists? : " + wordExists);
                    if (wordExists) {
                        addLineToWord(word, lineNumber);
                    } else {
                        Word newWord = new Word(word, filename, lineNumber);
                        Words.add(newWord);
                        //System.out.println("Added: "+newWord);
                    }
                }
            }
            lineNumber++;
        }
        return Words;
    }
    
    
    
    
    public static void buildBinarySearchTree(BSTree tree, WordTracker tracker){
        ArrayList<Word> array = new ArrayList();
        array = tracker.readFile();
        for (Word word : array){
            tree.add(word);
        }  
    }
    
    public static void listWords(WordTracker tracker){
        ArrayList<Word> array = new ArrayList();
        array = tracker.readFile();
        for (Word word : array){
            System.out.println(word);
            
        }
        System.out.println("---Listing Complete---");
    }
    
    
    public static void main(String[] args) {
        
        
        String fileName = "simpleTest.txt";
        //String sortOption = args[5];
        //String outputFile = args[6];
        
        WordTracker tracker = null;
        try {
            tracker = new WordTracker(fileName);
            listWords(tracker);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        //BSTree tree = new BSTree();
        //buildBinarySearchTree(tree, tracker);
        
        
        
    }

}
