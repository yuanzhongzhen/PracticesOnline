package net.lzzy.practicesonline.activities.frageents;

import android.app.AlertDialog;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.BaseActivity;
import net.lzzy.practicesonline.activities.models.Practice;
import net.lzzy.practicesonline.activities.models.PracticeFactory;
import net.lzzy.practicesonline.activities.models.UserCookies;
import net.lzzy.practicesonline.activities.nework.PracticeService;
import net.lzzy.practicesonline.activities.utils.AbstractStatiHhandler;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.utils.DateTimeUtils;
import net.lzzy.practicesonline.activities.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.io.IOException;
import java.util.Collections;

import java.util.Date;
import java.util.List;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class PracticesFrangmnt extends BeseFargment {
    public static final int WHAT_PRACTICE_DONE = 0;
    public static final int WHAT_EXCEPTION = 1;
    private ListView lv;
    private SwipeRefreshLayout swipe;
    private TextView tvHint;
    private TextView tvTime;
    private List<Practice>practices;
    private GenericAdapter<Practice>adapter;
    private PracticeFactory factory=PracticeFactory.getInstance();
    private ThreadPoolExecutor executor= AppUtils.getExecutor();
    private DownloadHandler handler=new DownloadHandler(this);
    private Button but;
    private float touchX1;
    private float touchX2;
    private boolean isDelete;
    public static final int MIN_DISTANCE = 100;

  private static  class DownloadHandler extends AbstractStatiHhandler<PracticesFrangmnt> {
      protected DownloadHandler(PracticesFrangmnt context) {
          super(context);
      }

      @Override
      public void handleMessage(Message mags, PracticesFrangmnt frangmnt) {
      switch (mags.what){
          case WHAT_PRACTICE_DONE:
              frangmnt.tvTime.setText(DateTimeUtils.DATE_ITE_FORMAT.format(new Date()));
              UserCookies.getInstance().updateLastRefreehTime();
            try {
                List<Practice>practices=PracticeService.getPractices(mags.obj.toString());
               for (Practice practice:practices){
                   frangmnt.adapter.add(practice);
               }
                Toast.makeText(frangmnt.getContext(),"同步完成",Toast.LENGTH_SHORT).show();
                frangmnt.finishRefesh();
            }catch (Exception e){
                e.printStackTrace();
                frangmnt.handleFractiveExcption(e.getMessage());
            }
            break;
            case WHAT_EXCEPTION:
              frangmnt.handleFractiveExcption(mags.obj.toString());
              break;
              default:
                  break;
      }

      }
  }

    private void handleFractiveExcption(String message) {
       finishRefesh();
       Snackbar.make(lv,"同步完成"+message,Snackbar.LENGTH_LONG)
               .setAction("重试",v->{
                   swipe.setRefreshing(true);
                   refreshListener.onRefresh();
               }).show();



    }

    private void finishRefesh(){
     adapter.notifyDataSetChanged();
    swipe.setRefreshing(false);
    tvTime.setVisibility(View.GONE);
    tvHint.setVisibility(View.GONE);
}
private SwipeRefreshLayout.OnRefreshListener refreshListener=this::downloadPractices;
  private void downloadPractices() {
      tvTime.setVisibility(View.VISIBLE);
      tvHint.setVisibility(View.VISIBLE);
      executor.execute(() -> {
          try {
              String json = PracticeService.getPracticesFrmServer();
              handler.sendMessage(handler.obtainMessage(WHAT_PRACTICE_DONE, json));
          } catch (IOException e) {
              e.printStackTrace();
              handler.sendMessage(handler.obtainMessage(WHAT_EXCEPTION, e.getMessage()));
          }


      });

  }

  private void initSwipe(){
      swipe.setOnRefreshListener(refreshListener);
      lv.setOnScrollListener(new AbsListView.OnScrollListener() {
          @Override
          public void onScrollStateChanged(AbsListView view, int scrollState) {

          }

          @Override
          public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
       boolean isTop=view.getChildCount()==0||view.getChildAt(0).getTop()>=0;
       swipe.setEnabled(isTop);
          }
      });
  }

    @Override
    protected void populate() {
       initViews();
       loadPractioes();
       initViews();
       initSwipe();
    }

    private void loadPractioes() {
        practices=factory.get();
        Collections.sort(practices,((o1, o2) -> o2.getDowntoladDote().compareTo(o1.getDowntoladDote())));
        adapter=new GenericAdapter<Practice>(getContext(),R.layout.practice_item,practices) {
            @Override
            public void populate(ViewHolder holder, Practice practice) {
                holder.setTextView(R.id.practice_item_tv_name, practice.getName());
                Button btnOutlines=holder.getView(R.id.practice_item_btn_outlines);

                if (practice.isDownloaded()){
                    btnOutlines.setVisibility(View.VISIBLE);
                    btnOutlines.setOnClickListener(v -> new AlertDialog. Builder(getContext())
                            .setMessage(practice.getOutlines())
                            .show());
                }else {
                    btnOutlines.setVisibility(View.GONE);
                }

                Button btnDel=holder.getView(R.id.practice_item_btm_btn_del);
                btnDel.setVisibility(View.GONE);
                btnDel.setOnClickListener(v -> new AlertDialog.Builder(getContext())

                        .setMessage("要删除章节以及题目吗？")
                        .setPositiveButton("删除",((dialog, which) -> {
                            isDelete=false;
                            adapter.remove(practice);
                        }))
                        .setNegativeButton("取消",null)
                        .show());
                int visible=isDelete?View.VISIBLE:View.GONE;
                btnDel.setVisibility(visible);
                holder.getConvertView().setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
                    @Override
                    public boolean handleTouch(MotionEvent event) {
                        slideToDelete(event,practice,btnDel);
                        return false;
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
    /**
     * 实现删除
     * */
    private void slideToDelete(MotionEvent event , Practice practice , Button btnDel){

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX1=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                touchX2 =event.getX();
                if (touchX1-touchX2> MIN_DISTANCE){
                    if (!isDelete){
                        btnDel.setVisibility(View.VISIBLE);
                        isDelete=true;
                    }

                }else {
                    if (btnDel.isShown()){
                        btnDel.setVisibility(View.GONE);
                        isDelete=false;
                    }else {

                    }
                }
                break;
            default:
                break;
        }
    }
    private  void initViews(){
        lv=find(R.id.fragement_pracivity_lv);
      TextView TvNone=find(R.id.fragement_pracivity_none);
      lv.setEmptyView(TvNone);
      swipe=find(R.id.fragement_pracivity_swipe);
      tvHint=find(R.id.fragement_pracivity_tv_hint);
      tvTime=find(R.id.fragement_pracivity_tv_time);
      tvTime.setText(UserCookies.getInstance().gteLastRefreshTime());
      tvHint.setVisibility(View.GONE);
      tvTime.setVisibility(View.GONE);
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
}
