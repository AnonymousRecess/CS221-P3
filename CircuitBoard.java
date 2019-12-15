import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 * 
 * @author mvail
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	// constants you may find useful
	private final int ROWS; // initialized in constructor
	private final int COLS; // initialized in constructor
	private final char OPEN = 'O'; // capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/**
	 * Construct a CircuitBoard from a given board input file, where the first line
	 * contains the number of rows and columns as ints and each subsequent line is
	 * one row of characters representing the contents of that position. Valid
	 * characters are as follows: 'O' an open position 'X' an occupied, unavailable
	 * position '1' first of two components needing to be connected '2' second of
	 * two components needing to be connected 'T' is not expected in input files -
	 * represents part of the trace connecting components 1 and 2 in the solution
	 * 
	 * @param filename file containing a grid of characters
	 * @throws FileNotFoundException      if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that
	 *                                    prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException, InvalidFileFormatException {
		Scanner fileScan;
		int oneCounter = 0; // counter for number of starting components on board
		int twoCounter = 0; // counter for number of ending components on board
		fileScan = new Scanner(new File(filename)); // scanner for the file containing the board information

		
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any formatting or parsing issues are
		// encountered
		String rowCol = fileScan.nextLine(); // Store the first line of the file as a string
		Scanner rowColScan = new Scanner(rowCol); //Create a scanner to parse the first line of the file 
		if (!rowColScan.hasNextInt()) { // check to see if next token is not an int
			String wrong = rowColScan.next(); // char that isn't an int
			rowColScan.close(); //close files
			fileScan.close(); // close files
			throw new InvalidFileFormatException(wrong + " is not a valid component"); // error message for incorrect component
		}
		ROWS = rowColScan.nextInt(); //Assign the first parsed value as the number of rows
		if (!rowColScan.hasNextInt()) {
			String wrong = rowColScan.next();
			rowColScan.close(); // close files
			fileScan.close(); // close files
			throw new InvalidFileFormatException(wrong + " is not a valid component"); // error message for incorrect component
		}
		COLS = rowColScan.nextInt(); //Assign the second parsed value as the number of columns
		if(rowColScan.hasNext()) //Throw dimension error if first string has more than the two values 
		{ 
			rowColScan.close(); // close files
			fileScan.close(); // close files
			throw new InvalidFileFormatException("Incorrect dimensions");
		}
		
		int rowCount = 0; // counter for checking number of scanned rows
		int i = -1; // counter for storing in array
		
		
	

		board = new char[ROWS][COLS]; // board array of characters
		while (fileScan.hasNext()) { // scan while there is another row
			rowCount++; // increase row count
			if(rowCount > ROWS) // check if too many rows
			{
				fileScan.close(); // close files
				rowColScan.close(); // close files
				throw new InvalidFileFormatException("Incorrect row number");
			}
			rowColScan.close(); // close files
			i++; // increment row counter for array
			int j = 0; // column counter for array
			int colCount = 0; // column counter
			
			boolean illegal = true; // boolean flag for testing scanned char
			int s = 0; // counter for looping through allowed chars
			String line = fileScan.nextLine(); // line to scan
			Scanner lineScan = new Scanner(line); // scanner to scan each token in the line
			while (lineScan.hasNext()) {
				char a = lineScan.next().charAt(0); // scanned in token
				colCount++; // increment count of column

				while (s <= ALLOWED_CHARS.length() - 1 && illegal == true) {
					if (a == ALLOWED_CHARS.charAt(s)) {
						illegal = false; // set flag to false when the character passes the valid char test
					}
					s++; //check next valid char 
				}
				if (illegal) {
					lineScan.close(); // close files
					fileScan.close(); // close files
					throw new InvalidFileFormatException(a + " is not a valid component"); 
				}
				if (a == START) {
					oneCounter++; // increment counter for starting components found
					startingPoint = new Point(i, j); // set starting point to starting component
					if (oneCounter > 1) { // too many starting components
						fileScan.close(); // close files
						lineScan.close(); // close files
						throw new InvalidFileFormatException("Cannot have multiple starting components on one board");
					}
					if (oneCounter < 1) { // no starting components
						fileScan.close(); // close files
						lineScan.close(); // close files
						throw new InvalidFileFormatException("File must have a starting component");
					}
				}
				if (a == END) {
					endingPoint = new Point(i, j);
					twoCounter++; // increment counter for ending components found
					if (twoCounter > 1) { // too many ending components
						fileScan.close(); // close files
						lineScan.close(); // close files
						throw new InvalidFileFormatException("Cannot have multiple ending components on one board");
					}
					if (twoCounter < 1) { // no ending components
						fileScan.close(); // close files
						lineScan.close(); // close files
						throw new InvalidFileFormatException("File must have a ending component");
					}
				}
				
				if(colCount > COLS) // too many columns
				{
					fileScan.close(); // close files
					lineScan.close(); // close files
					throw new InvalidFileFormatException("Incorrect column number");
				}
				board[i][j] = a; // populate board with scanned value
				j++; // increment column counter
				
			}
			if(colCount != COLS) // too few columns
			{
				fileScan.close(); // close files
				lineScan.close(); // close files
				throw new InvalidFileFormatException("Incorrect column number");
			}
			lineScan.close(); // close files
		}
		
		if (oneCounter < 1 || twoCounter < 1) { // no starting or ending component
			fileScan.close(); // close files
			throw new InvalidFileFormatException("Missing starting or ending point");
		}

		
		fileScan.close(); // close files

	}

	/**
	 * Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/**
	 * utility method for copy constructor
	 * 
	 * @return copy of board array
	 */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}

	/**
	 * Return the char at board position x,y
	 * 
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}

	/**
	 * Return whether given board position is open
	 * 
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}

	/**
	 * Set given position to be a 'T'
	 * 
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}

	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}

	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}

	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}

	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}

}// class CircuitBoard