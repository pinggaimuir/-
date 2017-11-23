package cn.gao.commom;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by tarena on 2016/10/19.
 */
public class POITest {
    public static void main(String[] args) throws IOException {
        //创建一个excel文件
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("sheet1");//新建sheet对象
        //在sheet中新建一行，参数为行号，（第一行，此处可想想成数组）
        HSSFRow row=sheet.createRow(0);
        //在row中创建一各单元格，参数为列号
        HSSFCell cell=row.createCell(0);
        cell.setCellValue(1);//设置cell的整形类型值
        row.createCell(1).setCellValue("test");
        row.createCell(2).setCellValue(1.2);
        row.createCell(3).setCellValue(new Date().toLocaleString());
        row.createCell(4).setCellValue(true);
        //新建cell的样式
        HSSFCellStyle cellStyle=wb.createCellStyle();
        //设置cell的样式为定值日期格式
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        HSSFCell dcell=row.createCell(5);
        dcell.setCellValue(new Date());
        dcell.setCellStyle(cellStyle);
        sheet.createFreezePane(4,5);
        //合并单元格
        sheet.addMergedRegion(new Region(1,(short)1,2,(short)4));

        //创建一个单元格 并设置居中
        createCell(wb,sheet.createRow(1),0,HSSFCellStyle.ALIGN_CENTER_SELECTION,"wode");
        FileOutputStream fos=new FileOutputStream("test1.xls");
        wb.write(fos);
        fos.close();

    }
    private static void createCell(HSSFWorkbook wb,HSSFRow row,int col,short align,String val){
        HSSFCell cell=row.createCell(col);
        cell.setCellValue(val);
        HSSFCellStyle style=wb.createCellStyle();

        //设置对其格式
        style.setAlignment(align);
        cell.setCellStyle(style);
    }
}
