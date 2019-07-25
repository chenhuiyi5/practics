package tree;
/**
 * 
 * @author chy
 * purpose:中序遍历二叉树  （左中右）
 */
public class ScanTree {
	public static void scanTree(Tree tree){
		//获取做几点
		if(tree.getlCrild().getRoot()!=null){
			scanTree(tree.getlCrild());
		}
		//获取根节点
		System.out.print(tree.getRoot());
		//获取右节点
		if(tree.getrChild().getRoot()!=null){
			scanTree(tree.getrChild());
		}
	}

}
