package net.lzzy.practicesonline.activities.nework;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.activities.activities.PracticesActivity;
import net.lzzy.practicesonline.activities.models.Practice;
import net.lzzy.practicesonline.activities.utils.AppUtils;



import java.util.List;


/**
 * @author lzzy_gxy
 * @date 2019/4/28
 * Description:
 */
public class DetectWebService extends Service {
    public static final int NOTIFICATION_DETECT_ID = 0;
    public static final int FLAG_SERVER_EXCEPTION = 0;
    public static final int FLAG_DATA_CHANGED = 1;
    public static final int FLAG_DATA_SAME = 2;
    public static final String EXTRA_REFRSH = "app";
    private int localCount;
    private NotificationManager manager;


    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        localCount = intent.getIntExtra(PracticesActivity.EXTRA_LOCAL_COUNT, 0);
        //②在onBind中返回Binder对象
        return new DetectWebBinder();
    }

    /***
     *
     * ②在服务类中创建Binder类（与Activity通信）
     */
    public class DetectWebBinder extends Binder {


        /**
         *
         * ③在Binder类中添加执行后台任务的方法
         */
        public void detect() {
            AppUtils.getExecutor().execute(new Runnable() {

                @Override
                public void run() {
                    int flag = compareData();
                    if (flag == FLAG_SERVER_EXCEPTION) {
                        notifyUser("服务器无法连接", android.R.drawable.ic_menu_compass, false);
                    } else if (flag == FLAG_DATA_CHANGED) {
                        notifyUser("远程服务器有更新", android.R.drawable.ic_popup_sync, true);
                    } else {
                        if (manager != null) {
                            manager.cancel(NOTIFICATION_DETECT_ID);
                        }
                    }
                }
            });
        }

        private void notifyUser(String info, int icon, boolean refresh) {
            Intent intent = new Intent(DetectWebService.this, PracticesActivity.class);
            intent.putExtra(EXTRA_REFRSH, refresh);
            PendingIntent pendingIntent = PendingIntent.getActivity(DetectWebService.this,
                    0, intent, PendingIntent.FLAG_ONE_SHOT);
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = new Notification.Builder(DetectWebService.this, "0")
                        .setContentTitle("检测远程服务器")
                        .setContentText(info)
                        .setSmallIcon(icon)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
            } else {
                notification = new Notification.Builder(DetectWebService.this)
                        .setContentTitle("检远程测服务器")
                        .setContentText(info)
                        .setSmallIcon(icon)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
            }
            if (manager != null) {
                manager.notify(NOTIFICATION_DETECT_ID, notification);
            }

        }

        private int compareData() {
            try {
                List<Practice> remote =
                        PracticeService.getPractices(PracticeService.getPracticesFromServer());
                if (remote.size() != localCount) {
                    return FLAG_DATA_CHANGED;
                } else {
                    return FLAG_DATA_SAME;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return FLAG_SERVER_EXCEPTION;
            }
        }
    }
}
