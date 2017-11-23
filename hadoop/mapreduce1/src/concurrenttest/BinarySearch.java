package concurrenttest;

/**
 * Created by gao on 2017/1/10.
 */
public class BinarySearch {
    public static void main(String[] args) {

    }
    public static int search(int[] arr,int aim){
        int start=0;
        int length=arr.length-1;
        while(start<=length){
            int middle=start+((start+length)>>1);
            if(arr[middle]==aim){
                return middle;
            }else if(arr[middle]>aim){
                length=middle-1;
            }else{
                start=middle+1;
            }
        }
        return -1;
    }
}
