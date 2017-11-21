package mytest;

/**
 * Levenshtein distance最先是由俄国科学家Vladimir Levenshtein在1965年发明，用他的名字命名。不会拼读，可以叫它edit distance（编辑距离）。
 * 原理很简单，就是返回将第一个字符串转换(删除、插入、替换)成第二个字符串的编辑次数。次数越少，意味着字符串相似度越高
 * LD用m*n的矩阵存储距离值。
 * Created by Sammy on 14-5-26.
 */
public class SimilarityClac {
    /**
     * 求三个数中最小的一个
     * @param one
     * @param two
     * @param three
     * @return
     */
    private static int min(int one, int two, int three) {
        int min = one;
        if(two < min) {
            min = two;
        }
        if(three < min) {
            min = three;
        }
        return min;
    }

    /**
     * 计算矢量距离
     * Levenshtein Distance(LD)
     * @param str1
     * @param str2
     * @return
     */
    public static int ld(String str1, String str2) {
        int d[][];    //矩阵
        int n = str1.length();
        int m = str2.length();
        int i;    //遍历str1的
        int j;    //遍历str2的
        char ch1;    //str1的
        char ch2;    //str2的
        int temp;    //记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if(n == 0) {
            return m;
        }
        if(m == 0) {
            return n;
        }
        d = new int[n+1][m+1];
        for(i=0; i<=n; i++) {    //初始化第一列
            d[i][0] = i;
        }
        for(j=0; j<=m; j++) {    //初始化第一行
            d[0][j] = j;
        }
        for(i=1; i<=n; i++) {    //遍历str1
            ch1 = str1.charAt(i-1);
            //去匹配str2
            for(j=1; j<=m; j++) {
                ch2 = str2.charAt(j-1);
                if(ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1]+temp);
            }
        }
        return d[n][m];
    }

    /**
     * 计算相似度
     * @param str1
     * @param str2
     * @return
     */
    public static double sim(String str1, String str2) {
        int ld = ld(str1, str2);
        return 1 - (double) ld / Math.max(str1.length(), str2.length());
    }

    public static void main(String[] args) {

//        String str1 = "chenlb.blogjava.net";
        String str1 = "人本 春款韩版潮女鞋 厚底松糕鞋 低帮休闲布鞋子纯色帆布鞋 包邮";
//        String str2 = "chenlb.blogjava.net";
//        String str2 = "chenlb.javaeye.com";
        String str2 = "Zenus 杰努斯 新款男士休闲鞋英伦低帮运动鞋 韩版潮流驾车鞋板鞋优";
        System.out.println("ld="+ld(str1, str2));
        System.out.println("sim="+sim(str1, str2));
    }
}
