package net.lzzy.practicesonline.activities.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by lzzy_gxy on 2019/4/12.
 * Description:
 */
public abstract class AbstractStatiHhandler<T> extends Handler {
 private final WeakReference<T>context;

    protected AbstractStatiHhandler(T context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t=context.get();
        handleMessage(msg,t);
    }

    /**
     * 处理消息的业务逻辑
     * @param mags Message对象
     * @param t context
     */
    public abstract void handleMessage(Message mags,T t);
}
