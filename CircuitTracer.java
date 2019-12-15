import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {
	List<TraceState> bestPaths = new ArrayList<TraceState>(); //NNEEEEEDSS CHANGEEEEEEEED
	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		
		if (args.length != 3) { // expects 3 arguments
			printUsage(); // message displaying use information
			System.exit(1);
		}
		try {
			new CircuitTracer(args); //create this with args
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		
		
		System.out.println("This program expects three command-line arguments, in the following order:"
				+ "\n -q for storing as a queue, -c for console output, and the input file name.");
	}
	private void search(Storage<TraceState> stateStore, CircuitBoard board)
	{
		int startingRow = board.getStartingPoint().x; // position of starting component
		int startingCol = board.getStartingPoint().y; // position of starting component
		
			if(board.isOpen(startingRow+1,startingCol)) // check if below component is open
			{
				TraceState initialState = new TraceState(board, startingRow+1, startingCol);
				stateStore.store(initialState); // make and store trace if open
			}
			if(board.isOpen(startingRow,startingCol-1)) // check if left component is open
			{
				TraceState verticalTrace = new TraceState(board,startingRow, startingCol-1);
				stateStore.store(verticalTrace); // make and store trace if open
			}
			if(board.isOpen(startingRow, startingCol+1)) // check if right component is open
			{
				TraceState verticalTrace = new TraceState(board,startingRow, startingCol+1);
				stateStore.store(verticalTrace); // make and store trace if open
			}
			if(board.isOpen(startingRow-1,startingCol)) // check if above component is open
			{
				TraceState horizontalTrace = new TraceState(board,startingRow-1,startingCol);
				stateStore.store(horizontalTrace); // make and store trace if open
			}
		int pathMin = 0; // int to keep track of minimum path length
		while(!stateStore.isEmpty())
		{
			
			TraceState current = stateStore.retrieve(); // retrieve traces while stateStore is not empty
			if(bestPaths.isEmpty() && current.isComplete()) // if tracestate is complete and there is no best path, this is the new best path
			{
				pathMin = current.pathLength(); // set min to new trace length
				bestPaths.add(current); // add to bestPath list
			}
			else if(current.isComplete() && current.pathLength() == pathMin)
			{
				bestPaths.add(current); // if the path is the same length as current mid, add as an additional best path
			}
			else if(current.isComplete() && current.pathLength() < pathMin)
			{
				pathMin = current.pathLength(); // clear bestpath if new minPath is found and add new tracestate to bestpath
				bestPaths.clear();
				bestPaths.add(current);
			}
			else
			{
				int currentRow = current.getRow(); // new position to check adjacent positions from
				int currentCol = current.getCol(); // new position to check adjacent positions from
				if(current.isOpen(currentRow, currentCol+1)) // check if right component is open
				{
					TraceState verticalTrace = new TraceState(current,currentRow, currentCol+1);
					stateStore.store(verticalTrace); // make and store trace if open
				}
				if(current.isOpen(currentRow, currentCol-1)) // check if left component is open
				{
					TraceState verticalTrace = new TraceState(current,currentRow, currentCol-1);
					stateStore.store(verticalTrace); // make and store trace if open
				}
				if(current.isOpen(currentRow+1, currentCol)) // check if below component is open
				{
					TraceState verticalTrace = new TraceState(current,currentRow+1, currentCol);
					stateStore.store(verticalTrace); // make and store trace if open
				}
				if(current.isOpen(currentRow-1, currentCol)) // check if above component is open
				{
					TraceState verticalTrace = new TraceState(current,currentRow-1, currentCol);
					stateStore.store(verticalTrace); // make and store trace if open
				}
			} 
		}
	}
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	private CircuitTracer(String[] args) {
		
		
		if(args[0].contains("s"))
		{
			Storage<TraceState> stateStore = Storage.getStackInstance(); // configure storage as a stack
			try {
			CircuitBoard board = new CircuitBoard(args[2]); // create a board with filename sent as a parameter
			search(stateStore, board); // perform search
			}
			catch (FileNotFoundException e)
			{
				System.out.println("Incorrect FilePath\n" + "e");
			}
			catch (InvalidFileFormatException e)
			{
				System.out.println(e);
			}
			if(args[1].contains("c"))
			{
				for(TraceState states: bestPaths)
				{
					System.out.println(states); // print out each bestpath
				}
			}
			else if(args[1].equals("g"))
			{
				System.out.println("GUI mode not implemented");
			}
		}
		else if(args[0].contains("q"))
		{
			Storage<TraceState> stateStore = Storage.getQueueInstance(); // configure storage as a queue
			try {
			CircuitBoard board = new CircuitBoard(args[2]); // create new board object with filename as parameter
			search(stateStore, board); // perform search
			}
			catch (FileNotFoundException e)
			{
				System.out.println("Incorrect FilePath\n" + "e");
			}
			catch (InvalidFileFormatException e)
			{
				System.out.println(e);
			}
			if(args[1].contains("c"))
			{
				for(TraceState states: bestPaths)
				{
					System.out.println(states); // print out each state in bestPaths
				}
			}
			else if(args[1].equals("g"))
			{
				System.out.println("GUI mode not implemented");
			}
		}
		else
		{
			printUsage();
		}
		
		
	
	}
	
} // class CircuitTracer