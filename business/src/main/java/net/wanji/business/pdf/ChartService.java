package net.wanji.business.pdf;

import net.wanji.business.exercise.dto.evaluation.TrendChange;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-28 8:47 上午
 */
@Service
public class ChartService {

    public ByteArrayOutputStream generateChart(String title, List<TrendChange> trendChanges) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(TrendChange data: trendChanges){
            Integer time = data.getTime();
            Double value = data.getValue();
            dataset.addValue(value, "Values", time);
        }

        JFreeChart lineChart = ChartFactory.createLineChart(title, "Time", "Value", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(outputStream, lineChart, 640, 480);
        return outputStream;
    }
}
