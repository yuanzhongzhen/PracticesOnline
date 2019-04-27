package net.lzzy.practicesonline.activities.frageents;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.Practice;

import net.lzzy.practicesonline.activities.models.PracticeFactory;
import net.lzzy.practicesonline.activities.models.QuesetionFaceory;
import net.lzzy.practicesonline.activities.models.Question;

import net.lzzy.practicesonline.activities.models.UserCookies;

import net.lzzy.practicesonline.activities.nework.PracticeService;

import net.lzzy.practicesonline.activities.nework.QuestionService;
import net.lzzy.practicesonline.activities.utils.AbstractStatiHhandler;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.utils.DateTimeUtils;
import net.lzzy.practicesonline.activities.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
 public  class PracticesFrangmnt extends BeseFargment {

    public static final int WHAT_PRACTICE_DONE = 0;
    public static final int WHAT_EXCEPTION = 1;
    private ListView lv;
    private TextView tvNone;
    private SwipeRefreshLayout swipe;
    private TextView tvHint;
    private TextView tvTime;
    private List<Practice> practices;
    private GenericAdapter<Practice> adapter;
    private PracticeFactory factory=PracticeFactory.getInstance();
    private ThreadPoolExecutor executor= AppUtils.getExecutor();
    private boolean isDelete=false;
    private float touchX1;
    public static final int MIN_DISTANCE = 100;
    private DownloadHandler handler=new DownloadHandler(this);
    private OnPracticeListener listener;

    /**
     * 自定义线程 返回数据的方法
     *
     */
    private static class DownloadHandler extends AbstractStatiHhandler<PracticesFrangmnt> {

        public DownloadHandler(PracticesFrangmnt context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg, PracticesFrangmnt fragment) {
            switch (msg.what){
                case WHAT_PRACTICE_DONE:
                    fragment.tvTime.setText(DateTimeUtils.DATE_ITE_FORMAT.format(new Date()));
                    UserCookies.getInstance().updateLastRefreehTime();
                    try {
                        List<Practice> practices=PracticeService.getPractices(msg.obj.toString());
                        for (Practice practice:practices){
                            fragment.adapter.add(practice);
                        }
                        Toast.makeText(fragment.getContext(), "同步完成", Toast.LENGTH_SHORT).show();
                        fragment.finishRefresh();
                    } catch (Exception e) {
                        e.printStackTrace();
                        fragment.handlePracticeException(e.getMessage());
                    }
                    break;
                case WHAT_EXCEPTION:
                    fragment.handlePracticeException(msg.obj.toString());
                    break;
                default:
                    break;

            }
            if (msg.what==0){

            }
        }
    }

    private static class PracticeDownloader extends AsyncTask<Void,Void,String>{
        WeakReference<PracticesFrangmnt> fragment;
        PracticeDownloader(PracticesFrangmnt fragment){
            this.fragment=new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PracticesFrangmnt fragment=this.fragment.get();
            fragment.tvTime.setVisibility(View.VISIBLE);
            fragment.tvHint.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return PracticeService.getPracticesFrmServer();

            } catch (IOException e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PracticesFrangmnt fragment=this.fragment.get();
            fragment.tvTime.setText(DateTimeUtils.DATE_ITE_FORMAT.format(new Date()));
            try {
                List<Practice> practices=PracticeService.getPractices(s);
                for (Practice practice:practices){
                    fragment.adapter.add(practice);
                }
                Toast.makeText(fragment.getContext(), "同步完成", Toast.LENGTH_SHORT).show();
                fragment.finishRefresh();
            } catch (Exception e) {
                e.printStackTrace();
                fragment.handlePracticeException(e.getMessage());
            }
        }
    }
    private static class QuestionDownloader extends AsyncTask<Void,Void,String>{
        WeakReference<PracticesFrangmnt> fragment;
        Practice practice;
        QuestionDownloader(PracticesFrangmnt fragment, Practice practice){
            this.fragment=new WeakReference<>(fragment);
            this.practice=practice;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return QuestionService.getQuestionsOfPracticeFromServer(practice.getApild());
            }catch (IOException e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PracticesFrangmnt fragment=this.fragment.get();
            QuesetionFaceory factory=QuesetionFaceory.getInstance();
            try {
                List<Question> questions=QuestionService.getQuestions(s,practice.getId());
                for (Question question:questions){
                    factory.insert(question);
                }
                practice.setDownloaded(true);
                Toast.makeText(fragment.getContext(), "下载成功", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    private void handlePracticeException(String message) {
        finishRefresh();
        Snackbar.make(lv,"同步失败\n"+message,Snackbar.LENGTH_LONG)
                .setAction("重试",v -> {
                    swipe.setRefreshing(true);
                    refreshListener.onRefresh();
                }).show();

    }

    private void finishRefresh() {
        swipe.setRefreshing(false);
        tvTime.setVisibility(View.GONE);
        tvHint.setVisibility(View.GONE);
    }

    @Override
    protected void populate() {
        initViews();
        loadPractices();
        initSWipe();
    }
    private SwipeRefreshLayout.OnRefreshListener refreshListener= this::downloadPracticesAsync;


    /**
     * 自定义线程初始化记准备工作 ，与执行线程方法
     */
    private void downloadPractices() {
        tvTime.setVisibility(View.VISIBLE);
        tvHint.setVisibility(View.VISIBLE);
        executor.execute(()->{
            try {
                String json= PracticeService.getPracticesFrmServer();
                handler.handleMessage(handler.obtainMessage(WHAT_PRACTICE_DONE,json));
            } catch (IOException e) {
                e.printStackTrace();
                handler.handleMessage(handler.obtainMessage(WHAT_EXCEPTION,e.getMessage()));
            }
        });
    }

    private void downloadPracticesAsync(){
        new PracticeDownloader(this).execute();
    }

    private void initSWipe() {
        swipe.setOnRefreshListener(refreshListener);
        //region处理滑动和刷新的冲突
        //滚动监听器 setOnScrollListener()
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean isTop=view.getChildCount()==0||view.getChildAt(0).getTop()>=0;
                //使下拉刷新失效 setEnabled()
                swipe.setEnabled(isTop);
            }
        });
        //endregion
    }

    private void loadPractices() {
        practices=factory.get();
        //region排序
        Collections.sort(practices,((o1, o2) -> o2.getDowntoladDote().compareTo(o1.getDowntoladDote())));
        //endregion
        adapter=new GenericAdapter<Practice>(getActivity(),R.layout.practices,practices) {
            @Override
            public void populate(ViewHolder viewHolder, Practice practice) {
                viewHolder.setTextView(R.id.practices_item_tv_name,practice.getName());
                Button btnOutlines=viewHolder.getView(R.id.practices_item_btn_outlines);
                if (practice.isDownloaded()){
                    btnOutlines.setVisibility(View.VISIBLE);
                    btnOutlines.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage(practice.getOutlines())
                                    .show();
                        }
                    });
                }else {
                    btnOutlines.setVisibility(View.GONE);
                }
                Button btnDel=viewHolder.getView(R.id.practices_item_btn_del);
                btnDel.setVisibility(View.GONE);
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new android.app.AlertDialog.Builder(getContext())
                                .setTitle("删除确认")
                                .setMessage("要删除吗？")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", (dialog, which) -> { isDelete=false;adapter.remove(practice); }).show();
                    }
                });
                int visibility=isDelete?View.VISIBLE:View.GONE;
                btnDel.setVisibility(visibility);

                viewHolder.getConvertView().setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
                    @Override
                    public boolean handleTouch(MotionEvent event) {
                        slideToDelete(event,practice,btnDel);
                        return true;
                    }
                });
            }

            @Override
            public boolean persistInsert(Practice practice) {
                return factory.add(practice);
            }

            @Override
            public boolean persistDelete(Practice practice) {
                return factory.deletePracticeAndRelated(practice);
            }
        };
        lv.setAdapter(adapter);
    }
    private void slideToDelete(MotionEvent event, Practice practice, Button but) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX1=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float touchX2 = event.getX();
                if (touchX1- touchX2 > MIN_DISTANCE){
                    if (!isDelete){
                        but.setVisibility(View.VISIBLE);
                        isDelete=true;
                    }

                }else {
                    if (but.isShown()){
                        but.setVisibility(View.GONE);
                        isDelete=false;
                    }else {
                        performItemClick(practice);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void performItemClick(Practice practice) {
        if (practice.isDownloaded()){
            listener.OnPractice(practice.getId().toString());
        }else {
            new AlertDialog.Builder(getContext())
                    .setMessage("下载该章节题目吗？")
                    .setPositiveButton("下载",(dialog, which) -> downloadQuestions(practice))
                    .setNeutralButton("取消",null)
                    .show();
        }
    }
    private void downloadQuestions(Practice practice){
        new QuestionDownloader(this,practice).execute();
    }

    public void initViews(){
        lv = find(R.id.fragement_pracivity_lv);
        tvNone = find(R.id.fragement_pracivity_none);
        lv.setEmptyView(tvNone);
        swipe = find(R.id.fragement_pracivity_swipe);
        tvHint = find(R.id.fragement_pracivity_tv_hint);
        tvTime = find(R.id.fragement_pracivity_tv_time);
        tvTime.setText(UserCookies.getInstance().gteLastRefreshTime());
        tvHint.setVisibility(View.GONE);
        tvTime.setVisibility(View.GONE);
        find(R.id.fragement_pracivity_lv).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isDelete=false;
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragement_pracivity;
    }

    @Override
    public void search(String kw) {
        practices.clear();
        if (kw.isEmpty()){
            practices.addAll(factory.get());
        }else {
            practices.addAll(factory.search(kw));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (OnPracticeListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnPracticeListener接口");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    /**
     * 跳转接口
     */
    public interface OnPracticeListener{

        void OnPractice(String practiceId);
    }
}
