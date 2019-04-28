package net.lzzy.practicesonline.activities.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.constants.ApiConnstants;
import net.lzzy.practicesonline.activities.frageents.Sqlashfragment;
import net.lzzy.practicesonline.activities.utils.AbstractStatiHhandler;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.utils.ViewUtils;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

public class SplashActivity extends BaseActivity implements Sqlashfragment.OnSplashFinishedLisetener {
    public static final int WHAT_CONUTING = 0;
    public static final int WHAT_EXCEPTION = 1;
    public static final int WHAT_CONT_DONE = 2;
    public static final int WHAT_SERVER_OFF = 4;
    private int seconds=10;
    private TextView tvCount;
    private boolean isServerOn =true;
/**/
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
                      if (activity.isServerOn){
                          if (AppUtils.getContext() instanceof SplashActivity){
                              activity.getoMain();
                          }
                      }
                      break;
                  case WHAT_EXCEPTION:
                      new AlertDialog.Builder(activity)
                              .setMessage(mas.obj.toString())
                              .setPositiveButton("继续",(dialog, which) ->activity.getoMain() )
                              .setNegativeButton("继续",(dialog, which) -> AppUtils.exit())
                              .show();
                      break;
                  case WHAT_SERVER_OFF:
                      Activity context=AppUtils.getRuningActivity();
                      new AlertDialog.Builder(Objects.requireNonNull(context))
                              .setMessage("服务器没有响应，是否继续？\n"+mas.obj.toString())
                              .setNeutralButton("设置", (dialog, which) -> {
                                  ViewUtils.gotoSetting(context);
                              })
                              .setNegativeButton("确定", (dialog, which) -> {
                                  if (context instanceof AppCompatActivity){
                                      ((SplashActivity)context).goToMain();
                                  }
                              })
                              .setPositiveButton("退出", (dialog, which) -> AppUtils.Exit()).show();
                      break;
                      default:
                          break;

              }
           }
       }

 @Override
 protected void onCreate(@Nullable Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     if (!AppUtils.isNetworkAcailable(this)) {
         new AlertDialog.Builder(this)
                 .setMessage("网络不可用，是否继续")
                 .setPositiveButton("退出", ((dialog, which) -> AppUtils.exit()))
                 .setNegativeButton("确定", ((dialog, which) -> getoMain()))
                 .show();

     } else {
         tvCount = findViewById(R.id.activity_splash_count_down);
         ThreadPoolExecutor executor = AppUtils.getExecutor();
         //倒计时线程1
         executor.execute(this::countDown);

         executor.execute(this::detectServerStatus);

     }
 }
     private void detectServerStatus(){
         try {
             AppUtils.tryConnectServer(ApiConnstants.URL_API);
         } catch (Exception e) {
             isServerOn = false;
             e.printStackTrace();
             handler.sendMessage(handler.obtainMessage(WHAT_SERVER_OFF,e.getMessage()));
         }

 }
public void goToMain(){
    if (AppUtils.getRuningActivity()==this){
        startActivity(new Intent(this, PracticesActivity.class));
        finish();
    }
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
             startActivity(new Intent(this, PracticesActivity.class));
             finish();

         }


 @Override
    public void cancelCount() {
     seconds=0;
    goToMain();
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
    protected Fragment createFragment() {
        return new Sqlashfragment();
    }



}

