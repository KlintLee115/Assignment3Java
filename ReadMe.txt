
Here’s a comprehensive readMe.txt file for Assignment 3 Binary Search Tree Implementation and
Word Tracker:


Assignment 3: Binary Search Tree Implementation and
Word Tracker


Overview
	This project implements a word tracker to read every word in a given text file and stored in a custom binary search tree. The tree is then serialized for the next time the program runs again and retrives the data.

	This document provides installation, usage instructions, and project details for using this program.


Usage Instructions
1. Navigate to the Directory:

	Open a terminal/command prompt.
	Change to the directory where JavaProject3.jar is located.
	
2. Run the Parser:

	Execute the following command:

	java -jar JavaProject3.jar <input.txt> -pf/-pl/-po [-f output.txt]

	Replace <input.txt> with the name of the text file you want the program to read.
	Select one of the three sorting options to set how the program will dispay the list of words:

	-pf :	Alphabetical order with the text file each came from.
	-pl	:	Alphabetical order with the text file each came from, along with the lines they appeared in the file. 
	-po	:	Alphabetical order with the text file each came from, along with the amount of times the word shows up and lines they appeared in the file.

	"-f output.txt" is an optional command which instructs the program to export a text file of the results.



Examples:

	1. If "simpleTest.txt" is the desired text file to be read with only the words and the file source:
		java -jar Parser.jar simpleTest.txt -pf

	2. If you want "otherTest.txt" to export an output of the result with all the frequencies and lines:
		java -jar Parser.jar otherTest.txt -po -f output.txt

3. Output:

	The program will take the users inputs at the command line, search and read the text file, build and story a binary search tree off of the words, and print the results at the end.

	Example:
	Key: ===again...=== number of entries: 1 found in file: otherTest.txt +  on lines: [1]
	Key: ===Hello=== number of entries: 2 found in file: otherTest.txt +  on lines: [1, 2]
	Key: ===is=== number of entries: 1 found in file: otherTest.txt +  on lines: [1]....


Features
1. Binary Search Tree:

	A hierarchical data structure of nodes containing the words from the text file.

2. Serialization:

	Stores the tree into a file called repository.ser that can be deserialized to retrive the tree to add on to it with the next file.

3. Command-line Execution:

	Can declare filename, display option, and whether to export a result file directly from the command line.