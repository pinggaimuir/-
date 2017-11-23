package cn.gao.service;

import cn.gao.commom.EasyUiResult;

/**
 * Created by tarena on 2016/10/12.
 */
public interface ItemsService {
    EasyUiResult getEasyUiItemList(int total, int rows) throws Exception;
}
