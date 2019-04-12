package net.lzzy.practicesonline.activities.activities;

import android.os.Bundle;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.frageents.BeseFargment;
import net.lzzy.practicesonline.activities.frageents.Sqlashfragment;
import net.lzzy.practicesonline.activities.utils.AbstractStatiHhandler;
import net.lzzy.practicesonline.activities.utils.AppUtils;

import java.util.concurrent.ThreadPoolExecutor;

public class SplashActivity extends BaseActivity implements Sqlashfragment.OnSplashFinishedLisetener {
    public static final int WHAT_CONUTING = 0;
    public static final int WHAT_EXCEPTION = 1;
    public static final int WHAT_CONT_DONE = 2;
    private int seconds=10;
    private TextView tvCount;

    private SqlashHandler handler=new SqlashHandler(this);
       private static class SqlashHandler extends AbstractStatiHhandler<SplashActivity>{

           protected SqlashHandler(SplashActivity context) {
               super(context);
           }

           @Override
           public void handleMessage(Message mas, SplashActivity activity) {
              switch (mas.what){
                  case WHAT_CONUTING:
                      String display=mas.obj+"秒";
                      activity.tvCount.setText(display);
                      break;
                  case WHAT_CONT_DONE:
                      activity.getoMain();
                      break;
                  case WHAT_EXCEPTION:
                      new AlertDialog.Builder(activity)
                              .setMessage(mas.obj.toString())
                              .setPositiveButton("继续",(dialog, which) ->activity.getoMain() )
                              .setNegativeButton("继续",(dialog, which) -> AppUtils.exit())
                              .show();
              }
           }
       }

 @Override
 protected void onCreate(@Nullable Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  if (!AppUtils.isNetworkAcailable()){
   new AlertDialog.Builder(this)
           .setMessage("网络不可用，是否继续")
           .setPositiveButton("退出",((dialog, which) -> AppUtils.exit()))
           .setNegativeButton("确定",((dialog, which) -> getoMain()))
           .show();

  }else {
      ThreadPoolExecutor  executor =AppUtils.getExecutor();
      executor.execute(this::cancelCount);
  }
   AppUtils.getExecutor().execute(new Runnable() {
       @Override
       public void run() {

       }
   });
 }

   private void countDown(){
     while (seconds>=0){
         handler.sendMessage(handler.obtainMessage(WHAT_CONUTING,seconds));
     try {
         Thread.sleep(1000);
     } catch (InterruptedException e) {
         handler.sendMessage(handler.obtainMessage(WHAT_EXCEPTION,e.getMessage()));
     }
     seconds--;
     }
     handler.sendEmptyMessage(WHAT_CONT_DONE);
    }

 private void getoMain() {

 }

 @Override
    public void cancelCount() {
     seconds=0;
    }


    @Override
    protected int getLayoutRes() {
     return R.layout.activity_splash;
    }

    @Override
    protected int getContainerId() {
     return R.id.fragment_splanh_container;
    }

    @Override
    protected Fragment createFragent() {
     return new Sqlashfragment();
    }
   }
