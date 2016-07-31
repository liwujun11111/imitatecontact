package com.leo.imitatecontact.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by LEO on 16-7-19.
 */
public class ContactBean implements Serializable {
    private int iconId;
    private String title;
    private String phoneNum;
    private String descriptor;
    private String firstHeadLetter;
    private String headLetterNum;
    private String pinyin;
    private Uri uri;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public ContactBean(int iconId, String title, String phoneNum, String descriptor, String firstHeadLetter, String headLetterNum) {
        this.iconId = iconId;
        this.title = title;
        this.phoneNum = phoneNum;
        this.descriptor = descriptor;
        this.firstHeadLetter=firstHeadLetter;
        this.headLetterNum=headLetterNum;
    }

    public ContactBean() {

    }

    public int getIconId() {
        return iconId;
    }

    public String getFirstHeadLetter() {
        return firstHeadLetter;
    }

    public void setFirstHeadLetter(String firstHeadLetter) {
        this.firstHeadLetter = firstHeadLetter;
    }

    public String getHeadLetterNum() {
        return headLetterNum;
    }

    public void setHeadLetterNum(String headLetterNum) {
        this.headLetterNum = headLetterNum;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
