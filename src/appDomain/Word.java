package appDomain;

import java.util.ArrayList;

public class Word implements Comparable<Word> {

    private String word;
    private String filename;
    private ArrayList<Integer> lines;

    public Word(String word, String filename, int lineNumber) {
        this.word = word;
        this.filename = filename;
        this.lines = new ArrayList();
        this.lines.add(lineNumber);
    }

    public void addLine(int lineNumber) {
        lines.add(lineNumber);
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "===" + word + "=== found in file: " + filename + " +  on lines: " + lines.toString();
	}

    @Override
    public int compareTo(Word wordObj) {
        //Not completed.
        int difference = this.word.compareTo(wordObj.word);
        if (difference == 0){
            return 0;
        } else if (difference == 1){
            return 1;
        }
        return -1;
    }

}
