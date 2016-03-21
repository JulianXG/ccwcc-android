package com.kalyter.ccwcc.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Date;

public class BarChartUtilActivity {
	private Context mContext;

	public BarChartUtilActivity(Context mContext) {
		this.mContext = mContext;
	}

	public View getBarChartView(JSONArray array) {
		if (array == null) {
			return null;
		}
		XYMultipleSeriesDataset dataSet=new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int maxHeight=0;
		String[] titles = new String[]{"种类统计", "数量统计"};
		for (int i = 0; i < titles.length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			for (Object o : array) {
				JSONObject content = ((JSONObject) o);
				if (i == 0) {
					series.add(Double.parseDouble(content.getString("speciesCount")));
				} else if(i == 1){
					series.add(Double.parseDouble(content.getString("quantity")));
				}
				Integer quantity = content.getInteger("quantity");
				Integer speciesCount = content.getInteger("speciesCount");
				int tmp = Math.max(quantity, speciesCount);
				maxHeight = Math.max(tmp, maxHeight);
			}
			dataSet.addSeries(series.toXYSeries());
		}

		SimpleSeriesRenderer r =new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();

		r.setColor(Color.GREEN);
		renderer.addSeriesRenderer(r);
		renderer.setBarSpacing(0.1);
		renderer.setDisplayChartValues(true);

		renderer.setChartTitle("鸟种统计表");
		renderer.setYTitle("数量（只）");
		renderer.setXLabels(0);
		for (int i = 0; i < array.size(); i++) {
            String date = array.getJSONObject(i).getString("date");
            if (date == null) {
                date = "";
            }
            renderer.addTextLabel(i + 1, array.getJSONObject(i).getString("category") + "\n" + date);
        }
        renderer.setPanEnabled(true, false);
        renderer.setShowGrid(true);
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(maxHeight*1.1);

		return ChartFactory.getBarChartView(mContext,dataSet,renderer, BarChart.Type.DEFAULT);
	}



}