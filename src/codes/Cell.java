package codes;

public class Cell {

	private int data, boxID, x, y;
	private Cell up, down, left, right;
	private boolean[] possible = { true, true, true, true, true, true, true, true, true, true };

	public Cell() {
		up = null;
		down = null;
		left = null;
		right = null;
		data = 0;
		boxID = 0;
	}

	public Cell(int data) {
		up = null;
		down = null;
		left = null;
		right = null;
		this.data = data;
		boxID = 0;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
		// Once we solve a node, all of its possibilities go away
		for (int x = 0; x < 10; x++)
			possible[x] = false;
	}

	public Cell getUp() {
		return up;
	}

	public void setUp(Cell up) {
		this.up = up;
	}

	public Cell getDown() {
		return down;
	}

	public void setDown(Cell down) {
		this.down = down;
	}

	public Cell getLeft() {
		return left;
	}

	public void setLeft(Cell left) {
		this.left = left;
	}

	public Cell getRight() {
		return right;
	}

	public void setRight(Cell right) {
		this.right = right;
	}

	public int getBoxID() {
		return boxID;
	}

	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean[] getPossible() {
		return possible;
	}

	public void setPossible(boolean[] possible) {
		this.possible = possible;
	}

	public int numberPossible() {
		int count = 0;
		for (int x = 1; x < 10; x++)
			if (possible[x] == true)
				count++;
		return count;
	}

	public boolean isPossible(int number) {
		return possible[number];
	}

	public void setFalse(int number) {
		possible[number] = false;
	}

}
