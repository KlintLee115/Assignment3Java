package appDomain;

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Comparable<Word>, Serializable {

    private static final long serialVersionUID = 5248877746679030444L;

    private String word;
    private String filename;
    private ArrayList<Integer> lines;
    private int frequency;

    public Word(String word, String filename, int lineNumber) {
        this.word = word;
        this.filename = filename;
        this.lines = new ArrayList();
        this.lines.add(lineNumber);
        this.frequency = 1;
    }

    public void addLine(int lineNumber) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i) != lineNumber) {
                lines.add(lineNumber);
            }
        }
    }

    public void increaseFrequency() {
        frequency = frequency + 1;
    }

    public String getWord() {
        return word;
    }

    public String getFilename() {
        return filename;
    }

    public String toString_pf() {
        return "===" + word + "=== found in file: " + filename;
    }

    public String toString_pl() {
        return "===" + word + "=== found in file: " + filename + " +  on lines: " + lines.toString();
    }
    
    public String toString_po() {
        return "===" + word + "=== number of entries: " + frequency + " found in file: " + filename + " +  on lines: " + lines.toString();
    }

    @Override
    public String toString() {
        return "===" + word + "=== number of entries: " + frequency + " found in file: " + filename + " +  on lines: " + lines.toString();
    }

    @Override
    public int compareTo(Word wordObj) {
        int difference = this.word.compareTo(wordObj.word);
        if (difference == 0) {
            difference = this.filename.compareTo(wordObj.filename);
            if (difference == 0) {
                return 0;
            } else if (difference == 1) {
                return 1;
            }
            return -1;
        } else if (difference == 1) {
            return 1;
        }
        return -1;
    }

}
