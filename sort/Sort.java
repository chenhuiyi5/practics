package sort;

import java.util.Arrays;
/**
 * 
 * @author chy
 * purpose:常见排序算法
 */

public class Sort {

	public static void main(String[] args) {
		int[] array={1,3,7,9,5,2};
		insertSort(array);
		bubbleSort(array);
		quickSort(array);
		shellSort(array);
	}
	
	/*
	 * 插入排序：直接插入排序,从小到大
	 * 直接插入排序法的排序原则是：将一组无序的数字排列成一排，左端第一个数字为已经完成排序的数字，其他数字为未排序的数字。然后从左到右依次将未排序的数字插入到已排序的数字中
	 * 平均时间复杂度：n^2
	 */
	public static void insertSort(int[] array){
		for(int i=1;i<array.length;i++){
			int temp = array[i];
			int j=i-1;
			for(;j>=0 && array[j]>temp;j--){
				array[j+1]=array[j];
			}
			array[j+1]=temp;
		}
		System.out.println("insertSort:"+Arrays.toString(array));
	}
	/*
	 * 交换排序：冒泡排序，从小到大(两两交换，将最大的数依次往后挪)
	 * 平均时间复杂度：n^2
	 */
	public static void bubbleSort(int[] arr){
		for(int i=0;i<arr.length-1;i++){
			for(int j=0;j<arr.length-1-i;j++){
				if(arr[j]>arr[j+1]){
					int temp=arr[j];
					arr[j]=arr[j+1];
					arr[j+1]=temp;
				}	
			}
			
		}
		System.out.println("bubbleSort:"+Arrays.toString(arr));
	}
	/*
	 * 选择排序，从小到大（按位置排，与剩下的数全部比较一遍找到最小的那个）
	 * 平均时间复杂度:n^2
	 */
	public static void quickSort(int[] arr){
		for(int i=0;i<arr.length;i++){
			for(int j=i;j<arr.length;j++){
				if(arr[i]>arr[j]){
					int temp = arr[i];
					arr[i]=arr[j];
					arr[j]=temp;
				}
			}
		}
		System.out.println("quickSort:"+Arrays.toString(arr));
		
	}
	/*
	 * 希尔排序，从小到大
	 * 希尔排序又称“缩小增量排序”
	 * 基本思想是：先将整个待排元素序列分割成若干个子序列（由相隔某 个“增量”的元素组成的）分别进行直接插入排序，然后依次缩减增量再进行排序，待整个序列中的元素基本有序（增量足够小）时，再对全体元素进行一次直接插 入排序
	 */
	public static void shellSort(int[] arr){
		int len = arr.length;
		int gap = 1;
		while(gap<len/3){
			gap=gap*3+1;
		}
		for(;gap>0;gap=(int) Math.floor(gap/3)){
			for(int i=gap;i<len;i++){
				int temp=arr[i];
				int j=i-gap;
				for(;j>0 && arr[j]>temp;j-=gap){
					arr[j+gap]=arr[j];
				}
				arr[j+gap]=temp;
			}
			
		}
		System.out.println("shellSort:"+Arrays.toString(arr));
	}
}
