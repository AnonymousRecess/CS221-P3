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
		int oneCounter = 0;
		int twoCounter = 0;
		fileScan = new Scanner(new File(filename));

		// TODO: parse the given file to populate the char[][]
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any formatting or parsing issues are
		// encountered
		String rowCol = fileScan.nextLine(); // Store the first line of the file as a string
		Scanner rowColScan = new Scanner(rowCol); //Create a scanner to parse the first line of the file 
		if (!rowColScan.hasNextInt()) {
			String wrong = rowColScan.next();
			rowColScan.close();
			fileScan.close();
			throw new InvalidFileFormatException(wrong + " is not a valid component");
		}
		ROWS = rowColScan.nextInt(); //Assign the first parsed value as the number of rows
		if (!rowColScan.hasNextInt()) {
			String wrong = rowColScan.next();
			rowColScan.close();
			fileScan.close();
			throw new InvalidFileFormatException(wrong + " is not a valid component");
		}
		COLS = rowColScan.nextInt(); //Assign the second parsed value as the number of columns
		if(rowColScan.hasNext()) //Throw dimension error if first string has more than the two values 
		{ 
			rowColScan.close();
			fileScan.close();
			throw new InvalidFileFormatException("Incorrect dimensions");
		}
		
		int rowCount = 0;
		int i = -1;
		
		
	//	fileScan.nextLine();

		board = new char[ROWS][COLS];
		while (fileScan.hasNext()) {
			rowCount++;
			if(rowCount > ROWS)
			{
				fileScan.close();
				rowColScan.close();
				throw new InvalidFileFormatException("Incorrect row number");
			}
			rowColScan.close();
			i++;
			int j = 0;
			int colCount = 0;
			
			boolean illegal = true;
			int s = 0;
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			while (lineScan.hasNext()) {
				char a = lineScan.next().charAt(0);
				colCount++;

				while (s <= ALLOWED_CHARS.length() - 1 && illegal == true) {
					if (a == ALLOWED_CHARS.charAt(s)) {
						illegal = false;
					}
					s++;
				}
				if (illegal) {
					lineScan.close();
					fileScan.close();
					throw new InvalidFileFormatException(a + " is not a valid component");
				}
				if (a == START) {
					oneCounter++;
					startingPoint = new Point(i, j);
					if (oneCounter > 1) {
						fileScan.close();
						lineScan.close();
						throw new InvalidFileFormatException("Cannot have multiple starting components on one board");
					}
					if (oneCounter < 1) {
						fileScan.close();
						lineScan.close();
						throw new InvalidFileFormatException("File must have a starting component");
					}
				}
				if (a == END) {
					endingPoint = new Point(i, j);
					twoCounter++;
					if (twoCounter > 1) {
						fileScan.close();
						lineScan.close();
						throw new InvalidFileFormatException("Cannot have multiple ending components on one board");
					}
					if (twoCounter < 1) {
						fileScan.close();
						lineScan.close();
						throw new InvalidFileFormatException("File must have a ending component");
					}
				}
				
				if(colCount > COLS)
				{
					fileScan.close();
					lineScan.close();
					throw new InvalidFileFormatException("Incorrect column number");
				}
				board[i][j] = a;
				j++;
				
			}
			if(colCount != COLS)
			{
				fileScan.close();
				lineScan.close();
				throw new InvalidFileFormatException("Incorrect column number");
			}
			lineScan.close();
		}
		
		if (oneCounter < 1 || twoCounter < 1) {
			fileScan.close();
			throw new InvalidFileFormatException("Missing starting or ending point");
		}

		
		fileScan.close();

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