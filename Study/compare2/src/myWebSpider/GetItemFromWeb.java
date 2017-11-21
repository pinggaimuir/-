/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myWebSpider;

import infoData.ItemInfo;
import infoData.PageInfoData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import myUtils.CommonUtil;
import myUtils.SimilarityUtil;
import myWebSpider.amazonSpider.AZItemInfoList;
import myWebSpider.jdSpider.JDItemInfoList;
import myWebSpider.taobaoSpider.TaobaoItemInfoList;


/**
 * 业务逻辑层的核心类。 该类生成两大电商网站的URL，将URL提交给数据爬取层；同时业务逻辑层还将数据爬取层返回的商品数据进行简单的处理，将其返回给视图层。
 *
 * @author Sammy
 */
public class GetItemFromWeb {
    /**
     * 页面请求
     */
    HttpServletRequest request;
    /**
     * 从页面传递过来的请求信息
     */
    public PageInfoData pagedata = new PageInfoData();

    /**
     * 构造方法
     *
     * @param request
     * @throws UnsupportedEncodingException
     */
    public GetItemFromWeb(HttpServletRequest request) {
        String page = request.getParameter("page");
        if (page != null)
            pagedata.page = Integer.parseInt(page);
        else
            pagedata.page = 1;
//        pagedata.key = request.getParameter("key").replaceAll(" ", "+");
        pagedata.key = request.getParameter("key");
        String b2c = request.getParameter("getFromB2C");
        if (b2c != null) {
            pagedata.getFromB2C = Integer.parseInt(b2c);
        }
        String jd = request.getParameter("getFromJd");
        if (jd != null) {
            pagedata.getFromJd = Integer.parseInt(jd);
        }
        String az = request.getParameter("getFromAz");
        if (az != null) {
            pagedata.getFromAz = Integer.parseInt(az);
        }
        String tb = request.getParameter("getFromTb");
        if (tb != null) {
            pagedata.getFromTb = Integer.parseInt(tb);
        }
        String sbp = request.getParameter("sortByPrice");
        if (sbp != null) {
            pagedata.sortByPrice = Integer.parseInt(sbp);
        }
        try {
            pagedata.key_utf8 = new String(pagedata.key.getBytes("ISO8859-1"), "UTF-8");
            pagedata.key_gbk = new String(pagedata.key_utf8.getBytes("UTF-8"), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.request = request;
        System.out.println("当前页码：" + pagedata.page);
    }

    /**
     * 该方法用以获取用户请求的商品信息并将其作为返回值返回。
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<ItemInfo> getItemInfoList() {
        try {
            System.out.println("搜索关键词：" + pagedata.key_utf8);
            if (pagedata.page == 1) {
                //返回第一页商品信息列表
                return getFirstPageItemInfoList();

            } else {
                //返回其他页商品信息列表
                return getItemInfoListFromNeighbor();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过url获取商品列表
     *
     * @param urljd
     * @param urlaz
     * @param urltb
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getFromURLs(String urljd, String urlaz, String urltb) throws IOException, InterruptedException {
        List<ItemInfo> list = new ArrayList<ItemInfo>();
        if (this.pagedata.getFromB2C == 1 || (this.pagedata.getFromJd == 1 && this.pagedata.getFromAz == 1)) {
            if (this.pagedata.getFromTb == 1) {
                list.addAll(this.getFromAllSellers(urljd, urlaz, urltb));
            } else {
                list.addAll(this.getFromB2C(urljd, urlaz));
            }
        } else if (this.pagedata.getFromJd == 1) {
            if (this.pagedata.getFromTb == 1) {
                list.addAll(this.compareSimilarity(this.getFromTb(urltb), this.getFromJd(urljd)));
            } else {
                list.addAll(this.getFromJd(urljd));
            }
        } else if (this.pagedata.getFromAz == 1) {
            if (this.pagedata.getFromTb == 1) {
                list.addAll(this.compareSimilarity(this.getFromTb(urltb), this.getFromAz(urlaz)));
            } else {
                list.addAll(this.getFromAz(urlaz));
            }
        }
        if (this.pagedata.getFromTb == 1) {
            list.addAll(this.getFromTb(urltb));
        }
        if (this.pagedata.getFromB2C == 0 && this.pagedata.getFromJd == 0 && this.pagedata.getFromAz == 0 && this.pagedata.getFromTb == 0) {
            list.addAll(this.getFromAllSellers(urljd, urlaz, urltb));
        }
        if (this.pagedata.sortByPrice == 1)
            this.sortByPrice(list);
        return list;
    }


    /**
     * 从所有商家中获取商品列表
     *
     * @param urljd
     * @param urlaz
     * @param urltb
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getFromAllSellers(String urljd, String urlaz, String urltb) throws IOException, InterruptedException {
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        List<ItemInfo> jdItemInfoList = new ArrayList<ItemInfo>();
        if (urljd != null) {
            jdItemInfoList = new JDItemInfoList(pagedata, urljd).getItemInfoList();
        }
        List<ItemInfo> azItemInfoList = new ArrayList<ItemInfo>();
        if (urlaz != null) {
            azItemInfoList = new AZItemInfoList(pagedata, urlaz).getItemInfoList();
        }
        List<ItemInfo> tbItemInfoList = new ArrayList<ItemInfo>();
        if (urltb != null) {
            tbItemInfoList = new TaobaoItemInfoList(pagedata, urltb).getItemInfoList();
        }
        int jdsize = jdItemInfoList.size();
        int azsize = azItemInfoList.size();
        int tbsize = tbItemInfoList.size();

        List<ItemInfo> maxList = null;

        if (CommonUtil.getMax(jdsize, azsize, tbsize) == jdsize) {
            maxList = jdItemInfoList;
            for (int i = 0; i < maxList.size(); i++) {
//              itemInfoList.add(maxList.get(i));
                itemInfoList.add(this.getSimilarity(this.pagedata.key_utf8,maxList));
                maxList.remove(itemInfoList.get(itemInfoList.size() - 1));
                String itemIntro = itemInfoList.get(itemInfoList.size() - 1).itemIntro;
                if (i < azsize) {
//                    itemInfoList.add(azItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, azItemInfoList));
                    azItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
                if (i < tbsize) {
//                    itemInfoList.add(tbItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, tbItemInfoList));
                    tbItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
            }
        } else if (CommonUtil.getMax(jdsize, azsize, tbsize) == azsize) {
            maxList = azItemInfoList;
            for (int i = 0; i < maxList.size(); i++) {
//                itemInfoList.add(maxList.get(i));
                itemInfoList.add(this.getSimilarity(this.pagedata.key_utf8,maxList));
                maxList.remove(itemInfoList.get(itemInfoList.size() - 1));
                String itemIntro = itemInfoList.get(itemInfoList.size() - 1).itemIntro;
                if (i < jdsize) {
//                    itemInfoList.add(jdItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, jdItemInfoList));
                    jdItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
                if (i < tbsize) {
//                    itemInfoList.add(tbItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, tbItemInfoList));
                    tbItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
            }
        } else {
            maxList = tbItemInfoList;
//            System.out.println("jdsize:"+jdsize);
//            System.out.println("azsize:"+azsize);
//            System.out.println("tbsize:"+tbsize);
            for (int i = 0; i < tbsize; i++) {
//                itemInfoList.add(maxList.get(i));
                itemInfoList.add(this.getSimilarity(this.pagedata.key_utf8,maxList));
                String itemIntro = itemInfoList.get(itemInfoList.size() - 1).itemIntro;
                maxList.remove(itemInfoList.get(itemInfoList.size() - 1));
//                System.out.println("i:"+i);
                if (i < azsize) {
//                    itemInfoList.add(azItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, azItemInfoList));
                    azItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
                if (i < jdsize) {
//                    itemInfoList.add(jdItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, jdItemInfoList));
                    jdItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
            }
        }
//        this.sortByPrice(itemInfoList);
        return itemInfoList;
    }

    /**
     * 从所有B2C商家中获取商品
     *
     * @param urljd
     * @param urlaz
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getFromB2C(String urljd, String urlaz) throws IOException, InterruptedException {
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        List<ItemInfo> jdItemInfoList = new JDItemInfoList(pagedata, urljd).getItemInfoList();
        List<ItemInfo> azItemInfoList = new AZItemInfoList(pagedata, urlaz).getItemInfoList();
        int jdsize = jdItemInfoList.size();
        int azsize = azItemInfoList.size();

        List<ItemInfo> maxList = null;

        if (CommonUtil.getMax(jdsize, azsize) == jdsize) {
            maxList = jdItemInfoList;
            for (int i = 0; i < maxList.size(); i++) {
//                itemInfoList.add(maxList.get(i));
                itemInfoList.add(this.getSimilarity(this.pagedata.key_utf8,maxList));
                maxList.remove(itemInfoList.get(itemInfoList.size() - 1));
                String itemIntro = itemInfoList.get(itemInfoList.size() - 1).itemIntro;
                if (i < azsize) {
//                    itemInfoList.add(azItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, azItemInfoList));
                    azItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
            }
        } else {
            maxList = azItemInfoList;
            for (int i = 0; i < maxList.size(); i++) {
//                itemInfoList.add(maxList.get(i));
                itemInfoList.add(this.getSimilarity(this.pagedata.key_utf8,maxList));
                maxList.remove(itemInfoList.get(itemInfoList.size() - 1));
                String itemIntro = itemInfoList.get(itemInfoList.size() - 1).itemIntro;
                if (i < jdsize) {
//                    itemInfoList.add(jdItemInfoList.get(i));
                    itemInfoList.add(this.getSimilarity(itemIntro, jdItemInfoList));
                    jdItemInfoList.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
            }
        }

        return itemInfoList;
    }

    /**
     * 将两个单独的列表合并成一个，并按照产品比较的顺序排序
     *
     * @param l1
     * @param l2
     * @return
     */
    private List<ItemInfo> compareSimilarity(List<ItemInfo> l1, List<ItemInfo> l2) {
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        int size1 = l1.size();
        int size2 = l2.size();
        List<ItemInfo> maxList = null;

        if (CommonUtil.getMax(size1, size2) == size1) {
            maxList = l1;
            for (int i = 0; i < maxList.size(); i++) {
//                itemInfoList.add(maxList.get(i));
                itemInfoList.add(this.getSimilarity(this.pagedata.key_utf8,maxList));
                maxList.remove(itemInfoList.get(itemInfoList.size() - 1));
                String itemIntro = itemInfoList.get(itemInfoList.size() - 1).itemIntro;
                if (i < size2) {
                    itemInfoList.add(this.getSimilarity(itemIntro, l2));
                    l2.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
            }
        } else {
            maxList = l2;
            for (int i = 0; i < maxList.size(); i++) {
//                itemInfoList.add(maxList.get(i));
                itemInfoList.add(this.getSimilarity(this.pagedata.key_utf8,maxList));
                maxList.remove(itemInfoList.get(itemInfoList.size() - 1));
                String itemIntro = itemInfoList.get(itemInfoList.size() - 1).itemIntro;
                if (i < size1) {
                    itemInfoList.add(this.getSimilarity(itemIntro, l1));
                    l1.remove(itemInfoList.get(itemInfoList.size() - 1));
                }
            }
        }

        return itemInfoList;
    }

    /**
     * 从京东中获取商品
     *
     * @param urljd
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getFromJd(String urljd) throws IOException, InterruptedException {
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        List<ItemInfo> jdItemInfoList = new JDItemInfoList(pagedata, urljd).getItemInfoList();
        int jdsize = jdItemInfoList.size();
        for (int i = 0; i < jdsize; i++) {
            itemInfoList.add(jdItemInfoList.get(i));
        }
        return itemInfoList;
    }

    /**
     * 从亚马逊中获取商品
     *
     * @param urlaz
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getFromAz(String urlaz) throws IOException, InterruptedException {
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        List<ItemInfo> azItemInfoList = new AZItemInfoList(pagedata, urlaz).getItemInfoList();
        int azsize = azItemInfoList.size();
        for (int i = 0; i < azsize; i++) {
            itemInfoList.add(azItemInfoList.get(i));
        }
        return itemInfoList;
    }

    /**
     * 从淘宝中获取商品
     *
     * @param urltb
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getFromTb(String urltb) throws IOException, InterruptedException {
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        List<ItemInfo> tbItemInfoList = new TaobaoItemInfoList(pagedata, urltb).getItemInfoList();
        int tbsize = tbItemInfoList.size();
        for (int i = 0; i < tbsize; i++) {
            itemInfoList.add(tbItemInfoList.get(i));
        }
        return itemInfoList;
    }


    /**
     * 从列表list中找到与itemIntro相似度最高的ItemInfo
     *
     * @param itemIntro
     * @param list
     * @return 相似度最高的ItemInfo
     */
    private ItemInfo getSimilarity(String itemIntro, List<ItemInfo> list) {
        ItemInfo itemInfo = null;
        /**
         * 找到list中所有的itemIntro与字符串itemIntro的相似度，保存在lens数组中
         */
        double lens[] = new double[list.size()];
        for (int i = 0; i < list.size() - 1; i++) {
            lens[i] = SimilarityUtil.sim(itemIntro, list.get(i).itemIntro);
        }
        /**
         * 遍历出最大的相似度maxLen
         */
        double maxLen = 0.0;
        for(int i = 0;i<lens.length;i++){
            if (maxLen<lens[i]){
                maxLen = lens[i];
            }
        }
        /**
         * 遍历出最大的相似度的索引maxLenIndex
         */
        int maxLenIndex = 0;
        for(int i = 0;i<lens.length;i++){
            if(maxLen == lens[i]){
                maxLenIndex = i;
            }
        }
        itemInfo = list.get(maxLenIndex);
        return itemInfo;
    }


    /**
     * 当加载第一页时用此方法获取商品列表
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getFirstPageItemInfoList()
            throws IOException, InterruptedException {
        String pageurljd = "http://search.jd.com/Search?keyword=" + pagedata.key_gbk + "&enc=utf-8";
        String pageurlaz = "http://www.amazon.cn/s/ref=nb_sb_noss_1?__mk_zh_CN=亚马逊网站&url=search-alias%3Daps&field-keywords=" + pagedata.key_utf8;
//        String pgurltb = "http://s.taobao.com/search?q=" + this.pagedata.key_gbk + "&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index";
        String pgurltb = "http://s.taobao.com/search?q=" + this.pagedata.key_utf8;

        return getFromURLs(pageurljd, pageurlaz, pgurltb);
    }

    /**
     * 当点击下一页、上一页时用此方法获取商品列表
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<ItemInfo> getItemInfoListFromNeighbor()
            throws IOException, InterruptedException {
        int jdpage = Integer.parseInt(request.getParameter("JD"));
        int azpage = Integer.parseInt(request.getParameter("AZ"));
        int tbpage = Integer.parseInt(request.getParameter("TB"));
        String pageurljd = null;
        String pageurlaz = null;
        String pgrultb = null;
        if (jdpage > 0 && this.pagedata.page < jdpage) {
            pageurljd = "http://search.jd.com/search?keyword=" + pagedata.key_utf8 + "&qr=&qrst=UNEXPAND&et=&rt=1&area=17&page=" + pagedata.page;
        }
        if (azpage > 0 && this.pagedata.page < azpage) {
            pageurlaz = "http://www.amazon.cn/s/ref=sr_pg_2?page=" + pagedata.page + "&keywords=" + pagedata.key_utf8 + "&ie=UTF8";
        }
        if (tbpage > 0 && this.pagedata.page < tbpage) {
            pgrultb = "http://s.taobao.com/search?q=" + this.pagedata.key_gbk + "&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&s=" + pagedata.page * 40;
        }
        return getFromURLs(pageurljd, pageurlaz, pgrultb);
    }


    /**
     * 按价格降序排序
     *
     * @param itemlist
     */
    private void sortByPrice(List<ItemInfo> itemlist) {
        System.out.println("开始排序！");
        if (itemlist.size() > 0) {
            Collections.sort(itemlist);
        }
    }


}

