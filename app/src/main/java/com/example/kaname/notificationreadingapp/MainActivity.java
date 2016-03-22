package com.example.kaname.notificationreadingapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private String TwitterName = "com.twitter.android";
    private String LineName = "jp.naver.line.android";

    private RadioButton radiobutton;
    private NotificationManager manager;


    private static int NOTIFICATION_MINIMUM_ID = 112;

    private static int REQUEST_CODE_RESULT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setInitializeInstallApp();
        setInitializeNotificationSet();

        radiobutton = (RadioButton)findViewById(R.id.Radio_appButton);
        setCheckRadioButton();

        radiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 11);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_RESULT){
            setInitializeNotificationSet(); //意地でも許可させる
        }

        setCheckRadioButton();

        //ONであれば通知バー表示、OFFであれば消す
        if(radiobutton.isChecked()) setNotificationBar();
        else manager.cancel(NOTIFICATION_MINIMUM_ID);

    }

    //TODO ラジオボタンのチェック
    private void setCheckRadioButton() {
        if(isNLServiceNotificationRunning()){
            radiobutton.setChecked(true);
        }else{
            radiobutton.setChecked(false);
        }
    }


    //TODO 通知するアプリがインストールされているか確認(Lineにしてる)
    private void setInitializeInstallApp() {
        if(!isNLInstallAppRunning()){
            new AlertDialog.Builder(this)
                    .setTitle("Lineのインストール")
                    .setMessage("このアプリを使用するにはLineをインストールしてください")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + LineName))); //Line
                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + TwitterName))); //Twitter
                        }
                    })
                    .show();
        }
    }
    private boolean isNLInstallAppRunning(){
        List<ApplicationInfo> applicationInfo = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : applicationInfo) {

            //if(TwitterName.equals(app.packageName)) return true;
            if(LineName.equals(app.packageName)) return true;
        }
        return false;
    }


    //TODO 通知バーの監視が許可しているか確認
    private void setInitializeNotificationSet() {
        if(isNLServiceNotificationRunning()){
            new AlertDialog.Builder(this)
                    .setTitle("※注意※")
                    .setMessage("終了するときは、「現在の状態」の右にあるラジオボタンを押して「Line読み上げさん」をOFFにしてください")
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .show();

            setNotificationBar();

        }else{
            new AlertDialog.Builder(this)
                    .setTitle("通知へのアクセスを許可")
                    .setMessage("このアプリ開始するには「Line読み上げさん」の通知アクセスをONにしてください")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUEST_CODE_RESULT);
                        }
                    })
                    .show();
        }
    }
    private boolean isNLServiceNotificationRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if (NotificationService.class.getName().equals(service.service.getClassName())) { //ここはServiceのClassを入れる
                return true;
            }
        }
        return false;
    }


    //TODO 通知バーに登録
    private void setNotificationBar() {
        PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(getApplicationContext(), MainActivity.class), 0);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder
                .setContentTitle(getResources().getString(R.string.app_jpnname))
                .setContentText("Lineの読み上げ中")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_record_voice_over_orange_a100_48dp);

        Notification notification = builder.build();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        manager.notify(NOTIFICATION_MINIMUM_ID, notification);
    }

}
