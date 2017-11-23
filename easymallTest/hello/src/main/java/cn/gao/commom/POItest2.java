package cn.gao.commom;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tarena on 2016/10/19.
 */
public class POItest2 {
    public static void main(String[] args) throws IOException {
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("工作簿1");
        HSSFRow row0=sheet.createRow(0);
        HSSFCell cell0=createCell(wb,row0,0,"我的工作簿");
        HSSFCellStyle fontStyle=wb.createCellStyle();
//        fontStyle.setFont(new HSSFFont9);
        sheet.addMergedRegion(new Region(0,(short)0,1,(short)7));
        HSSFRow row=sheet.createRow(2);
        createCell(wb,row,0,"字段1");
        createCell(wb,row,1,"字段2");
        createCell(wb,row,2,"字段3");
        createCell(wb,row,3,"字段4");
        createCell(wb,row,4,"字段5");
        createCell(wb,row,5,"字段6");
        createCell(wb,row,6,"字段7");
        createCell(wb,row,7,"字段8");
        for (int i = 3; i <20 ; i++) {
            HSSFRow row2=sheet.createRow(i);
            for(int j=0;j<8;j++){
                String s="——";
                createCell(wb,row2,j,i+""+s+""+j);
            }
        }
        FileOutputStream in=new FileOutputStream("test1.xls");
        wb.write(in);
        in.close();
    }
    public static HSSFCell createCell(HSSFWorkbook wb,HSSFRow row,int col,String val){
        HSSFCell cell=row.createCell(col);
        cell.setCellValue(val);
        HSSFCellStyle style=wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
        cell.setCellStyle(style);
        return cell;
    }
}
