package com.bric.temporary;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.bric.crawler.MapInit;
import com.bric.util.MapConfigureIO;

public class Renew21foodNameList {
	
private static String KindUrl = "http://www.21food.cn/news/pricecategorylist_cat-";

public void DEBUG_fetch21FoodPrice(){
	for(String kind:MapInit.nameKindIdMap21food.keySet()){
		System.out.println("**********begin to fetch"+kind+" data*********");
		List<String> varList = getVarList(kind);
		Map<String, Integer> map = MapInit.nameReflectMap21food.get(kind);
		map = new MapConfigureIO().getFullMap(varList, kind);//初始化对应map
	}
}

private List<String> getVarList(String kind){
	List<String> varList = new LinkedList<String>();
	String encKind = "";
	String content = "";
	try {
		encKind = URLEncoder.encode(kind, "gbk");
		Parser parser = new Parser();
		parser.setURL(KindUrl+encKind+".html");
		TagNameFilter tagNameFilter = new TagNameFilter("table");
		NodeList tableList = parser.extractAllNodesThatMatch(tagNameFilter);
		NodeList trNodeList = tableList.elementAt(0).getChildren();
		for(int i=0;i<trNodeList.size();i++){
			NodeList tdNodeList = trNodeList.elementAt(i).getChildren();
			if (tdNodeList != null) {
				for(int j=0;j<tdNodeList.size();j++){
					String tdString = tdNodeList.elementAt(j).toPlainTextString();
					if (!tdNodeList.elementAt(j).getText().contains("td")) {
						continue;
					}else if (!tdString.equals("") && tdString != null && !tdString.equals("&nbsp")) {
						varList.add(tdString.trim());
					}					
				}
			}
		}
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	} catch (ParserException e) {
		// TODO: handle exception
		e.printStackTrace();
		return null;
	}

	return varList;
}


}
