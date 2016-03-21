package com.kalyter.ccwcc.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class ExcelHelper {

	public static boolean saveExcel(String path, JSONArray data) {
		File file = new File(path);
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("sheet-1",0);
            //下面是先找出这个data中的最长的一个元素的个数，然后以他的keySet作为整张表的首列名字
            int columnCount=0,column=0,row=0;
            JSONObject titleDemo = null;
            for (int i = 0; i < data.size(); i++) {
                JSONObject object = data.getJSONObject(i);
                if (object.size() > columnCount) {
                    columnCount=object.size();
                    titleDemo=object;
                }
            }
            for (String s:titleDemo.keySet()) {
                Label title=new Label(column,row,s);
                sheet.addCell(title);
                column++;
            }

            row=1;
			for (Object o:data) {
				JSONObject cell=(JSONObject)o;
                Set<String> set = cell.keySet();
                column=0;
                for (String s : set) {
                    Label label = new Label(column, row, cell.getString(s));
                    sheet.addCell(label);
                    column++;
                }
				row++;
			}
			workbook.write();
			workbook.close();
		} catch (IOException | WriteException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}