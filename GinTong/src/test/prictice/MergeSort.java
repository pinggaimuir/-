package test.prictice;

/**
 * 并归排序
 * Created by 高健 on 2017/3/15.
 */
public class MergeSort {
    public static int[] sort(int[] nums,int low,int high){
        int mid=(low+high)/2;
        if(low<high){
            sort(nums,low,mid);
            sort(nums,mid+1,high);
            merge(nums,low,mid,high);
        }
        return nums;
    }

    public static void merge(int[] nums,int low,int mid,int high){
        int[] tmp=new int[high-low+1];
        int i=low;//座指针
        int j=mid+1;//右指针
        int k=0;
        while(i<mid&&j<high){
            if(nums[i]<nums[j]){
                tmp[k++]=nums[i++];
            }else{
                tmp[k++]=nums[j++];
            }
        }

        while (j<=high){
            tmp[k++]=nums[j++];
        }
        while(i<=mid){
            tmp[k++]=nums[j++];
        }
        for (int l = 0; l <tmp.length ; l++) {
            nums[low+l]=tmp[l];
        }
    }
    
}
