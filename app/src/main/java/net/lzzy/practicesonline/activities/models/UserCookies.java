package net.lzzy.practicesonline.activities.models;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.NetworkInfo;

import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.utils.DateTimeUtils;

import java.util.Date;

/**
 * Created by lzzy_gxy on 2019/4/24.
 * Description:
 */
public class UserCookies {
    private SharedPreferences spTime;
    private static  final String KEY_TIME="keytime";
private static final UserCookies INSTANCE=new UserCookies();

    private  UserCookies(){
        spTime= AppUtils.getContext()
                .getSharedPreferences("refresh_hime", Context.MODE_PRIVATE);

    }
    public static UserCookies getInstance(){
        return INSTANCE;
    }
    /**
     * 获取上次更新的时间
     *
     * */
    public void  updateLastRefreehTime(){
        String tim= DateTimeUtils.DATE_ITE_FORMAT.format(new Date());
        spTime.edit().putString(KEY_TIME,tim).apply();
    }
     /**
      * 获取上次读取的时间
      * **/
    public String  gteLastRefreshTime(){
       return spTime.getString(KEY_TIME,"");
    }
}
