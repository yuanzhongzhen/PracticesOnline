package net.lzzy.practicesonline.activities.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lzzy_gxy on 2019/3/11.
 * Description:
 */
public class AppUtils extends Application {
 /**链表，讲一个一个数据连接起来**/
   private static List<Activity>activities=new LinkedList<>();
    private static Context context;
    private static String runningActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }



    public static Context getContext(){
        return context;
    }

    /**添加Acticity**/
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    /**删除Acticity**/
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    /**退出Acticity**/
    public static void exit(){
        for (Activity activity:activities){
            if (activity!=null){
                activity.finish();
            }
        }
        System.exit(0);
    }
    /**启动遍历Acticity**/
    public static Activity getRuningActivity(){
        for (Activity activity:activities){
            String name=activity.getLocalClassName();
            if (AppUtils.runningActivity.equals(name)){
                return activity;
            }

        }
        return null;
    }

    public static void setRunning(String runningActivity){
        AppUtils.runningActivity =runningActivity;
    }
    public static void setStopped(String stoppedActivity){
       if (stoppedActivity.equals(AppUtils.runningActivity)){
           AppUtils.runningActivity="";
       }
    }

    public static boolean isNetworkAcailable(){
        ConnectivityManager manager=(ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        return info.isConnected();


    }
    /**
     * 线程时
     *
     * **/
    private static final int CPU_COUNT=Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIEE=Math.max(2,Math.min(CPU_COUNT-1,4));
    private static final int MAX_POOL_SIEE=CPU_COUNT*2+1;
    private static final int KEEP_ALICE_SECONDS=30;
    private static final ThreadFactory THREAD_FACTORY=new ThreadFactory() {
        private final AtomicInteger count =new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"thread #"+count.getAndIncrement());
        }
    };
    private  static final BlockingQueue<Runnable> POOL_QUEUE=new LinkedBlockingQueue<>(128);
    public static ThreadPoolExecutor getExecutor(){
        ThreadPoolExecutor executor=new ThreadPoolExecutor(CORE_POOL_SIEE,MAX_POOL_SIEE,
                KEEP_ALICE_SECONDS, TimeUnit.SECONDS,POOL_QUEUE,THREAD_FACTORY);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}

