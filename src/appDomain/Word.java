package appDomain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a word found in a file, along with metadata such as its frequency
 * and the lines on which it appears. Implements the Comparable interface to
 * allow comparison of Word objects based on their word content and filenames.
 */
public class Word implements Comparable<Word>, Serializable {

    private static final long serialVersionUID = 5248877746679030444L;

    private String word; // The word content
    private String filename; // The filename where the word is found
    private ArrayList<Integer> lines; // List of line numbers where the word appears
    private int frequency; // Number of times the word appears in the file

    /**
     * Constructs a Word object with the given word, filename, and line number.
     * Initializes the frequency to 1 and adds the initial line number to the
     * list.
     *
     * @param word the word content
     * @param filename the filename where the word is found
     * @param lineNumber the line number where the word is located
     */
    public Word(String word, String filename, int lineNumber) {
        this.word = word;
        this.filename = filename;
        this.lines = new ArrayList();
        this.lines.add(lineNumber);
        this.frequency = 1;
    }

    /**
     * Adds a line number to the list of lines where the word appears. Ensures
     * that duplicate line numbers are not added.
     *
     * @param lineNumber the line number to add
     */
    public void addLine(int lineNumber) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i) != lineNumber) {
                lines.add(lineNumber);
            }
        }
    }

    /**
     * Increases the frequency count of the word by one.
     */
    public void increaseFrequency() {
        frequency = frequency + 1;
    }

    /**
     * Retrieves the word content.
     *
     * @return the word content as a string
     */
    public String getWord() {
        return word;
    }

    /**
     * Retrieves the filename where the word is found.
     *
     * @return the filename as a string
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Converts the Word object to a string representation based on the
     * specified option. The output format depends on the option provided.
     *
     * @param option the formatting option: "-pf", "-pl", or "-po"
     * @return a string representation of the Word object
     */
    public String toString(String option) {
        switch (option) {
            case "-pf":
                return "Key: ===" + word + "=== found in file: " + filename;
            case "-pl":
                return "Key: ===" + word + "=== found in file: " + filename + " +  on lines: " + lines.toString();
            case "-po":
                return "Key: ===" + word + "=== number of entries: " + frequency + " found in file: " + filename + " +  on lines: " + lines.toString();
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns the default string representation of the Word object. Includes
     * the word, its frequency, the filename, and the line numbers.
     *
     * @return the default string representation
     */
    @Override
    public String toString() {
        return "===" + word + "=== number of entries: " + frequency + " found in file: " + filename + " +  on lines: " + lines.toString();
    }

    /**
     * Compares this Word object to another based on the word content and
     * filename. Comparison is case-insensitive for the word content, and
     * secondary comparison is based on the filename in a case-sensitive manner.
     *
     * @param wordObj the Word object to compare against
     * @return -1 if this Word comes before the other, 1 if after, 0 if equal
     */
    @Override
    public int compareTo(Word wordObj) {
        String a = this.word.toLowerCase();
        String b = wordObj.word.toLowerCase();
        int difference = a.compareTo(b);
        if (difference == 0) {
            difference = this.filename.compareTo(wordObj.filename);
            if (difference == 0) {
                return 0;
            } else if (difference > 0) {
                return 1;
            } else {
                return -1;
            }

        } else if (difference > 0) {
            return 1;
        } else {
            return -1;
        }

    }
}
