package tree;

import java.util.Scanner;

public class CreataTree {
	static Scanner scan = new Scanner(System.in);
	public static void createTree(Tree tree){
		String endStr="";
		endStr=scan.nextLine();
		if("#".equals(endStr)){//每个树节点以 “#” 结束创建 
			return;
		}else{
			tree.setRoot(endStr);//赋值根节点
			tree.setlCrild(new Tree());//创建左节点
			createTree(tree.getlCrild());//设置左节点值
			tree.setrChild(new Tree());//创建右节点
			createTree(tree.getrChild());//设置左节点值
		}
	}

}
