package com.nav.notificationdemo.pdf;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

class CustomWebView extends WebView
{
    public int rawContentWidth   = 0;                         //unneeded
    public int rawContentHeight  = 0;                         //unneeded
    Context mContext          = null;                      //unneeded

    public CustomWebView(Context context)                     //unused constructor
    {
        super(context);
        mContext = this.getContext();
    }   

    public CustomWebView(Context context, AttributeSet attrs) //inflate constructor
    {
        super(context,attrs);
        mContext = context;
    }

    public int getContentWidth()
    {
        int ret = super.computeHorizontalScrollRange();//working after load of page
        rawContentWidth = ret;
        return ret;
    }

    public int getContentHeight()
    {
        int ret = super.computeVerticalScrollRange(); //working after load of page
        rawContentHeight = ret;
        return ret;
    }
//=========
}//class
//=========