package com.gunock.pod.cipher

import org.knowm.xchart.CategoryChart
import org.knowm.xchart.CategoryChartBuilder
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.style.Styler

class BarChart {

    static void main(String[] args) {
        displayChart(["a", "c", "d", "e", "f"] as List, [4, 5, 9, 6, 5] as List, "Test 1")
    }

    static void displayChart(Map<Character, Integer> values, String title) {
        final List<String> keys = HelperUtil.characterSetToStringList(values.keySet())
        displayChart(keys, values.values() as List, title)
    }

    static void displayChart(List<String> keys, List<Integer> values, String title) {
        CategoryChart chart = getChart(keys, values, title)
        new SwingWrapper<>(chart).displayChart()
    }

    static CategoryChart getChart(List<String> keys, List<Integer> values, String title) {

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
