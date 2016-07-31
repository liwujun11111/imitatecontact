package com.leo.imitatecontact.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.imitatecontact.R;
import com.leo.imitatecontact.bean.ContactBean;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 2016/7/30.
 */
public class ContactListAdapter extends AbsBaseAdapter<ContactBean> {
    private final int VIEW_TYPE_COUNT = 3;
    private Map<String,Integer> alphaIndexer;
    private List<String> sections;
    private OnGetLetterBySectionsListener listener;
    private boolean flag;//标志用于只执行一次代码
    public ContactListAdapter(List<ContactBean> listBeans, Context context, int layoutId) {
        super(listBeans, context,layoutId);
        alphaIndexer=new HashMap<>();
        sections=new ArrayList<>();
        for (int i = 0; i <mDataList.size();i++) {
            //当前汉语拼音的首字母
            String currentAlpha=listBeans.get(i).getFirstHeadLetter();
            //上一个拼音的首字母，如果不存在则为""
            String previewAlpha=(i-1)>=0?listBeans.get(i-1).getFirstHeadLetter():"";
            if (!previewAlpha.equals(currentAlpha)){
                String firstAlpha=listBeans.get(i).getFirstHeadLetter();
                alphaIndexer.put(firstAlpha,i);
                sections.add(firstAlpha);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        int type=0;
        if (position==0){
            type=2;
        }else if (position==1){
            type=1;
        }
        return type;
    }

    @Override
    public void convertViewValue(ContactViewHolder contactViewHolder) {
        if (!flag){
            if (listener!=null){
                listener.getLetterBySelected(alphaIndexer,sections);
            }
            flag=true;
        }
        ImageView iv = contactViewHolder.getView(R.id.contact_icon_id);
        ContactBean contactBean = mDataList.get(contactViewHolder.getmPosition());
        Picasso.with(mContext).load(contactBean.getUri()).placeholder(R.mipmap.contact_user).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
               synchronized (this) {
                   if (source == null){
                       return null;
                   }
                   int originBitmapHeight = source.getHeight();
                   int originBitmapWidth = source.getWidth();
                   int min = Math.min(originBitmapHeight, originBitmapWidth);
                   Bitmap resultBitmap =  Bitmap.createScaledBitmap(source, min, min, false);
                   final Paint paint = new Paint();
                   paint.setAntiAlias(true);

                   Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);

                   Canvas canvas = new Canvas(target);
                   canvas.drawCircle(min / 2, min / 2, min / 2, paint);
                   paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                   canvas.drawBitmap(resultBitmap, 0, 0, paint);

                   source.recycle();
                   return target;
               }
            }

            @Override
            public String key() {
                return  "circleImage";
            }
        }).into(iv);
        contactViewHolder.setText(R.id.contact_title, mDataList.get(contactViewHolder.getmPosition()).getTitle()).setText(R.id.contact_phone_num, contactBean.getPhoneNum());
        if (contactViewHolder.getmPosition() >= 1) {
            String currentAlpha = mDataList.get(contactViewHolder.getmPosition()).getFirstHeadLetter();
            String previewAlpha = mDataList.get(contactViewHolder.getmPosition() - 1).getFirstHeadLetter();
            if (!previewAlpha.equals(currentAlpha)) {
                contactViewHolder.getView(R.id.first_alpha).setVisibility(View.VISIBLE);
                TextView tv = contactViewHolder.getView(R.id.first_alpha);
                tv.setText(currentAlpha);
            } else {
                contactViewHolder.getView(R.id.first_alpha).setVisibility(View.GONE);
            }
        }else{
            TextView tv = contactViewHolder.getView(R.id.first_alpha);
            tv.setText(contactBean.getFirstHeadLetter());
        }
    }

    public void setOnGetLetterBySectionsListener(OnGetLetterBySectionsListener listener){
        this.listener=listener;
    }

    public interface OnGetLetterBySectionsListener{
         void getLetterBySelected(Map<String, Integer> alphaIndexer, List<String> sections);

    }

    @Override
    public int getTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
