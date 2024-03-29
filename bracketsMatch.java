
package match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
/**
 * 
 * @author chy
 * purpose:括号匹配算法
 *
 */
public class Match {

	public static void main(String[] args) {
		String str="()akd{{fd}ds[dfd](sdf)}()";
		if(myMatch(str)){
			System.out.println("括号匹配成功");
		}else{
			System.out.println("括号匹配失败");
		}

	}
	public static boolean myMatch(String str){
		//空串直接匹配失败
		if(str==null ||"".equals(str)){
			return false;
		}
		//定义一个map,存储匹配规则
	    Map<String,String> map = new HashMap<String,String>();
	    //定义一个list集合，存储括号用于字符串中括号的查找
	    List<String> list = new ArrayList<String>();
	    boolean result=true;
	    //匹配规则
	    map.put("}", "{");
	    map.put("]", "[");
	    map.put(")", "(");
      //集合包含所有括号
	    list.add("{");
	    list.add("}");
	    list.add("[");
	    list.add("]");
	    list.add("(");
	    list.add(")");
	    //定义一个栈，用于存储字符串中没有匹配的左括号
		Stack<String> stack=new Stack<String>();
		//切割成一个个字符
		String[] chars=str.split("");
		//map取出值
		String temp="";
		//出栈值
		String left;
		//循环遍历开始匹配
		for(int i=0;i<chars.length;i++){
			//是括号则开始匹配
			if(list.contains(chars[i])){
				//如果是右括号则与栈中的左括号匹配
				temp=map.get(chars[i]);
				if(temp!=null && !"".equals(temp)){
					//栈空，匹配失败
					if(stack.isEmpty()){
						result=false;
						break;
					}else{
						//栈不空，则取出一个左括号匹配
						left=stack.pop();
						//匹配规则不符合，匹配失败
						if(!left.equals(temp)){
							result=false;
							break;
						}
						
					}
				}else{//左括号则入栈
					stack.push(chars[i]);
				}
			}
		}
		//result=true 时看看栈是否已空
		if(result){
			 //栈不空 匹配失败
			if(!stack.isEmpty()){
				result=false;
			}
		}
		return result;
	}

}
