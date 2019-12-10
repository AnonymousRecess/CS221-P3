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
		
		if (args.length != 3) {
			printUsage();
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
		//TODO: print out clear usage instructions when there are problems with
		// any command line args
	}
	private void search(Storage<TraceState> stateStore, CircuitBoard board)
	{
		
		TraceState initialState = new TraceState(board, board.getStartingPoint().x, board.getEndingPoint().y);
		
		TraceState previousState = initialState;
		if(board.isOpen(previousState.getRow()+1,previousState.getCol())) //PROBABLY NEED TO CHECK LEFT AS WELL
		{
			TraceState horizontalTrace = new TraceState(previousState,previousState.getRow()+1,previousState.getCol());
			stateStore.store(horizontalTrace);
		}
		if(board.isOpen(previousState.getRow(), previousState.getCol()+1)) //PROBABLY NEED TO CHECK DOWN AS WELL
		{
			TraceState verticalTrace = new TraceState(previousState,previousState.getRow(), previousState.getCol()+1);
			stateStore.store(verticalTrace);
		}
		while(!stateStore.isEmpty())
		{
			int pathMin = 0;
			TraceState current = stateStore.retrieve();
			if(bestPaths.isEmpty() && current.isComplete())
			{
				pathMin = current.pathLength();
				bestPaths.add(current);
			}
			if(current.isComplete() && current.pathLength() == pathMin)
			{
				bestPaths.add(current);
			}
			else if(current.isComplete() && current.pathLength() < pathMin)
			{
				pathMin = current.pathLength();
				bestPaths.clear();
				bestPaths.add(current);
			}
			else
			{
				if(board.isOpen(current.getRow()+1,current.getCol())) //PROBABLY NEED TO CHECK LEFT AS WELL (ALSO CHECK IF OUT OF BOUNDS)
				{
					TraceState horizontalTrace = new TraceState(current,current.getRow()+1,current.getCol());
					stateStore.store(horizontalTrace);
				}
				if(board.isOpen(current.getRow(), current.getCol()+1)) //PROBABLY NEED TO CHECK DOWN AS WELL
				{
					TraceState verticalTrace = new TraceState(current,current.getRow(), current.getCol()+1);
					stateStore.store(verticalTrace);
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
		//TODO: parse command line args
		
		if(args[0].toLowerCase() == "s")
		{
			System.out.println("Implemented Using a Queue");
		}
		else if(args[0] == "q")
		{
			Storage<TraceState> stateStore =Storage.getQueueInstance();
			try {
			CircuitBoard board = new CircuitBoard(args[3]);
			search(stateStore, board);
			}
			catch (FileNotFoundException e)
			{
				System.out.println("Incorrect FilePath\n" + "e");
			}
			catch (InvalidFileFormatException e)
			{
				System.out.println(e);
			}
			if(args[1].toLowerCase() == "c")
			{
				for(TraceState states: bestPaths)
				{
					System.out.println(states);
				}
			}
			if(args[1].toLowerCase() == "g")
			{
				System.out.println("GUI mode not implemented");
			}
		}
		else
		{
			printUsage();
		}
		
		
		//TODO: initialize the Storage to use either a stack or queue
		//TODO: read in the CircuitBoard from the given file
		//TODO: run the search for best paths
		//TODO: output results to console or GUI, according to specified choice

	}
	
} // class CircuitTracer