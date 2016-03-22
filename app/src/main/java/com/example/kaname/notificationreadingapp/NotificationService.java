package com.example.kaname.notificationreadingapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kaname on 2016/03/20.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService implements TextToSpeech.OnInitListener{

    private String TAG = "Notification";

    private String TwitterName = "com.twitter.android";
    private String LineName = "jp.naver.line.android";

    //音声読み上げのセット
    private TextToSpeech Textspeech;

    @Override
    public void onCreate() {
        super.onCreate();

        //TextToSpeechオブジェクトの生成
        Textspeech = new TextToSpeech(this, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(Textspeech != null){
            Textspeech.shutdown();
        }
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {//通知が来た時
        String packagename = sbn.getPackageName();
        String speakword = String.valueOf(sbn.getNotification().tickerText);

        if(packagename.equals(LineName)){//Lineの場合の処理
            String regex = " : ";
            Pattern p = Pattern.compile(regex);

            Matcher m = p.matcher(speakword);
            String result = m.replaceFirst("さんからのラインです。　");

            showLog(sbn);
            Log.d(TAG, result);

            //マナーモードであれば読み上げをしないようにする
            if(SetRingerMode()) Textspeech.speak(result, TextToSpeech.QUEUE_FLUSH, null); //これで話す

        }
    }
    private boolean SetRingerMode() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        switch (ringerMode){
            case AudioManager.RINGER_MODE_NORMAL:
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {//通知が削除
    }

    private void showLog( StatusBarNotification sbn ){
        int id = sbn.getId();
        String name = sbn.getPackageName();
        long time = sbn.getPostTime();
        boolean clearable = sbn.isClearable();
        boolean playing = sbn.isOngoing();
        CharSequence text = sbn.getNotification().tickerText;

        Log.d(TAG, "id:" + id + " name:" + name + " time:" + time);
        Log.d(TAG,"isClearable:" + clearable +
                " isOngoing:" + playing + " tickerText:" +text);
    }

    @Override
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status){
            Locale local = Locale.JAPAN; //言語設定
            if (Textspeech.isLanguageAvailable(local) >= TextToSpeech.LANG_AVAILABLE){
                Textspeech.setLanguage(local);
            }else{
                Log.e("","Error SetLocale");
            }
        }else{
            Log.e("","Error Init");
        }
    }
}

