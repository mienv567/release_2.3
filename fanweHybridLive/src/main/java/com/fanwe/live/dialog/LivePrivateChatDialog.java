package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.appview.LivePrivateChatView;
import com.fanwe.live.appview.LivePrivateChatView.ClickListener;
import com.fanwe.live.event.ELivePrivateChatDialogDissmis;
import com.sunday.eventbus.SDEventManager;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-27 下午8:54:36 类说明
 */
public class LivePrivateChatDialog extends LiveBaseDialog
{

    private int defaultHeight;
    private LivePrivateChatView chatView;

    public LivePrivateChatDialog(Activity activity, String user_id)
    {
        super(activity);

        defaultHeight = SDViewUtil.getScreenHeightPercent(0.5f);

        chatView = new LivePrivateChatView(activity);
        chatView.setClickListener(new ClickListener()
        {

            @Override
            public void onClickBack()
            {
                ELivePrivateChatDialogDissmis eventdismiss = new ELivePrivateChatDialogDissmis();
                eventdismiss.dialog_close_type = 1;
                SDEventManager.post(eventdismiss);
                dismiss();
            }
        });

        chatView.setExtendVisibilityChangeListener(new LivePrivateChatView.ExtendVisibilityChangeListener()
        {
            @Override
            public void onShow(final View v)
            {
                new SDDelayRunnable()
                {
                    @Override
                    public void run()
                    {
                        SDViewUtil.setViewHeight(chatView, defaultHeight + SDViewUtil.getViewHeight(v));
                    }
                }.runDelay(200);
            }

            @Override
            public void onHide(View v)
            {
                SDViewUtil.setViewHeight(chatView, defaultHeight);
            }
        });
        setContentView(chatView);
        chatView.setUserId(user_id);

        SDViewUtil.setViewHeight(chatView, defaultHeight);

        setCanceledOnTouchOutside(false);
        paddings(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = super.onTouchEvent(event);
        if (!SDViewUtil.isTouchView(getContentView(), event))
        {
            chatView.hideInputMethod();
            ELivePrivateChatDialogDissmis eventdismiss = new ELivePrivateChatDialogDissmis();
            SDEventManager.post(eventdismiss);
            dismiss();
        }
        return result;
    }
}
