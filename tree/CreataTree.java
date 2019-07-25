package tree;

import java.util.Scanner;

public class CreataTree {
	static Scanner scan = new Scanner(System.in);
	public static void createTree(Tree tree){
		String endStr="";
		endStr=scan.nextLine();
		if("#".equals(endStr)){
			return;
		}else{
			tree.setRoot(endStr);
			tree.setlCrild(new Tree());
			createTree(tree.getlCrild());
			tree.setrChild(new Tree());
			createTree(tree.getrChild());
		}
	}

}
