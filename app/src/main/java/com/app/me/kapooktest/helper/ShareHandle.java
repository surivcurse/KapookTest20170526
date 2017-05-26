package com.app.me.kapooktest.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.app.me.kapooktest.modelclass.NewContentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuRiV on 2/5/2560.
 */

public class ShareHandle {

    public static final String LINE_ACTIVITY_INFO_NAME = "jp.naver.line.android";
    public static final String LINECORP_ACTIVITY_INFO_NAME = "com.linecorp.linekeep";
    public static final String LINE_CLASS_NAME = "jp.naver.line.android.activity.selectchat.SelectChatActivity";
    public static final String FACEBOOK_ACTIVITY_INFO_NAME = "com.facebook.katana";
    public static final String GMAIL_ACTIVITY_INFO_NAME = "android.gm";
    public static final String EMAIL_ACTIVITY_INFO_NAME = "android.email";
    public static final String TWEETER_ACTIVITY_INFO_NAME = "com.twitter.android";

    private Context context;
    private String[] extraEmail = new String[]{};
    private String extraSubject;
    private String extraText;
    private String contentLinkHtml;

    public ShareHandle(String[] extraEmail,String extraSubject,String extraText,String contentLinkHtml,Context context){
        if(extraEmail != null){
            this.extraEmail = extraEmail;
        }
        this.extraSubject = extraSubject;
        this.extraText = extraText;
        this.context = context;
        this.contentLinkHtml = contentLinkHtml;

    }

    private Intent intentActionSend(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, extraEmail);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, extraSubject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, extraText+"\n"+contentLinkHtml);
        return shareIntent;
    }

    public void startShareActivity(String activityInfoName){
        Intent shareIntent = intentActionSend();

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            Log.d("activityInfo", "startShareActivity: "+app.activityInfo.name);
            if ((app.activityInfo.name).contains(activityInfoName)) {
                final ActivityInfo activity = app.activityInfo;

                if(activityInfoName.equals(LINECORP_ACTIVITY_INFO_NAME) ){
                    //final ComponentName name = new ComponentName(LINE_ACTIVITY_INFO_NAME, activity.name);
                    //shareIntent.setComponent(name);
                    shareIntent.setClassName(LINE_ACTIVITY_INFO_NAME, LINE_CLASS_NAME);
                }else{
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.setComponent(name);
                }

                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                context.startActivity(shareIntent);
                return;
            }
        }
        if(activityInfoName.equals(LINECORP_ACTIVITY_INFO_NAME)){
            showGooglePlayApp(LINE_ACTIVITY_INFO_NAME);
        }
    }

    private void showGooglePlayApp(String appPackageName){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void createChooserShareContent(){

        //Resources resources = getResources();

        Intent intent1 = new Intent();
        intent1.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        intent1.putExtra(Intent.EXTRA_TEXT, extraText +"\n"+contentLinkHtml);
        intent1.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
        intent1.setType("message/rfc822");

        PackageManager pm = context.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");


        Intent openInChooser = Intent.createChooser(intent1,extraSubject.substring(0,10)+" . . .");

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if(packageName.contains("android.email")) {
                intent1.setPackage(packageName);
            } else if(packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana") || packageName.contains("android.gm") || packageName.contains("jp.naver.line.android")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, extraText+"\n"+contentLinkHtml);
                intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
                intent.setType("message/rfc822");


                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        context.startActivity(openInChooser);
    }


}
