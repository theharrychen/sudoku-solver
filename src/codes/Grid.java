package codes;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Grid {
	
	private Cell first;

	private int dimension;

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public Cell getFirst() {
		return first;
	}

	public void setFirst(Cell first) {
		this.first = first;
	}
	
	//Constructor
	public Grid(int dimension){	
		this.dimension = dimension;
		
		//Creating the linked grid
		if(dimension == 1)
			first = new Cell();
		else if (dimension <= 0)
			first = null;
		else {
			// Making the first Cell
			Cell temp = new Cell();
			first = temp;
			Cell columnMarker = first;
			Cell rowMarker = first;

			// Making the rest of the first row
			for(int x = 0; x < dimension - 1; x++){
				temp = new Cell();
				columnMarker.setRight(temp);
				temp.setLeft(columnMarker);
				columnMarker = temp;
			}

			for(int y = 0; y < dimension - 1; y++){
				// Making the first Cell of the next row
				temp = new Cell();
				rowMarker.setDown(temp);
				temp.setUp(rowMarker);
				rowMarker = temp;

				// Making the rest of the new row
				columnMarker = rowMarker;

				for(int x = 0; x < dimension - 1; x++){
					temp = new Cell();
					columnMarker.setRight(temp);
					temp.setLeft(columnMarker);
					temp.setUp(temp.getLeft().getUp().getRight());
					temp.getUp().setDown(temp);
					columnMarker = temp;
				}
			}
			
			
		}
		
		//Setting BoxIDS & Coordinates
		Cell temp = first;
		Cell rowMarker = first;
		int row = 0, column = 0;
		
		while(temp != null){
			column = 0;
			while(temp != null){
				temp.setBoxID((column/3+1) + (row/3)*3);//Equation for BoxIDS
				temp.setX(column);
				temp.setY(row);
				temp = temp.getRight();
				column++;
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
			row++;
		}
	}
	
	//Display only the board
	public void display(){
		Cell temp = first;
		Cell rowMarker = first;
		int vertCount = 0, horizCount = 0;
		
		for(int x = 0; x < dimension*3; x++)
			System.out.print("-");	
		System.out.println();
		
		while(temp != null){
			while(temp != null){
				if(vertCount%3 == 0)
					System.out.print(" |");
				vertCount++;
				
				System.out.print(" " + temp.getData());
				temp = temp.getRight();
				
				if(temp == null)
					System.out.print(" |");
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
			System.out.println();
			
			horizCount++;
			if(horizCount %3 == 0){
				for(int x = 0; x < dimension*3; x++)
					System.out.print("-");	
				System.out.println();
			}
		
		}
	}
	
	//Display the possibilities of each cell
	public void displayPossible(){
		Cell temp = first;
		Cell rowMarker = first;
		int vertCount = 0, horizCount = 0;
		
		for(int x = 0; x < dimension*3; x++)
			System.out.print("-");	
		System.out.println();
		
		while(temp != null){
			while(temp != null){
				if(vertCount%3 == 0)
					System.out.print(" |");
				vertCount++;
				
				System.out.print(" " + temp.numberPossible());
				temp = temp.getRight();
				
				if(temp == null)
					System.out.print(" |");
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
			System.out.println();
			
			horizCount++;
			if(horizCount %3 == 0){
				for(int x = 0; x < dimension*3; x++)
					System.out.print("-");	
				System.out.println();
			}
		
		}
	}
	
	public void displayAll(){
		System.out.println("The Board:");
		display();
		System.out.println("Possiblities:");
		displayPossible();
	}
	
	public void loadSudoku(String fileName) throws IOException {
		File puzzle = new File(fileName);
		Scanner input = new Scanner(puzzle);

		Cell temp = first;
		Cell rowMarker = first;

		while (temp != null) {
			while (temp != null) {
				int solution = input.nextInt();
				if (solution != 0)
					solve(temp, solution);

				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}

		input.close();
	}
	
	//Solves a cell with a given number
	public void solve(Cell cell, int number){
		cell.setData(number);
		
		Cell temp = cell.getLeft();
		while(temp != null){
			temp.setFalse(number);
			temp = temp.getLeft();
		}
		
		temp = cell.getRight();
		while(temp != null){
			temp.setFalse(number);
			temp = temp.getRight();
		}
		
		temp = cell.getUp();
		while(temp != null){
			temp.setFalse(number);
			temp = temp.getUp();
		}
		
		temp = cell.getDown();
		while(temp != null){
			temp.setFalse(number);
			temp = temp.getDown();
		}
		
		temp = first;
		Cell rowMarker = first;
		
		while(temp != null){
			while(temp != null){
				if(temp.getBoxID() == cell.getBoxID())
					temp.setFalse(number);;
					
				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
	}

	// SolveMethod1 solves for cells that only have 1 possibility
	//Naked Single
	public int solveMethod1(){
		int changesMade = 0;
		Cell temp = first;
		Cell rowMarker = first;

		while (temp != null) {
			while (temp != null) {
				if (temp.numberPossible() == 1)
					for (int x = 1; x < dimension+1; x++)
						if (temp.isPossible(x)) {
							solve(temp, x);
							changesMade++;
						}

				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
		return changesMade;
	}

	//Solves a cell that has a unique number in its row and column
	//Hidden Single elimnation
	public int solveMethod2(){
		int changesMade = 0;
		
		Cell temp = first;
		Cell rowMarker = first;
		
		while (temp != null) {
			while (temp != null) {
				if(temp.numberPossible() != 0){
					for(int x = 1; x < dimension+1; x++){
						int count = 0;
						Cell temp2 = temp;
						Cell unique = null;
						
						while(temp2 != null){
							if(temp2.isPossible(x)){
								count++;
								unique = temp2;
							}
							temp2 = temp2.getRight();
						}
						
						temp2 = temp;
						while(temp2 != null){
							if(temp2.isPossible(x)){
								count++;
								unique = temp2;
							}
							temp2 = temp2.getLeft();
						}
						
						temp2 = temp;
						while(temp2 != null){
							if(temp2.isPossible(x)){
								count++;
								unique = temp2;
							}
							temp2 = temp2.getUp();
						}
						
						temp2 = temp;
						while(temp2 != null){
							if(temp2.isPossible(x)){
								count++;
								unique = temp2;
							}
							temp2 = temp2.getDown();
						}
						
						if(count == 1){
							solve(unique, x);
							changesMade++;
							break;
						}	
					}
				}
				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;

		}

		return changesMade;
	}
	
	//If a two cells in a row have the same pair of possibilities, all other 
	//possibilities are eliminated.
	//Naked Pair - for rows
	public int solveMethod3(){
		int changesMade = 0;
		Cell temp = first;
		Cell rowMarker = first;
		
		while (temp != null) {
			Cell firstPair = null;
			Cell secondPair = null;
			while (temp != null) {
				if(temp.numberPossible() == 2){
					if(firstPair == null)
						firstPair = temp;
					else if(firstPair != null && secondPair == null)
						secondPair = temp;
				}
				
				if(firstPair != null && secondPair != null){
					if(equalPair(firstPair.getPossible(), secondPair.getPossible())){
						int num1 = 0, num2 = 0;
						for(int x = 1; x < dimension+1; x++){
							if(firstPair.getPossible()[x] == true && num1 == 0)
								num1 = x;
							else if(firstPair.getPossible()[x] == true && num1 !=0 && num2 == 0)
								num2 = x;
						}
						
						if(eliminatePair(num1, num2, firstPair, secondPair, rowMarker) == true){
							changesMade++;
						}
					}
				}

				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
		return changesMade;
	}
	
	public boolean equalPair(boolean[] one, boolean[] two){
		for(int x = 0; x < dimension+1; x++){
			if(one[x] != two[x])
				return false;
		}
		return true;
	}
	
	//Eliminates a pair from a row
	public boolean eliminatePair(int num1, int num2, Cell pair1, Cell pair2, Cell current){
		boolean hasElim = false;
		
		while(current != null){
			if(current.numberPossible() != 0 && current != pair1 && current != pair2){
				if(current.getPossible()[num1] == true){
					current.setFalse(num1);
					hasElim = true;
				}
				
				if (current.getPossible()[num2] == true){
					current.setFalse(num2);
					hasElim = true;
				}
			}
			
			current = current.getRight();
		}
		return hasElim;
	}
	
	//Runs all solve methods
	public void runSolveMethods() {
		int changesMade = 0;
		//Loops until no more changes are made
		do{
			changesMade = 0;
			changesMade += solveMethod1() + solveMethod2() + solveMethod3();
		} while (changesMade > 0);	
	}
	
	//Returns a newly cloned instance of the grid
	//This gives a deep copy of the grid which acts as a separate object
	public Grid clone(){
		Grid clone = new Grid(dimension);
		
		Cell temp = first;
		Cell rowMarker = first;
		Cell temp2 = clone.getFirst();
		Cell rowMarker2 = clone.getFirst();

		while (temp != null) {
			while (temp != null) {
				temp2.setData(temp.getData());
				temp2.setBoxID(temp.getBoxID());
				//Transfers a deep copy of the array
				temp2.setPossible(temp.getPossible().clone());
				//System.arraycopy(temp.getPossible(), 0, temp2.getPossible(), 0, dimension+1);
				
				temp = temp.getRight();
				temp2 = temp2.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
			rowMarker2 = rowMarker2.getDown();
			temp2 = rowMarker2;
		}
		
		return clone;
	}
	
	//Checks if the grid can still be solved
	//Returns false if there is an empty cell with 0 possibilities
	public boolean isValid(){
		boolean isValid = true;
		Cell temp = first;
		Cell rowMarker = first;
		
		outerloop:
		while(temp != null){
			while(temp != null){
				if(temp.getData() == 0 && temp.numberPossible() == 0){
					isValid = false;
					break outerloop;
				}
				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
		
		return isValid;
	}
	
	//Finds a node based on given coordinates
	public Cell find(int x, int y){
		Cell temp = first;
		Cell rowMarker = first;
		
		while(temp != null){
			while(temp != null){
				if(temp.getX() == x && temp.getY() == y)
					return temp;
				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
		
		return null;
	}
	
	//Returns if the the sudoku is solved or not
	public boolean isSolved(){
		Cell temp = first;
		Cell rowMarker = first;
		
		while(temp != null){
			while(temp != null){
				if(temp.getData() == 0)
					return false;
				temp = temp.getRight();
			}
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
		
		return true;
	}
}

