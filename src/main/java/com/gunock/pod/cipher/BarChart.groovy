package com.gunock.pod.cipher

import com.gunock.pod.utils.HelperUtil
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.CategoryChartBuilder
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.style.Styler

import javax.swing.*

class BarChart {

    static JFrame getChart(Map<Character, Integer> values, String title) {
        List<String> keys = HelperUtil.characterSetToStringList(values.keySet())
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) == '\n') {
                keys.set(i, "\\n")
            } else if (keys.get(i) == '\r') {
                keys.set(i, "\\r")
            } else if (keys.get(i) == ' ') {
                keys.set(i, "\\s")
            }
        }
        return getChart(keys, values.values() as List, title)
    }

    static JFrame getChart(List<String> keys, List<Integer> values, String title) {
        CategoryChart chart = createChart(keys, values, title)
        JFrame chartFrame = new SwingWrapper<>(chart).displayChart()
        chartFrame.setVisible(false)
        chartFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE)
        return chartFrame
    }

    static CategoryChart createChart(List<String> keys, List<Integer> values, String title) {
        // Create Chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Frequency Histogram")
                .xAxisTitle("Character")
                .yAxisTitle("Frequency")
                .build()

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE)
        chart.getStyler().setHasAnnotations(true)

        // Series
        chart.addSeries(title, keys, values)

        return chart
    }
}
