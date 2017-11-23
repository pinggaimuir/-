package cn.shoparound.utils;

import cn.wanghaomiao.xpath.model.JXDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 高健 on 2017/5/17.
 */
@Component
public class ParseUtil {

    /**
     * 通过Jsoup的url
     * @param url
     * @return
     * @throws IOException
     */
    public JXDocument getDocByJsoup(String url) throws IOException {
        Document doc= Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .ignoreContentType(true)
                .get();
        JXDocument jxdoc=new JXDocument(doc);
        return jxdoc;
    }

    //获取json
    public String getJsonByJsoup(String url) throws IOException {
        Document doc= Jsoup.connect(url).ignoreContentType(true).get();
        return doc.text();
    }

    //通过xpath获取节点列表
    public List<String> getInfoListByXpath(JXDocument document, String xpath){
        try {
            List list = document.sel(xpath);
            List<String> strlist=new ArrayList<String>();
            for(Object o:list){
                strlist.add(o.toString());
            }
            return strlist;
        }catch (Exception e){
//            throw new RuntimeException("解析xpath出错：---------"+xpath+"----------");
            return null;
        }
    }
    //通过xpath获取单节点信息
    public String getInfoByXpath(JXDocument document,String xpath){
        try {
            String info = document.sel(xpath).get(0).toString().trim();
            return info;
        }catch (Exception e){
//            throw new RuntimeException("解析xpath出错：---------"+xpath+"----------");
            return "";
        }
    }

    /**
     * 用正则表达式获取单个匹配信息
     * @param reg
     * @param str
     * @return
     */
    public String getStrByReg(String reg,String str){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            return matcher.group(1).toString();
        }
        return null;
    }

    /**
     * 用正则表达式获取多个匹配信息
     * @param reg
     * @param str
     * @returns
     */
    public List<String> getStrsByReg(String reg,String str){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        List<String> list=new ArrayList<String>();
        while (matcher.find()){
            list.add(matcher.group(1).toString());
        }
        return list;
    }

}
