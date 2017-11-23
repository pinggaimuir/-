import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gao on 2017/1/9.
 */
public class RegxTest {
    public static void main(String[] args) {
        String str="http://127.0.0.1:8080/a.jsp|a.jsp|页面A|UTF-8|1366x768|24-bit|zh-cn|0|1|23.0 r0|0.23479920360599338||Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36|81751120689790008696|3854672754_3_1483937985011|127.0.0.1";
        String str2="http://127.0.0.1:8080/b.jsp|b.jsp|页面B|UTF-8|1366x768|24-bit|zh-cn|0|1|23.0 r0|0.6983232384838032|http://127.0.0.1:8080/a.jsp|Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36|81751120689790008696|3854672754_19_1483940436656|127.0.0.1";
        String rex="^.*?_\\d_(.+?)\\|.*$";
        String rex2="(?:^.*?\\|)+\\w+?_\\d_(.+?)\\|.*$";
        Pattern p=Pattern.compile(rex2);
        Matcher matcher=p.matcher(str2);
        matcher.find();
        System.out.println(matcher.group(1));

        String str3="<books><book><author>李四</author></book><book><author>李四</book></books>";
        String reg="^<books>(<book><author>.*?</author></book>)+</books>$";
        Pattern p2=Pattern.compile(reg);
        Matcher matcher1=p2.matcher(str3);
        matcher.find();
        System.out.println();

        byte i=127;
//        i=i+1;
        double c=(short)10/10.2*2;
    }

}
