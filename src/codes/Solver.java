package codes;

import java.io.IOException;

public class Solver {

	public static Grid board = new Grid(9);

	public static void main(String[] args) {
		if (args.length == 1) {
			try {
				board.loadSudoku(args[0]);
				board.displayAll();

				System.out.println("\nSolving in progress...");
				board.runSolveMethods();
				runGuess(board.clone(), 0, 0, 0);

				System.out.println("The Sudoku has been Solved!\n");
				board.displayAll();
			} catch (IOException e) {
				System.out.println("Please enter the name of a valid sudoku "
									+ "text file as a command line argument!");
			}
		} else {
			System.out.println("Please enter exactly 1 command line argument!");
			System.out.println("Ex: java codes.Solver easy.txt");
		}
	}

	// Back-Tracking Guessing Algorithm
	// Uses a recursive call stack paradigm, where the method is called when a new
	// move is made.
	// It returns or "pops" a move off the stack when the board becomes invalid and
	// makes a new
	// move starting from the valid board.
	public static void runGuess(Grid sudoku, int x, int y, int guess) {
		Cell temp = sudoku.find(x, y);
		Cell rowMarker = sudoku.getFirst();

		for (int j = 0; j < temp.getY(); j++)
			rowMarker = rowMarker.getDown();

		if (guess != 0)
			sudoku.solve(temp, guess);

		if (sudoku.isSolved() && sudoku.isValid()) {
			board = sudoku.clone();
			return;
		}

		if (!sudoku.isValid())
			return;

		while (temp != null) {
			while (temp != null) {
				if (temp.getData() == 0) {
					int i;
					for (i = 1; i < 10; i++) {
						if (temp.getPossible()[i] == true) {
							runGuess(sudoku.clone(), temp.getX(), temp.getY(), i);
						}
					}
					if (i > 9)
						return;
				}
				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
	}
}
