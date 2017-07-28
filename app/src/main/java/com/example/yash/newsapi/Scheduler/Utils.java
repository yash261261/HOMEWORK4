package com.example.yash.newsapi.Scheduler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Yash on 7/27/17.
 */

public class Utils {

    // NEWS UPDATE AFTER 60 SECONDS
    private static final int SCHEDULE_INTERVAL_MINUTES=60;
   // private static final int SYNC_FLEXTIME_SECONDS=60;
    private static final String JOB_TAG="news_job_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleRefresh(@NonNull final Context context){
        //Schedules when the database will be refreshed

//Prevents scheduling if that has occurred already
        if(sInitialized) return;


        Driver driver=new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher=new FirebaseJobDispatcher(driver);

        Job contraintRefresh=dispatcher.newJobBuilder()
                .setService(Service.class)
                .setTag(JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_MINUTES,SCHEDULE_INTERVAL_MINUTES))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(contraintRefresh);
        sInitialized=true;
    }
}
