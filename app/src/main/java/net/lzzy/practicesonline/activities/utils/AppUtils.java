package net.lzzy.practicesonline.activities.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.util.Pair;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public static final String SP_SETTING = "spSetting";
    private static final String IP = "ip";
    private static final String PORT = "port";
    /**链表，讲一个一个数据连接起来**/
   private static List<Activity>activities=new LinkedList<>();
    private static WeakReference<Context> wContext;
    private static String runningActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        wContext = new WeakReference<>(this);
    }

    public static void Exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }






   public static Context getContext(){
        return wContext.get();
    }
    public static void remoreActivity(Activity activity) {
        activities.remove(activity);

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
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAcailable(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;

    }

    /**
     * 判断某服务能否连通
     *
     * @param address host  port
     */
    public static void tryConnectServer(String address) throws Exception{
        URL url=new URL(address);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.getContent();
    }


    /**
     * 保存服务器配置
     * @param ip 服务器地址
     * @param post 端口号
     * @param context Context
     */
    public static void saveServerSetting(String ip, String post, Context context){
        //步骤2-1：创建一个SharedPreferences.Editor接口对象，lock表示要写入的XML文件名，MODE_WORLD_WRITEABLE写操作
        SharedPreferences spSetting = context.getSharedPreferences(SP_SETTING, MODE_PRIVATE);
        //步骤2-2：将获取过来的值放入文件
        spSetting.edit()
                .putString(IP, ip)
                .putString(PORT, post).apply();
    }
    /**
     * 获取服务器配置
     * @param context Context
     * @return Pair对象
     */
    public static Pair<String,String> loadServerSetting(Context context){
        SharedPreferences spSetting = context.getSharedPreferences(SP_SETTING, MODE_PRIVATE);
        String ip=spSetting.getString(IP,"10.88.91.102");
        String port=spSetting.getString(PORT,"8888");
        return  new Pair<>(ip,port);
    }



    /**
     * 线程池的创建
     *
     * **/
    //CPU数量
    private static final int CPU_COUNT=Runtime.getRuntime().availableProcessors();
    //调用cpu的最小值 最大值
    private static final int CORE_POOL_SIEE=Math.max(2,Math.min(CPU_COUNT-1,4));
    //最大线程
    private static final int MAX_POOL_SIEE=CPU_COUNT*2+1;
    //保持运行的时间
    private static final int KEEP_ALICE_SECONDS=30;
    //定义线程创建
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

