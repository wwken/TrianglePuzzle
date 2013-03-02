package com.liquidNet.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author ken Wu 
 * Copyright: 2013-02-14 happy V-day :)
 *
 */
public class TrianglePuzzle {

	
	static class Node {
		static int seed = 1;
		int nID;	//This is for idenified each individual node
		int data;
		Node left, right = null;
		Node heavyLeftOrRight = null;
		Node(int i) {
			data = i;
			nID = seed++;
		}
	}
	
	public static void main(String[] args) {
		//Some variables to initialize
		//String inputFile = "C:\\triangle_test_100rows.txt";
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a file name (e.g. triangle_test_4rows.txt or triangle_test_100rows.txt ): ");
		System.out.flush();
		String filename = scanner.nextLine();
		
		//Step 1, parse the whole input file and construct the binary tree structure to store it
		Node treeRoot = constructTree(filename);

		//Step 2, traverse the whole tree
		HashMap<Integer, Integer> childSum = new HashMap<Integer, Integer>();
		int mostSum = traverseTreeAndFindTheHeaviestPath(treeRoot, childSum);
		
		//Step 3, print out what the traverse function found
		printFindings(mostSum, treeRoot);
	}

	private static void printFindings(int mostSum, Node treeRoot) {
		System.out.println("The maximum sum from top to bottom is : " + mostSum);
		
		Node t = treeRoot.heavyLeftOrRight;
		System.out.print("This is found by following the path : " + treeRoot.data);
		while(t != null) {
			System.out.print(" + " + t.data);
			t = t.heavyLeftOrRight;
		}
		System.out.print(" = " + mostSum);
		
	}

	private static int traverseTreeAndFindTheHeaviestPath(Node node, HashMap<Integer, Integer> childSum) {
		if(node.left == null && node.right == null) {
			return node.data;
		} else {
			int leftPath, rightPath;
			if(childSum.containsKey(node.left.nID)) {
				leftPath = childSum.get(node.left.nID);
			} else {
				leftPath = traverseTreeAndFindTheHeaviestPath(node.left, childSum);
			}
			if(childSum.containsKey(node.right.nID)) {
				rightPath = childSum.get(node.right.nID);
			} else {
				rightPath = traverseTreeAndFindTheHeaviestPath(node.right, childSum);
			}
			
			int sum;
			if(leftPath > rightPath) {
				node.heavyLeftOrRight = node.left;
				sum = node.data + leftPath;
			} else {
				node.heavyLeftOrRight = node.right;
				sum = node.data + rightPath;
			}
			childSum.put(node.nID, sum);
			return sum;
		}
	}

	public static Node constructTree(String inputFile) {
		BufferedReader reader;
		String line = null;
		Node root = null;
		ArrayList<Node> allCurrentParrentNodes = new ArrayList<Node>();
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			if ((line = reader.readLine()) != null) {
				//Reading the first line and create the root
				int thisNum = Integer.parseInt(line.trim());
				root = new Node(thisNum);
				allCurrentParrentNodes.add(root);
			}
			
			
			while ((line = reader.readLine()) != null) {
				String[] numStrArray = line.split(" ");
				ArrayList<Node> thisAllCurrentNodes = new ArrayList<Node>();
				for(int i=0; i<numStrArray.length; i++) {
					int thisNum = Integer.parseInt(numStrArray[i].trim());
					//System.out.print(thisNum + " ");
					Node thisNode = new Node(thisNum);
					thisAllCurrentNodes.add(thisNode);
					if(i==0) {
						allCurrentParrentNodes.get(i).left = thisNode;
					} else if (i == (numStrArray.length - 1)) {
						allCurrentParrentNodes.get(i-1).right = thisNode;
					} else {
						allCurrentParrentNodes.get(i-1).right = thisNode;
						allCurrentParrentNodes.get(i).left = thisNode;
					}
				}
				allCurrentParrentNodes = thisAllCurrentNodes;
				//System.out.println(" ");
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

}

