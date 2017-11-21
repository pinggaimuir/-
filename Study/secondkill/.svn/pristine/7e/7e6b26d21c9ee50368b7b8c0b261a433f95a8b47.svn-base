package cn.futures.data.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;

public class HtmlNodeListUtil {
	private HtmlNodeListUtil(){}
	
	private static final String delimiter = ",";
	private static final Set<String> blankSymbols = new HashSet<String>(){
		{
			add("&nbsp;");
			add("-");
		}
	};
	
	private static boolean isBlank(String str){
		for (String blankSymbol:blankSymbols)
			if (blankSymbol.equals(str))
				return true;
		return false;
	}
	
	private static String getFormatCellStr(NodeList list, int index){
		String cellStr = list.elementAt(index).toPlainTextString().trim().replaceAll(delimiter, "");
		return isBlank(cellStr) ? "" : cellStr;
	}
	
	private static void formatListWithSpecifyStr(List<Integer> list, String str){
		int delNum = 0, size = list.size();
		for (int i = 0;i < str.length();++ i)
			if (i < size && str.charAt(i) == '0')
				list.remove(i - delNum ++);
	}
	
	private static int getNodeIndexByNonEmptyRank(NodeList list, int nonEmptyRank){
		int colIndex = firstNonEmptyNodeIndex(list);
		for (int i = 0;i < nonEmptyRank-1 && colIndex >= 0;++ i){
			colIndex = nextNonEmptyNodeIndex(list, colIndex);
		}
		return colIndex;
	}
	
	public static int firstNonEmptyNodeIndex(NodeList list){
		return nextNonEmptyNodeIndex(list, -1);
	}
	
	public static int nextNonEmptyNodeIndex(NodeList list, int thisNodeIndex){
		if (null == list || thisNodeIndex < -1 || thisNodeIndex >= list.size())
			return -1;
		for (int i = thisNodeIndex+1;i < list.size();++ i)
			if (null != list.elementAt(i) && null != list.elementAt(i).getChildren())
				return i;
		return -1;
	}
	
	public static Node firstNonEmptyNode(NodeList list){
		if (null == list)
			return null;
		for (int i = 0;i < list.size();++ i)
			if (null != list.elementAt(i) && null != list.elementAt(i).getChildren())
				return list.elementAt(i);
		return null;
	}
	
	public static Node nextNonEmptyNode(Node thisNode){
		if (null == thisNode)
			return null;
		while (true){
			thisNode = thisNode.getNextSibling();
			if (null == thisNode)
				return null;
			if (thisNode.getChildren() != null)
				return thisNode;
		}
	}
	
	/**
	 * 从NodeList表示的表格中获取第一个非空行中第一个非空单元格的内容String
	 * @param table
	 * @return String
	 */
	public static String getTableFirstCellStr(NodeList table){
		Node firstrow = firstNonEmptyNode(table);
		Node firstCell = firstNonEmptyNode(firstrow.getChildren());
		if (null == firstCell || null == firstCell.getChildren())
			return null;
		return firstCell.toPlainTextString();
	}
	
	/**
	 * 获取NodeList表示的表格中所有非空element的顺序索引
	 * @param list
	 * @return List<Integer>
	 */
	public static List<Integer> getNonEmptyNodeIndexList(NodeList list){
		List<Integer> indexList = new ArrayList<Integer>();
		for (int index = firstNonEmptyNodeIndex(list);
				index >= 0;
				index = nextNonEmptyNodeIndex(list, index))
			indexList.add(index);
		return indexList;
	}
	
	/**
	 * 将Htmlparser得到的代表一行的NodeList转换为String返回
	 * @param rowList
	 * @return String
	 * 		同一行的不同单元格用','分隔
	 * 		原本单元格中的','将被去除
	 */
	public static String row2Str(NodeList rowList){
		int cellIndex = firstNonEmptyNodeIndex(rowList);
		if (cellIndex < 0)
			return "";
		StringBuffer rowStr = new StringBuffer();
		while (true){
			rowStr.append(getFormatCellStr(rowList, cellIndex));
			cellIndex = nextNonEmptyNodeIndex(rowList, cellIndex);
			if (cellIndex < 0)
				return rowStr.toString();
			rowStr.append(delimiter);
		}
	}
	
	private static String row2Str_Format(NodeList rowList, List<Integer> indexList){
		StringBuffer rowStr = new StringBuffer();
		for (int i = 0;i < indexList.size();++ i){
			rowStr.append(getFormatCellStr(rowList, indexList.get(i)));
			rowStr.append(delimiter);
		}
		if (rowStr.length() == 0)
			return "";
		return rowStr.substring(0, rowStr.length()-1);
	}
	
	/**
	 * 将Htmlparser得到的NodeList表格转换为String返回，可定制行列的取舍
	 * 		如输入参数：(tableList,"00110","010")将返回tableList对应的表格String,同时舍弃1、2、5行以及1、3列
	 * @param tableList
	 * @param rowSpecifyStr
	 * 使用01字符串表示行的取舍，用0标明的行不进行转换，用1标明或超出长度部分对应的行进行转换
	 * @param colSpecifyStr
	 * 01字符串，意义同上
	 * @return String
	 * 		同一行的不同单元格用','分隔,不同行之间用'\n'分隔
	 * 		原本单元格中的','将被去除
	 */
	public static String table2Str_SpecifyRowsCols(NodeList tableList, String rowSpecifyStr, String colSpecifyStr){
		if (null == tableList)
			return null;

		StringBuffer tableStr = new StringBuffer();
		List<Integer> rowIndexList =  getNonEmptyNodeIndexList(tableList);
		formatListWithSpecifyStr(rowIndexList, rowSpecifyStr);		
		if (rowIndexList.size() <= 0)
			return "";
		
		NodeList topSpecifyrow = tableList.elementAt(rowIndexList.get(0)).getChildren();
		List<Integer> colIndexList = getNonEmptyNodeIndexList(topSpecifyrow);
		formatListWithSpecifyStr(colIndexList, colSpecifyStr);
		
		for (int i = 0;i < rowIndexList.size();++ i){
			tableStr.append(row2Str_Format(tableList.elementAt(rowIndexList.get(i)).getChildren(), colIndexList));
			tableStr.append("\n");
		}
		System.out.println(tableStr);
		return tableStr.toString();
	}
	
	/**
	 * 将Htmlparser得到的NodeList表格转换为String返回，可定制列的取舍
	 * 		如输入参数：(tableList,"00110")将返回tableList对应的表格String,同时舍弃1、2、5列
	 * @param tableList
	 * @param colSpecifyStr
	 * 使用01字符串表示行的取舍，用0标明的行不进行转换，用1标明或超出长度部分对应的行进行转换
	 * @return String
	 * 		同一行的不同单元格用','分隔,不同行之间用'\n'分隔
	 * 		原本单元格中的','将被去除
	 */
	public static String table2Str_SpecifyCols(NodeList tableList, String colSpecifyStr){
		return table2Str_SpecifyRowsCols(tableList, "", colSpecifyStr);
	}
	
	/**
	 * 将Htmlparser得到的NodeList表格转换为String返回，可定制行的取舍
	 * 		如输入参数：(tableList,"00110")将返回tableList对应的表格String,同时舍弃1、2、5行
	 * @param tableList
	 * @param rowSpecifyStr
	 * 使用01字符串表示行的取舍，用0标明的行不进行转换，用1标明或超出长度部分对应的行进行转换
	 * @return String
	 * 		同一行的不同单元格用','分隔,不同行之间用'\n'分隔
	 * 		原本单元格中的','将被去除
	 */
	public static String table2Str_SpecifyRows(NodeList tableList, String rowSpecifyStr){
		return table2Str_SpecifyRowsCols(tableList, rowSpecifyStr, "");
	}
	
	/**
	 * 将Htmlparser得到的NodeList表格转换为String返回
	 * @param tableList
	 * @return String
	 * 		同一行的不同单元格用','分隔,不同行之间用'\n'分隔
	 * 		原本单元格中的','将被去除
	 */
	public static String table2Str(NodeList tableList){
		return table2Str_SpecifyRowsCols(tableList, "", "");
	}
	
	public static List<String> col2ListExcludeTitleRow(NodeList tableList, int colNum){
		List<String> colList = new LinkedList<String>();
		
		List<Integer> rowIndexList =  getNonEmptyNodeIndexList(tableList);
		if (rowIndexList.size() <= 1)
			return colList;
		NodeList topRow = tableList.elementAt(rowIndexList.get(1)).getChildren();
		int colIndex = getNodeIndexByNonEmptyRank(topRow, colNum);
		for (int i = 1;i < rowIndexList.size();++ i){
			NodeList rowList = tableList.elementAt(rowIndexList.get(i)).getChildren();
			colList.add(getFormatCellStr(rowList, colIndex));
		}
		return colList;
	}
	
}
