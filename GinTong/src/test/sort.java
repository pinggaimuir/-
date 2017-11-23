package test;

import java.io.File;

/**
 * Created by 高健 on 2017/3/14.
 */
public class sort {
    public static void main(String[] args) {
//        Fibonacci(12);

//        System.out.println(fibnacci(12));
        int[] arr={10,4,5,34,657,25,685,908,33,23};
        arr=reverse2(arr);
        for (int i = 0; i <arr.length ; i++) {
            System.out.print(arr[i]+",");
        }
//        System.out.println(binarySearch(arr,5,0,arr.length-1));
//        arr=maopaoSort(arr);

//        String f=sort.class.getClassLoader().getResource("test").getPath();
//        File file=new File(f);
//        bianli(file);
    }
    //插入排序
    public static int[] charuSort(int[] arr){
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<i;j++){
                if(arr[i]<arr[j]){
                    int tmp=arr[i];
                    arr[i]=arr[j];
                    arr[j]=tmp;
                }
            }
        }
        return arr;
    }
    //冒泡排序
    public static int[] maopaoSort(int[] arr){
        for (int i = 0; i <arr.length-1 ; i++) {
            for(int j=0;j<arr.length-i-1;j++){
                if(arr[j+1]<arr[j]){
                    int tmp=arr[j+1];
                    arr[j+1]=arr[j];
                    arr[j]=tmp;
                }
            }
        }
        return arr;
    }
    //遍历文件系统
    public static void bianli(File file){
        File[] files=file.listFiles();
        for(File f:files){
            if(f.isDirectory()){
                System.out.println("Directory:"+f.getName());
                bianli(f);
            }else{
                System.out.println(f.getName());
            }
        }
    }
    //二分查找
    public static int binarySearch(int[] arr,int target){
        int low=0;
        int high=arr.length-1;
        while(low<=high){
            int middle=(low+high)/2;
            if(target==arr[middle]){
                return middle;
            }else if(target<arr[middle]){
                high=middle-1;
            }else{
                low=middle+1;
            }
        }
        return -1;
    }
    //递归实现二分查找
    public static int binarySearch(int[] arr,int target,int begin,int end){
        int middle=(begin+end)/2;
        if(begin>end){
            return -1;
        }
        if(target>arr[middle]){
            return binarySearch(arr,target,middle+1,end);
        }else if(target<arr[middle]){
            return binarySearch(arr,target,begin,middle-1);
        }else{
            return middle;
        }
    }
    //迭代实现菲波那切数列
    public static void Fibonacci(int num){
        int fib=1;
        int fib2=1;
        for (int i = 0; i <num ; i++) {
            System.out.print(fib+",");
            int tmp=fib;
            fib=fib2;
            fib2=fib+tmp;
        }
    }
    //递归实现斐波那契
    public static int fibnacci(int num){
        if(num<2){
            return 1;
        }
        return fibnacci(num-1)+fibnacci(num-2);
    }
    //翻转数组
    public static int[] reverse(int[] arr){
        for (int i = 0; i <arr.length/2 ; i++) {
            int tmp=arr[arr.length-i-1];
            arr[arr.length-1-i]=arr[i];
            arr[i]=tmp;
        }
        return arr;
    }
    //快排思想实现数组翻转
    public static int[] reverse2(int[] arr){
        for (int i = 0,j=arr.length-1; i <j ; i++,j--) {
            int tmp=arr[i];
            arr[i]=arr[j];
            arr[j]=tmp;
        }
        return arr;
    }
}
