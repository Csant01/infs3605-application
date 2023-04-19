package com.example.infs3605_app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class FragmentDashboardGeneral extends Fragment {
    View v;
    PieChart pieChart, attnPieChart;
    BarChart barChart;
    TextView upcomingEvents, feedbackRatings;
    RecyclerView commentsRv;
    FeedbackCommentsAdapter adapter;
    DatabaseConnector db;
    ArrayList<String> comments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard_general, container, false);
        commentsRv = v.findViewById(R.id.dashboardCommentsRv);
        db = new DatabaseConnector(getContext());
        upcomingEvents = v.findViewById(R.id.dashboardUpcomingEvents);
        feedbackRatings = v.findViewById(R.id.dashboardAverageFeedbackRating);
        pieChart = v.findViewById(R.id.dashboardPieChart);
        attnPieChart = v.findViewById(R.id.dashboardAttnPieChart);
        commentsRv = v.findViewById(R.id.dashboardCommentsRv);
        barChart = v.findViewById(R.id.dashboardBarChart);
        setupPieChart();
        loadPieChartData();
        setupAttnPieChart();
        loadAttnPieChartData();
        showBarChart();
        initBarChart();

        comments = new ArrayList<>();
        comments = db.getFeedbackCommentsAll();
        adapter = new FeedbackCommentsAdapter(getContext(), comments);
        commentsRv.setAdapter(adapter);
        commentsRv.setLayoutManager(new LinearLayoutManager(getContext()));

        upcomingEvents.setText("11");
        feedbackRatings.setText("3.2");
        adapter = new FeedbackCommentsAdapter(getContext(), comments);
        commentsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsRv.setAdapter(adapter);
        return v;
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.5f, "Upcoming Events"));
        entries.add(new PieEntry(0.2f, "Events Pending Approval"));
        entries.add(new PieEntry(0.3f, "Completed Events"));

        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(Color.rgb(251, 211, 4));
        colours.add(Color.rgb(220, 83, 95));
        colours.add(Color.rgb(218, 218, 218));

//        for (int colour : ColorTemplate.VORDIPLOM_COLORS) {
//            colours.add(colour);
//        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colours);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void setupPieChart () {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(8);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Status of Events");
        pieChart.setCenterTextSize(12);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadAttnPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.86f, "Attended"));
        entries.add(new PieEntry(0.14f, "Did not attend"));

        ArrayList<Integer> colours = new ArrayList<>();
        for (int colour : ColorTemplate.MATERIAL_COLORS) {
            colours.add(colour);
        }

        for (int colour : ColorTemplate.VORDIPLOM_COLORS) {
            colours.add(colour);
        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colours);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(attnPieChart));
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);

        attnPieChart.setData(data);
        attnPieChart.invalidate();
    }

    private void setupAttnPieChart () {
        attnPieChart.setDrawHoleEnabled(true);
        attnPieChart.setUsePercentValues(true);
        attnPieChart.setEntryLabelTextSize(8);
        attnPieChart.setEntryLabelColor(Color.BLACK);
        attnPieChart.setCenterText("Event Attendance Rate");
        attnPieChart.setCenterTextSize(12);
        attnPieChart.getDescription().setEnabled(false);

        Legend l = attnPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void showBarChart(){
        ArrayList<Integer> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>(); // Add this line
        String title = "Events Hosted by Country";


        // Add the following lines to add x-axis labels
        xLabels.add("Hong Kong");
        xLabels.add("Australia");
        xLabels.add("USA");

        for(int i = 0; i < 3; i++){
            valueList.add(1);
            valueList.add(8);
            valueList.add(4);

        }

        for (int i = 0; i < 3; i++) {
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
