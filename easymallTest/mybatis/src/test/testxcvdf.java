package test;

/**
 * Created by tarena on 2016/10/12.
 */
public class testxcvdf {
    public static  void main(String[] args){
        boolean[] houzi=new boolean[15];
        int i=0;
        int baoshu=1;
        int taotai=0;
        while(taotai!=14){
            if(houzi[i]==false){
                baoshu++;
                if(baoshu==7){
                    houzi[i]=true;
                    taotai++;
                   baoshu=1;
                }
            }
            i++;
            if(i==15){
                i=0;
            }
            for (int j = 0; j <houzi.length ; j++) {
                System.out.print(houzi[j]+"  ");
            }
            System.out.println();
        }
    }


}
