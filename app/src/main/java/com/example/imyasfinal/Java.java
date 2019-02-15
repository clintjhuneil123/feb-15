package com.example.imyasfinal;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class Java extends Application {

    public static final String bookReceived = "BookReceived";
    public static final String myReservation = "MyReservation";
//    public static final String bookdecline = "Bookdeclined";



    @Override
    public void onCreate() {
        super.onCreate();

        notificationChannels();
    }

    private void notificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(bookReceived, "Approved/Declined Bookings", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Approved/Declined Reservations");

            NotificationChannel channel1 = new NotificationChannel(myReservation, "Received Bookings", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Received Reservations");




            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.createNotificationChannel(channel1);


        }

    }
}
