package com.leo.imitatecontact.loader;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.leo.imitatecontact.bean.ContactBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by LEO on 2016/7/30.
 */
public class ContactLoader extends AbsBaseLoader<List<ContactBean>>{
    private Context mContext;

    public ContactLoader(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected List<ContactBean> doInBackGround() {
        List<ContactBean> mContactBeanList=new ArrayList<>();
        ContactBean mContactBean=null;
        ContentResolver mContentResolver=mContext.getContentResolver();
        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri=Uri.parse("content://com.android.contacts/data");
        Cursor cursor = null;
        try {
            cursor =mContentResolver.query(uri,null,null,null,null);
            while (cursor.moveToNext()){
                mContactBean=new ContactBean();
                String id=cursor.getString(cursor.getColumnIndex("_id"));
                String title=cursor.getString(cursor.getColumnIndex("display_name"));//获取联系人姓名
                String firstHeadLetter=cursor.getString(cursor.getColumnIndex("phonebook_label"));//这个字段保存了每个联系人首字的拼音的首字母
                mContactBean.setTitle(title);
                mContactBean.setFirstHeadLetter(firstHeadLetter);
                Cursor dataCursor=mContentResolver.query(dataUri,null,"raw_contact_id= ?",new String[]{id},null);
                while(dataCursor.moveToNext()){
                    String type=dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                    if (type.equals("vnd.android.cursor.item/phone_v2")) {//如果得到的mimeType类型为手机号码类型才去接收
                        String phoneNum = dataCursor.getString(dataCursor.getColumnIndex("data1"));//获取手机号码
                        mContactBean.setPhoneNum(phoneNum);
                        Uri photoUri = null;
                        Uri uriNumber2Contacts = Uri.parse("content://com.android.contacts/" + "data/phones/filter/" + phoneNum);
                        Cursor cursorContacts = mContentResolver.query(uriNumber2Contacts, null, null,
                                null, null);
                        if (cursorContacts.getCount() > 0) { //若游标不为0则说明有头像,游标指向第一条记录
                            cursorContacts.moveToFirst();
                            Long contactID = cursorContacts.getLong(cursorContacts
                                    .getColumnIndex("contact_id"));
                            photoUri = ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI, contactID);
                            mContactBean.setUri(photoUri);
                        }
                    }
                }

                dataCursor.close();
                if (mContactBean.getTitle()!=null&&mContactBean.getPhoneNum()!=null){
                    mContactBeanList.add(mContactBean);
                }

            }
            //按拼音首字母表排序
            Collections.sort(mContactBeanList,new Comparator<ContactBean>() {
                @Override
                public int compare(ContactBean t1, ContactBean t2) {
                    String a = t1.getFirstHeadLetter();
                    String b = t2.getFirstHeadLetter();
                    int flag = a.compareTo(b);
                    if (flag == 0) {
                        return a.compareTo(b);
                    } else {
                        return flag;
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return mContactBeanList;
    }

    @Override
    protected void onPostExecute(List<ContactBean> result) {
        super.onPostExecute(result);
    }

}
