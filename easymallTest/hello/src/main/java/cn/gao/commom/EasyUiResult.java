package cn.gao.commom;

import java.util.List;

/**
 * easyUI 所需要的结果集对象
 * Created by tarena on 2016/10/10.
 */
public class EasyUiResult {
    private long total;
    private List<?> rows;

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
