<%@page import="infoData.ItemInfo" %>
<%@page import="myWebSpider.GetItemFromWeb" %>
<%@ page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="utf-8"/>
    <script src="js/jquery-1.8.2.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="css/jdbase.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="css/jdpsearch20130409.css" media="all">
    <link rel="stylesheet" type="text/css" href="css/jdpop_compare.css" media="all"/>
    <script src="js/include.js" type="text/javascript"></script>
    <script src="js/search.js" type="text/javascript"></script>

    <style type="text/css">
        .skcolor_ljg {
            color: #FF0000;
        }

        #plist .reputation {
            display: inline;
        }
    </style>


    <title>商品搜索结果</title>
</head>
<body class="root61">
<%GetItemFromWeb getinfo = new GetItemFromWeb(request);%>
<div id="o-header-2013">
    <div class="w" id="header-2013">
        <div id="search-2013">
            <div class="i-search ld">
                <ul id="shelper" class="hide"></ul>
                <div class="form">
                    <form action="search.jsp" accept-charset="utf-8">
                        <input class="text" type="text" name="key"
                               value="<%=new String(request.getParameter("key").getBytes("ISO8859-1"),"UTF-8")%>"/>
                        <input class="button" type=submit value="搜索">
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>


<div class="w main">
    <div id="srp-page">
        <div id="filter" class="section" bx-name="filter" bx-path="components/filter/" bx-behavior="true">
            <div class="filter-box">
                <div class="filter-row filter-b2c-seller"><span class="filter-cat">商家</span>

                    <div class="filter-group b2c-group">
                        <%if (getinfo.pagedata.getFromB2C == 1 || (getinfo.pagedata.getFromJd == 1&&getinfo.pagedata.getFromAz == 1)) {%>
                        <a class="filter-item selected " id="b2c" onclick="selectSeller(this)"><s class="icon-check"></s>B2C商城</a>
                        <%} else {%>
                        <a class="filter-item "  id="b2c" onclick="selectSeller(this)"><s class="icon-check"></s>B2C商城</a>
                        <%}%>
                <span
                        class="filter-item icon-triangle" title=""><s class="icon-check"></s></span></div>
                    <div class="filter-group b2c-all-group">
                        <div class="filter-group filter-list more-group">
                            <%if (getinfo.pagedata.getFromB2C == 1 || getinfo.pagedata.getFromJd == 1) {%>
                            <a id="jd" class="filter-item selected " onclick="selectSeller(this)"><s class="icon-check"></s>京东商城</a>
                            <%} else {%>
                            <a id="jd" class="filter-item "  onclick="selectSeller(this)"><s class="icon-check"></s>京东商城</a>
                            <%}%>
                            <%if (getinfo.pagedata.getFromB2C == 1 || getinfo.pagedata.getFromAz == 1) {%>
                            <a class="filter-item selected "  onclick="selectSeller(this)" id="az"><s class="icon-check"></s>亚马逊</a>
                            <%} else {%>
                            <a class="filter-item "  onclick="selectSeller(this)" id="az"><s class="icon-check"></s>亚马逊</a>
                            <%}%>
                        </div>
                        <span class="filter-item toggle-scrollbar" title=""><s class="icon-check"></s></span></div>
                </div>
                <div class="filter-row filter-tao-seller">
                    <div class="filter-group tao-group">
                        <%if (getinfo.pagedata.getFromTb == 1) {%>
                        <a class="filter-item selected "  onclick="selectSeller(this)" id="tb"><s class="icon-check"></s>淘宝网</a></div>
                    <%} else {%>
                    <a class="filter-item "  onclick="selectSeller(this)" id="tb"><s class="icon-check"></s>淘宝网</a>
                    <%}%>
                </div>

            </div>
        </div>
    </div>
    <div class="section">
        <div id="toolbar" bx-name="toolbar" bx-path="components/toolbar/" bx-behavior="true"
             class="toolbar r-c4">
            <div class="top-filterbar-upper-part clearfix">
                <ul class="top-left-filter-list">


                    <li class="top-left-filter">
                        <a data-stat="userid=407570624&amp;at_alitrackid=www.etao.com&amp;lf_acfrom=0&amp;rn=2bc30776acca9073d878aa924933d951&amp;url=http%3A%2F%2Fs.etao.com%2Fsearch%3Fq%3D%25D4%25CB%25B6%25AF%25D0%25AC%26tab%3Dall%26style%3Dgrid&amp;lf_aclog=null-null-36-commend-0&amp;stats_click=rank_type%3Acommend"
                           class=" selected first-filter top-left-filter-link"
                           href="#"
                           data-sorting-type="0">
                            默认排序 </a>
                    </li>


                    <li class="top-left-filter">
                        <a data-stat="userid=407570624&amp;at_alitrackid=www.etao.com&amp;lf_acfrom=0&amp;rn=2bc30776acca9073d878aa924933d951&amp;url=http%3A%2F%2Fs.etao.com%2Fsearch%3Fq%3D%25D4%25CB%25B6%25AF%25D0%25AC%26tab%3Dall%26style%3Dgrid&amp;lf_aclog=null-null-36-commend-0&amp;stats_click=rank_type%3Asale-desc"
                           class=" top-left-filter-link"
                           href="#"
                           data-sorting-type="1">
                            销量 </a>
                    </li>


                    <li class="top-left-filter">
                        <%if(getinfo.pagedata.sortByPrice == 1){%>
                        <a class=" top-left-filter-link price-sort" href="javascript:sortByPrice()" data-sorting-type="3">
                            价格 <span class="price-icon price-arrow-up"  id="sort"></span> </a>
                        <%}else{%>
                        <a class=" top-left-filter-link price-sort" href="#" onclick="sortByPrice()" data-sorting-type="3">
                            价格 <span class="price-icon not-clicked-price-icon" id="sort"></span> </a>
                        <%}%>
                    </li>

                </ul>
                <ul class="top-right-filter-list">
                    <li class="top-right-filter origin-place-filter">
                        <div href="#" class="origin-place folded">
                            发货地筛选<span class="origin-place-icon"></span>
                        </div>
                    </li>
                </ul>

            </div>
            <%

                if (!getinfo.pagedata.key_utf8.isEmpty()) {
                    List<ItemInfo> prodInfoList = getinfo.getItemInfoList();
                    int size = prodInfoList.size();
                    if (size > 0) {
                        /**
                         * 打印商品项列表
                         */
                        out.println("<div class=\"m psearch\" id=\"plist\">");
                        out.println("<ul class=\"list-h clearfix\">");
                        for (int i = 0; i < size; i++) {
                            ItemInfo itemInfo = prodInfoList.get(i);
                            if (itemInfo != null) {
                                out.println("<li>");
                                out.println("<div class=\"p-img\">");
                                out.println("<a target=\"_blank\"href=\"" + itemInfo.itemUrl + "\">");
                                out.println("<img width=\"160\" height=\"160\" src=\"" + itemInfo.itemImage + " \"/>");
                                out.println("</a></div><div class=\"p-name\">");
                                out.println("<a target=\"_blank\" href=\"" + itemInfo.itemUrl + "\">");
                                out.println(itemInfo.itemIntro);
                                out.println("<font style='color:#ff0000;margin-left:5px;' class='adwords' ></font>");
                                out.println("</a></div><div class=\"p-price\" ><strong>￥" + itemInfo.itemPrice + "</strong></div></li>");
                            } else {
                                break;
                            }
                        }
                        out.println("</ul>");
                        out.println("</div>");
                        /**
                         * 分页栏
                         */
                        int nextpagenum = 0;//下一页页码
                        int prepagenum = 0;//上一页页码
                        int totalnum = 0;//总页数
                        String nextpage = null;//下一页url
                        String prepage = null;//上一页url
                        if (getinfo.pagedata.jdpage > 0) {
                            totalnum += getinfo.pagedata.jdpage;
                        }
                        if (getinfo.pagedata.azpage > 0) {
                            totalnum += getinfo.pagedata.azpage;
                        }
                        if (getinfo.pagedata.tbpage > 0) {
                            totalnum += getinfo.pagedata.tbpage;
                        }
                        if (getinfo.pagedata.page > 1) {
                            prepagenum = getinfo.pagedata.page - 1;//上一页页码
                        }
                        if (getinfo.pagedata.page < totalnum) {
                            nextpagenum = getinfo.pagedata.page + 1;//下一页页码
                        }

                        out.println("<div class=\"m clearfix\" id=\"bottom_pager\">");
                        out.println("<div id=\"pagin-btm\" class=\"pagin fr\" clstag=\"search|keycount|search|pre-page2\">");

                        if (prepagenum > 0) {

                            prepage = "search.jsp?key=" + getinfo.pagedata.key_utf8 + "&page=" + prepagenum + "&JD=" + getinfo.pagedata.jdpage + "&AZ=" + getinfo.pagedata.azpage
                                    + "&TB=" + getinfo.pagedata.tbpage;
//                            prepage = "search.jsp?key=" + getinfo.pagedata.key_utf8  + "&page=" + prepagenum;
                            out.println("<a class=\"prev\" charset=\"utf-8\"  href=\"" + prepage + "\" target=\"_self\" rel=\"nofollow\">上一页<b></b></a>");
                        }

                        if (nextpagenum > 0) {

                            nextpage = "search.jsp?key=" + getinfo.pagedata.key_utf8 + "&page=" + nextpagenum + "&JD=" + getinfo.pagedata.jdpage + "&AZ=" + getinfo.pagedata.azpage
                                    + "&TB=" + getinfo.pagedata.tbpage;
//                            nextpage = "search.jsp?key=" + getinfo.pagedata.key_utf8 + "&page=" + nextpagenum ;
                            out.println("<a class=\"next\" charset=\"utf-8\"  href=\"" + nextpage + "\" target=\"_self\" rel=\"nofollow\">下一页<b></b></a>");
                        }

                        out.println("<span class=\"page-skip\"><em>&nbsp;&nbsp;共" + totalnum + "页&nbsp;&nbsp;&nbsp;&nbsp;</em>");
                        out.println("<em>&nbsp;&nbsp;当前第" + getinfo.pagedata.page + "页</em>");
                        out.println("</span></div></div>");
                    } else {
                        out.println("没有搜索到商品！");
                    }
                    /**
                     * 底部搜索框
                     */
                    out.println("<div id=\"re-search\" class=\"m\">");
                    out.println("<dl><dt>重新搜索</dt><dd>");
                    out.println("<form action=\"search.jsp\" accept-charset=\"utf-8\">");
                    out.println("<input type=\"text\" class=\"text\" name=\"key\" />");
                    out.println("<input type=submit  class=\"button\" value=\"搜索\" >");
                    out.println("</form>");
                    out.println("</dd>");
                } else {
                    out.println("请输入关键字查询");
                }


            %>
        </div>
    </div>
</div>
</div>

</body>
</html>
