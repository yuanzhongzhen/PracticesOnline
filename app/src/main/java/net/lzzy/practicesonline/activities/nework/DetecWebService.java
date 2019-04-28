package net.lzzy.practicesonline.activities.nework;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.AndroidException;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.PrracticesActivi;
import net.lzzy.practicesonline.activities.models.Practice;
import net.lzzy.practicesonline.activities.utils.AppUtils;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/28.
 * Description:
 */
public class DetecWebService extends Service {
    private int localCount;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        localCount=intent.getIntExtra(PrracticesActivi.EXTRA_LOCAL_COUNT,0);
        return new DetectWebBinder();
    }
    public class DetectWebBinder extends Binder{

        public static final int FLAG_SERVER_EXCEPTION = 0;
        public static final int FLAG_DATE_CHIANGEND = 1;
        public static final int FLAG_DATE_SAME = 2;

        public void derect(){
            AppUtils.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    int flag=compareData();
                   if(flag== FLAG_SERVER_EXCEPTION){
                   ontifyUser("服务器无法连接",android.R.drawable.ic_menu_compass,false );
                   }else if(flag== FLAG_DATE_CHIANGEND){
                       ontifyUser("服务器有更新",android.R.drawable.ic_popup_sync,true);
                   }else {

                   }
                }
            });

        }



        private void ontifyUser(String info,int icon,boolean refresh) {

        }

        private int compareData() {
           try {
               List<Practice>remote=
                       PracticeService.getPractices(PracticeService.getPracticesFrmServer());
               if (remote.size()!=localCount){
                   return  FLAG_DATE_CHIANGEND;
               }else {
                   return FLAG_DATE_SAME;
               }
           }catch (Exception e){
               return FLAG_SERVER_EXCEPTION;
           }
        }
    }
}
