package net.lzzy.practicesonline.activities.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.frageents.Sqlashfragment;
import net.lzzy.practicesonline.activities.utils.AppUtils;

/**
 * Created by lzzy_gxy on 2019/4/11.
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
 /**托管frargent**/
 protected FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutRes());
        AppUtils.addActivity(this);
        FragmentManager manger=getSupportFragmentManager();
        Fragment fragment =manger.findFragmentById(getContainerId());
        if (fragment==null){
            manger.beginTransaction().add(getContainerId(),createFragent()).commit();
        }
    }
    /**
     * 销毁
     * **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.removeActivity(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        AppUtils.setRunning(getLocalClassName());
    }
    protected void onStop(){
        super.onStop();
        AppUtils.setStopped(getLocalClassName());
    }

/**
 * Activity的布局文件id
 * @return 布局资源id
 *
 * **/
    protected abstract  int getLayoutRes();

    /**
     * 容器的id
     *
     * **/
    protected abstract  int getContainerId();

    /**
     * 生成托管的Fragment
     * @return fragment
     * **/
    protected abstract Fragment createFragent();
}
