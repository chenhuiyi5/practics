package sort;

import java.util.Arrays;
/**
 * 
 * @author chy
 * purpose:���������㷨
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
	 * ��������ֱ�Ӳ�������,��С����
	 * ֱ�Ӳ������򷨵�����ԭ���ǣ���һ��������������г�һ�ţ���˵�һ������Ϊ�Ѿ������������֣���������Ϊδ��������֡�Ȼ����������ν�δ��������ֲ��뵽�������������
	 * ƽ��ʱ�临�Ӷȣ�n^2
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
	 * ��������ð�����򣬴�С����(��������������������������Ų)
	 * ƽ��ʱ�临�Ӷȣ�n^2
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
	 * ѡ�����򣬴�С���󣨰�λ���ţ���ʣ�µ���ȫ���Ƚ�һ���ҵ���С���Ǹ���
	 * ƽ��ʱ�临�Ӷ�:n^2
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
	 * ϣ�����򣬴�С����
	 * ϣ�������ֳơ���С��������
	 * ����˼���ǣ��Ƚ���������Ԫ�����зָ�����ɸ������У������ĳ ������������Ԫ����ɵģ��ֱ����ֱ�Ӳ�������Ȼ���������������ٽ������򣬴����������е�Ԫ�ػ������������㹻С��ʱ���ٶ�ȫ��Ԫ�ؽ���һ��ֱ�Ӳ� ������
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
