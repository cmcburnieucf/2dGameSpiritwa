package ai;

public class Node {
	
	Node parent;
	public int col, row;
	int gCost, hCost, fCost;
	boolean closed, open, checked;
	
	public Node(int col, int row) {
		this.col = col;
		this.row = row;
	}
}
