package tree;

public class ScanTree {
	public static void scanTree(Tree tree){
		
		if(tree.getlCrild().getRoot()!=null){
			scanTree(tree.getlCrild());
		}
		System.out.print(tree.getRoot());
		if(tree.getrChild().getRoot()!=null){
			scanTree(tree.getrChild());
		}
	}

}
