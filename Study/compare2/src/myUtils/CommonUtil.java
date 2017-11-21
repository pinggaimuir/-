package myUtils;

/**
 * Created by Sammy on 14-5-26.
 */
public class CommonUtil {
    /**
     * 获取三个数中最大的一个
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int getMax(int a, int b, int c) {
        int temp = a;
        if (b > a && b > c) temp = b;
        if (c > a && c > b) temp = c;
        return temp;
    }

    /**
     * 获取两个数中的最大一个
     *
     * @param a
     * @param b
     * @return
     */
    public static int getMax(int a, int b) {
        return a > b ? a : b;
    }
}
