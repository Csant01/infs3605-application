package com.example.infs3605_app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class FragmentDashboardCosting extends Fragment {
    LineChart costChart;
    View v;
    BarChart barChart;
    ArrayList<Entry> lineList;
    LineDataSet lineDataSet;
    LineData lineData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard_costing, container, false);
        costChart = v.findViewById(R.id.dashboardCostLineChart);
        lineList = new ArrayList<>();
        lineList.add(new Entry(10f, 100f));
        lineList.add(new Entry(20f, 140f));
        lineList.add(new Entry(30f, 320f));
        lineList.add(new Entry(40f, 410f));
        lineList.add(new Entry(50f, 440f));
        lineList.add(new Entry(60f, 440f));
        lineList.add(new Entry(70f, 600f));
        lineList.add(new Entry(80f, 660f));
        lineDataSet  = new LineDataSet(lineList, "Event Costing");
        costChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("Event Costing per Week");
        description.setTextSize(15f);
        costChart.setDescription(description);
        lineData = new LineData(lineDataSet);
        costChart.setData(lineData);
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(Color.rgb(251, 211, 4));
        colours.add(Color.rgb(220, 83, 95));
        colours.add(Color.rgb(218, 218, 218));
        lineDataSet.setColors(colours);
        lineDataSet.setDrawFilled(true);

        barChart = v.findViewById(R.id.dashboardCostBarChart);
        showBarChart();
        initBarChart();

        return v;



    }

    private void showBarChart(){
        ArrayList<Integer> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>(); // Add this line
        String title = "Event Costs by Cost Type";


        // Add the following lines to add x-axis labels
        xLabels.add("Food");
        xLabels.add("Venue");
        xLabels.add("Marketing");
        xLabels.add("Wages");
        xLabels.add("Promotions");
        xLabels.add("Equipment");
        xLabels.add("Medical");
        xLabels.add("Other");

        for(int i = 0; i < 8; i++){
            valueList.add(320);
            valueList.add(2420);
            valueList.add(230);
            valueList.add(400);
            valueList.add(650);
            valueList.add(110);
            valueList.add(60);
            valueList.add(135);
        }

        for (int i = 0; i < 8; i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);
        initBarDataSet(barDataSet);

        BarData data = new BarData(barDataSet);


        // Add the following lines to set x-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        barChart.setData(data);
        barChart.invalidate();
    }

    private void initBarDataSet(BarDataSet barDataSet){
        //Changing the color of the bar
        barDataSet.setColors(new int[]{Color.LTGRAY});
        //Setting the size of the form in the legend
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);
        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(12f);
    }

    private void initBarChart(){
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false);

        Description description = new Description();
        description.setText("Events Hosted by Country");
        //remove the description label text located at the lower right corner
        barChart.setDescription(description);
        description.setPosition(0, 16f);


        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000);

        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);
        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);
    }

}
