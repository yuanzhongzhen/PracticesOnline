package net.lzzy.practicesonline.activities.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.SplashActivity;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class ViewUtils {
    public static void gotoSetting(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_setting,null);
        Pair<String, String> url=AppUtils.loadServerSetting(context);
        EditText edtIp=view.findViewById(R.id.dialog_setting_edt_ip);
        edtIp.setText(url.first);
        EditText edtPort=view.findViewById(R.id.dialog_setting_edt_port);
        edtPort.setText(url.second);
        new AlertDialog.Builder(context)
                .setView(view)
                .setNegativeButton("取消", (dialog, which) -> {
                    AppUtils.Exit();
                })
                .setPositiveButton("保存", (dialog, which) -> {
                    String ip=edtIp.getText().toString();
                    String port=edtPort.getText().toString();
                    if (TextUtils.isEmpty(ip)||TextUtils.isEmpty(port)){
                        Toast.makeText(context,"输入信息不完整",Toast.LENGTH_LONG).show();
                        return;
                    }
                    AppUtils.saveServerSetting(ip,port,context);
                    gotoMain(context);
                }).show();
    }

    private static void gotoMain(Context context){
        if (context instanceof SplashActivity){
            ((SplashActivity)context).goToMain();
        }
    }
}
