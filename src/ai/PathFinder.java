package ai;

import java.util.ArrayList;

import main.GamePanel;

public class PathFinder {
	GamePanel gp;
	Node[][] nodes;
	ArrayList<Node> openList = new ArrayList<>();
	ArrayList<Node> checkedList = new ArrayList<>();
	public ArrayList<Node> path = new ArrayList<>();
	Node startNode, goalNode, curNode;
	boolean reachedGoal = false;
	int numSteps = 0;
	
	public PathFinder(GamePanel gp) {
		this.gp = gp;
		fillNodes();
	}
	
	public void fillNodes() {
		int maxCol = gp.tileManager.mapTileSizes[gp.curMap][1], 
		maxRow = gp.tileManager.mapTileSizes[gp.curMap][0];
		nodes = new Node[maxRow][maxCol];
		
		for(int i = 0; i < maxRow; i++) {
			for(int j = 0; j < maxCol; j++) {
				nodes[i][j] = new Node(j, i);
			}
		}
	}
	
	public void resetNodes() {
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[i].length; j++) {
				nodes[i][j].open = false;
				nodes[i][j].checked = false;
				nodes[i][j].closed = false;
			}
		}
		
		openList.clear();
		path.clear();
		reachedGoal = false;
		numSteps = 0;
	}
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
		resetNodes();
		
		curNode = startNode = nodes[startRow][startCol];
		goalNode = nodes[goalRow][goalCol];
		openList.add(curNode);
		
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[i].length; j++) {
				int tileNum = gp.tileManager.mapTileNums[gp.curMap][i][j];
				if(gp.tileManager.tile[tileNum].collision) {
					nodes[i][j].closed = true;
				}
				/*
				for(int i = 0; i < gp.iTiles[gp.curMap].length; i++){
					if(gp.iTiles[gp.curMap][i] != null &&
						gp.iTiles[gp.curMap][i].removable){
						int iCol = gp.iTiles[gp.curMap][i].worldPosX/gp.tileSize,
							iRow = gp.iTiles[gp.curMap][i].worldPosY/gp.tileSize;
						nodes[iRow][iCol].closed = true;
					}
				}
				*/
				getCost(nodes[i][j]);
			}
		}
	}
	
	public void getCost(Node node) {
		int xDist = 0, yDist = 0;
		//Get G Cost (how far away is current Node from Goal?)
		xDist = Math.abs(node.col - goalNode.col);
		yDist = Math.abs(node.row - goalNode.row);
		node.gCost = xDist+yDist;
		//Get H Cost (how far away is current Node from Start?)
		xDist = Math.abs(node.col - startNode.col);
		yDist = Math.abs(node.row - startNode.row);
		node.hCost = xDist+yDist;
		//Get F Cost (Total Distance between Start and Goal 
		//if current Node is included in path)
		node.fCost = node.gCost+node.hCost;	
	}
	
	public boolean followPath() {
		while(!reachedGoal && numSteps < 500) {
			int col = curNode.col, row = curNode.row;
			curNode.checked = true;
			openList.remove(curNode);
			
			//check all directions for open nodes
			if((row-1) >= 0) {
				openNode(nodes[row-1][col]);
			}
			if((row+1) < nodes.length) {
				openNode(nodes[row+1][col]);
			}
			if((col-1) >= 0) {
				openNode(nodes[row][col-1]);
			}
			if((col+1) < nodes[0].length) {
				openNode(nodes[row][col+1]);
			}
			
			//find best node
			int bestNodeIndex = 0;
			int bestNodeFCost = Integer.MAX_VALUE;
			int bestNodeGCost = Integer.MAX_VALUE;
			
			
			for(int i = 0; i < openList.size(); i++) {
				//Check if the node's F Cost is better than current one
				if(openList.get(i).fCost < bestNodeFCost) {
					bestNodeIndex = i;
					bestNodeFCost = openList.get(i).fCost;
					bestNodeGCost = openList.get(i).gCost;
				}
				//If F Costs are the same, compare G Costs
				else if((openList.get(i).fCost == bestNodeFCost) &&
						(openList.get(i).gCost < bestNodeGCost)) {
					bestNodeIndex = i;
					bestNodeFCost = openList.get(i).fCost;
					bestNodeGCost = openList.get(i).gCost;
				}
			}
			
			if(openList.size() == 0) {
				break;
			}
			
			curNode = openList.get(bestNodeIndex);
			
			if(curNode == goalNode) {
				reachedGoal = true;
				trackPath();
			}
			
			numSteps++;
		}
		
		return reachedGoal;
	}
	
	public void openNode(Node node) {
		if(!node.open && !node.checked && !node.closed) {
			node.open = true;
			node.parent = curNode;
			openList.add(node);
		}
	}
	
	public void trackPath() {
		Node cur = goalNode;
		
		while(cur != startNode) {
			path.add(0, cur);
			cur = cur.parent;
		}
	}
}
































