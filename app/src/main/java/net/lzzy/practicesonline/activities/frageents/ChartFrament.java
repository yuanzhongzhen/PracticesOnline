package net.lzzy.practicesonline.activities.frageents;
import android.content.Context;
import android.content.Entity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.UserCookies;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import net.lzzy.practicesonline.activities.models.view.WrongType;
import net.lzzy.practicesonline.activities.utils.ViewUtils;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ChartFrament extends BeseFargment implements View.OnClickListener{
    private static  final String EXTRA_PRACTICE_ID="extraPracticeId";
    private static final String COLOR_CREEN="#7FFF00";
    private static final String COLOR_RED="#D81B60";
    private static final String COLOR_PRIMARY="#90d7ec";
    private static final String COLOR_RECOWN="#00574B";
    private List<QuestionResult>results;
    private ChartKusteber chartKusteber;
    private LineChart lineChart;
    private PieChart pieChart;
    private BarChart barChart;
    private Chart[]charts;
    private View[]dots;
    private String[]tiyles={"正确错误比例（单位%）","题目阅读统计","题目错误类型统计"};
    private  int rightCount=0;
    private float touchX1;
    private int chartIndex=0;

    public static final int MIN_DISTANCE = 100;
    public static ChartFrament Instance(List<QuestionResult> questionResults){
        Bundle bundle= new Bundle();
        ChartFrament chartFrament=new ChartFrament();
        bundle.putParcelableArrayList(EXTRA_PRACTICE_ID, (ArrayList<?extends Parcelable>)questionResults);
       chartFrament.setArguments(bundle);
        return chartFrament;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
           results=getArguments().getParcelableArrayList(EXTRA_PRACTICE_ID);
        }
        for (QuestionResult result:results){
            if (result.isRight()){
                rightCount++;
            }
        }


       /* chart = chart.findViewById(R.id.fragment_chart_pie);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
*/


    }



    @Override
    protected void populate() {
        TextView grechar=find(R.id.fragement_char_count_down);
        grechar.setOnClickListener(v -> {
            if (chartKusteber!=null){
                chartKusteber.gotoGird();
            }
        });

     initCharts();
     configPieChart();
     displayPoeChart();
     confingBarLineChart(barChart);
     displayBarChart();
     configBarLineChart(lineChart);
     displayLineChart();
     pieChart.setVisibility(View.VISIBLE);
        View dot1=find(R.id.fragement_char_dot1);
        View dot2=find(R.id.fragement_char_dot2);
        View dot3=find(R.id.fragement_char_dot3);
        dots = new View[]{dot1,dot2,dot3};
        find(R.id.Fragment_char_container).setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
            @Override
            public boolean handleTouch(MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    touchX1 = event.getX();
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    float touchX2=event.getX();
                    if (Math.abs(touchX2-touchX1)>MIN_DISTANCE){
                        if (touchX2<touchX1){
                            if (chartIndex<charts.length-1){
                                chartIndex++;
                            }else {
                                chartIndex=0;
                            }
                        }else {
                            if (chartIndex>0){
                                chartIndex--;
                            }else {
                                chartIndex=charts.length-1;
                            }
                        }
                        switchChart();
                    }
                }
                return true;
            }

            private void switchChart() {
                for (int i=0;i<charts.length;i++){
                    if (chartIndex==i){
                        charts[i].setVisibility(View.VISIBLE);
                        dots[i].setBackgroundResource(R.drawable.dot_fill_style);
                    }else {
                        charts[i].setVisibility(View.GONE);
                        dots[i].setBackgroundResource(R.drawable.dot_style);
                    }
                }
            }
        });


        List<PieEntry>entries=new ArrayList<>();
        entries.add(new PieEntry(rightCount,"正确"));
        entries.add(new PieEntry(results.size()-rightCount,"错误"));
        PieDataSet dataSet=new PieDataSet(entries,"");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0,40));
        dataSet.setSelectionShift(5f);
        List<Integer>colors=new ArrayList<>();
        colors.add(Color.parseColor(COLOR_CREEN));
        colors.add(Color.parseColor(COLOR_RED));
        dataSet.setColors(colors);
        PieData data=new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.invalidate();





    }


    private void displayLineChart() {
        ValueFormatter formatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Q"+(int)value;
            }
        };
       lineChart.getXAxis().setValueFormatter(formatter);
        List<Entry> entries=new ArrayList<>();
        for (int i = 1; i < results.size(); i++) {
            int readCount = UserCookies.getInstance().
                    getReadCount(results.get(i).getQuestionId().toString());
            entries.add(new Entry(i+1, readCount));
        }
        LineDataSet dataSet=new LineDataSet(entries,"查看次数");
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(1f);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleColor(Color.YELLOW);
        dataSet.setValueTextSize(9f);
        LineData data=new LineData(dataSet);
        lineChart.setData(data);
        lineChart.invalidate();

      /* lineChart.getAxis().setValueFormatter(xFormatter);
        List<Entry>*/





      /*  ArrayList<Entry>vals1=new ArrayList<>();
        for (int i=1;i<results.size();i++){
            int readCount= UserCookies.getInstance().getReadCount(results.get(i).getQuestionId().toString());
            vals1.add(new Entry(i,readCount));

        }
        LineDataSet dataSet = new LineDataSet(vals1, "Label"); // add entries to dataset
        dataSet.setColor(Color.parseColor("#7d7d7d"));//线条颜色
        dataSet.setCircleColor(Color.parseColor("#7d7d7d"));//圆点颜色
        dataSet.setLineWidth(1f);//线条宽度


        YAxis rightAxis = lineChart.getAxisRight();

        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置图表左边的y轴禁用
        leftAxis.setEnabled(false);
        //设置x轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘

        //透明化图例
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);

        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        //chart设置数据
        LineData lineData = new LineData(dataSet);
        //是否绘制线条上的文字
        lineData.setDrawValues(false);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh*/


    }

    private void configBarLineChart(BarLineChartBase chart) {



        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8f);
        xAxis.setGranularity(1f);

        /**  Y 轴 **/
        YAxis yAxis=chart.getAxisLeft();
        yAxis.setLabelCount(8,false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setTextSize(8f);
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0);

        /** chart属性 **/
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setPinchZoom(false);




    }

    private void displayBarChart() {
  ValueFormatter xFormatter=new ValueFormatter() {
      @Override
      public String getFormattedValue(float value) {
          return WrongType.getInstance((int)value).toString();
      }
  };
   barChart.getXAxis().setValueFormatter(xFormatter);
   int ok=0,miss=0,extra=0,wrong=0;
   for (QuestionResult result:results){
       switch ((result.getType())){
           case WRONG_OPTIONS:
               wrong++;
           case MISS_OPTIONS:
               miss++;
           case EXTRA_POTIONS:
               extra++;
           case RIGHT_OPTIONS:
               ok++;
               break;
               default:
                   break;

       }
   }
   List<BarEntry>entries=new ArrayList<>();
   entries.add(new BarEntry(0,ok));
   entries.add(new BarEntry(1,miss));
   entries.add(new BarEntry(2,extra));
   entries.add(new BarEntry(3,wrong));
        BarDataSet dataSet=new BarDataSet(entries,"查看类型");
        dataSet.setColors(Color.parseColor(COLOR_PRIMARY),Color.parseColor(COLOR_RED),
                Color.parseColor(COLOR_CREEN),Color.parseColor(COLOR_RECOWN));

        ArrayList<IBarDataSet>dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data=new BarData(dataSets);
        data.setBarWidth(0.8f);
        barChart.setData(data);
        barChart.invalidate();
    }


    private void confingBarLineChart(BarLineChartBase barChart) {

    }

    private void displayPoeChart() {

    }

    private void configPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    }

    private void initCharts() {
        pieChart=find(R.id.fragment_chart_pie);
        lineChart=find(R.id.fragment_chart_line);
        barChart=find(R.id.fragment_chart_bar);
        charts=new Chart[]{pieChart,lineChart,barChart};
        int i=0;
        for ( Chart chart :charts){
            chart.setTouchEnabled(false);
            chart.setVisibility(View.GONE);
            Description desc=new Description();
            desc.setText(tiyles[i++]);
            chart.setDescription(desc);
            chart.setNoDataText("获取中..");
            chart.setExtraOffsets(5,10,4,25);
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragement_chart;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChartFrament.ChartKusteber){
            chartKusteber=(ChartFrament.ChartKusteber)context;
        }else {
            throw new ClassCastException(context.toString()+"必须实现ChartKusteber");
        }
    }

   public interface  ChartKusteber{
       void gotoGird();
    }

}
