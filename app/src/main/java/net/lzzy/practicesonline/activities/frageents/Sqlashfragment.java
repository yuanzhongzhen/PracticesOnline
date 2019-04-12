package net.lzzy.practicesonline.activities.frageents;

import android.content.Context;
import android.view.View;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.utils.AppUtils;

import java.util.Calendar;

/**
 * Created by lzzy_gxy on 2019/4/10.
 * Description:
 */
public class Sqlashfragment extends BeseFargment {
  private int[]img=new int[]{R.drawable.splash1,R.drawable.splash2,R.drawable.splash3};
  private OnSplashFinishedLisetener lisetener;

  @Override
    protected void populate() {
        View wall=find(R.id.fragment_sqlash_wall);
        int pos= Calendar.getInstance().get(Calendar.SECOND)%3;
    wall.setBackgroundResource(img[pos]);
     wall.setOnClickListener(v->lisetener.cancelCount());
  }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sqlash;
    }

    @Override
    public void search(String kw) {


    }
   /**创建**/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSplashFinishedLisetener){
            lisetener=(OnSplashFinishedLisetener)context;
        }else {
            throw new ClassCastException(context.toString()+"必须实现OnSplashFinishedLisetener");
        }
    }
   /**销毁**/
    @Override
    public void onDetach() {
        super.onDetach();
        lisetener=null;
    }

    /**定义接口**/
    public interface OnSplashFinishedLisetener{
        void cancelCount();
    }

}
