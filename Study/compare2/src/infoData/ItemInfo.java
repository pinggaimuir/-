/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infoData;

/**
 *
 * @author mazhenhao
 */
public class ItemInfo implements Comparable<ItemInfo>{
    public String itemUrl;
    public String itemIntro;
    public String itemImage;
    public String itemPrice;
    public String jdid=null;

    @Override
    public int compareTo(ItemInfo o) {
        String str1= this.itemPrice.trim();
        String str2 = null;
        if (o!=null) {
            str2 = o.itemPrice.trim();
        }else return 0;
        int len1 = 0;
        int len2 = 0;
        if ((str1!=null&&str2!=null)|(str1!=""&&str2!="")) {
            len1 = str1.length();
            System.out.println(str2+",");
            len2 = str2.length();
        }
        if (len1 > len2) {
            return 1;
        } else if (len2 > len1) {
            return 0;
        } else {
            int str1larger = 1;
            for (int i = 0; i < len1; i++) {
                if (str1.charAt(len1 - 1 - i) < str2.charAt(len1 - 1 - i)) {
                    str1larger = 0;
                } else {
                    str1larger = 1;
                }
            }
            return str1larger;
        }
    }
}
