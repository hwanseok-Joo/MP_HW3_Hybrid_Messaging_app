package com.example.hsju.hw4hybrid_messaging_app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * javaScriptInterface class이다. 여기서는 html에서 받은 값들을 저장하고 처리하는 과정을 한다.
 * void sendNum(String) fuction은 버튼을 입력한 값을 받아와서 현재 입력된 숫자뒤에 붙여 update해준다.
 * String getNum() function은 현재 입력된 번호를 리턴해준다.
 * void sendSMS(String) function은 첨부파일을 참조한 코드로 입력된 메시지를 보내주고 상황에 맞는 메시지를
 * 토스트 메시지로 보여준다.
 * Created by HS.Ju on 2016-05-14.
 */
public class JavaScriptInterface {
    Context mContext;
    String mNum;
    /** Instantiate the interface and set the context */
    JavaScriptInterface(Context c) {
        mContext = c;
        mNum="";
    }
    /** Show a toast from the web page */
    @JavascriptInterface
    public void sendNum(String num) {


            mNum = mNum.concat(num);

    }
    @JavascriptInterface
       public String getNum(){
              return mNum;
    }

    @JavascriptInterface
    public void sendSMS(String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        /**
         * SMS가 발송될때 실행
         * When the SMS massage has been sent
         */
       mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(this.getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        /**
         * SMS가 도착했을때 실행
         * When the SMS massage has been delivered
         */
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(mNum, null, smsText, sentIntent, deliveredIntent);
    }
}
