package cn.gao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 高健 on 2017/11/14.
 */
public class RegTest {
    public static void main(String[] args) {
        String s="</div>\n" +
                "    <script type=\"text/javascript\" src=\"//p-vp.autohome.com.cn/api/player?mid=A78BA906955859E0&container=youkuplayer&autostart=1&callback=onPlayerCreated&jsversion=20170905.1\"></script>\n" +
                "    <script type=\"text/javascript\">\n";

        Pattern p=Pattern.compile("//p-vp\\.autohome\\.com\\.cn/api/player\\?mid=(\\w+?)&container");
        Matcher m=p.matcher(s);
        if(m.find()){
            System.out.println(m.group());
        }
    }
}
