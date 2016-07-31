package com.leo.imitatecontact;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import com.leo.imitatecontact.adapter.ContactListAdapter;
import com.leo.imitatecontact.bean.ContactBean;
import com.leo.imitatecontact.loader.AbsBaseLoader;
import com.leo.imitatecontact.loader.ContactLoader;
import com.leo.imitatecontact.utils.ReflectionUtil;
import com.leo.imitatecontact.view.NavigationLetterListView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;
/**
 * Created by LEO on 2016/7/30.
 */
public class HomeActivity extends AppCompatActivity implements ContactListAdapter.OnGetLetterBySectionsListener, AbsBaseLoader.BaseLoaderCallBack<List<ContactBean>> {
    private ListView mContactListView;//联系人ListView
    private NavigationLetterListView mLetterListView;//字母表
    private TextView overLayout;//显示所选字母
    private OverlayThread overlayThread;
    private WindowManager windowManager;
    private Map<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private Handler handler;
    private ContactListAdapter adapter;
    private ContactLoader mContactLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        initView();
        initData();
        initOverlay();
    }

    private void initData() {
        overlayThread = new OverlayThread();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        handler = new Handler(Looper.getMainLooper());
        mContactLoader = new ContactLoader(getApplicationContext());
        mContactLoader.setCallBack(this);
        mContactLoader.excute();
    }


    private void initView() {
        mContactListView = (ListView) findViewById(R.id.id_listview);
        mLetterListView = (NavigationLetterListView) findViewById(R.id.id_letterview);
        mLetterListView.setmScrollCallBackLetterListener(new SelectLetterListener());
    }

    @Override
    public void onLoadFinished(List<ContactBean> result) {
        adapter = new ContactListAdapter(result, this, R.layout.contact_list_item);
        adapter.setOnGetLetterBySectionsListener(this);
        mContactListView.setAdapter(adapter);
        mContactListView.requestFocus();
    }

    /**
     * 字母列表点击滑动监听器事件
     */
    private class SelectLetterListener implements NavigationLetterListView.ScrollCallBackLetterListener {

        @Override
        public void showSelectLetter(String selectLetter) {
            if (alphaIndexer.get(selectLetter) != null) {
                int position = alphaIndexer.get(selectLetter);//如果存在集合中则取出集合中该字母对应所在的位置,再利用对应的setSelection，就可以实现点击选中相应字母，然后联系人就会定位到相应的位置
                mContactListView.setItemChecked(position,true);
                mContactListView.setSelection(position);
                overLayout.setText(selectLetter);
                overLayout.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                handler.postDelayed(overlayThread, 1000);
            }
        }
    }

    /**
     * 初始化所选首字母悬浮窗
     */
    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overLayout = (TextView) inflater.inflate(R.layout.overlay, null);
        overLayout.setVisibility(View.GONE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        windowManager.addView(overLayout, lp);
    }

    /**
     * 取消悬浮窗显示
     */
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getLetterBySelected(Map<String, Integer> alphaIndexer, List<String> sections) {
        this.alphaIndexer = alphaIndexer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(overlayThread);
        windowManager.removeViewImmediate(overLayout);
        clearPicassoCache();
    }

    protected void clearPicassoCache() {
        Picasso picasso = Picasso.with(getApplicationContext());
        try {
            Object object = ReflectionUtil.getField(picasso,Picasso.class, "cache");
            ReflectionUtil.invoke(Picasso.class,object, "clear", null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
