package com.leo.imitatecontact.loader;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LEO on 2016/7/30.
 */
public abstract class AbsBaseLoader<T> {
    //使用只有一个线程的线程池去执行数据库查询耗时操作
    private final static ExecutorService sDbEngine = Executors.newSingleThreadExecutor();
    //使用Handle进行UI更新
    private final static Handler sHandler = new Handler(Looper.getMainLooper());

    private BaseLoaderCallBack callBack;

    //执行数据库查询动作
    public final void excute(){
        sDbEngine.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    postResult(doInBackGround());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void postResult(final T reslut){
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(reslut);
            }
        });
    }
    //后台执行耗时动作
    protected abstract T doInBackGround();
    //UI更新界面
    protected void onPostExecute(T result){
        if (callBack != null){
            callBack.onLoadFinished(result);
        }
    }

    public interface BaseLoaderCallBack<T>{
        void onLoadFinished(T result);
    }

    public void setCallBack(BaseLoaderCallBack callBack) {
        this.callBack = callBack;
    }
}
