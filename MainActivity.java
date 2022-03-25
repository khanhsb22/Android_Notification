package com.example.demoapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.demoapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;
    private static final String CHANNEL_ID = "1000";
    private NotificationCompat.Builder builder = null;
    private NotificationCompat.Builder progressBuilder = null;
    private final int notificationId = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

        binding.buttonBasicNotification.setOnClickListener(view -> {
            setNotificationContent();
        });

        binding.buttonProgressBar.setOnClickListener(view -> {
            setProgressNotificationContent();
        });

        binding.buttonLargeImage.setOnClickListener(view -> {
            setLargeImageNotification();
        });

        binding.buttonLargeBlockOfText.setOnClickListener(view -> {
            setLargeBlockOfText();
        });
    }

    private void setLargeBlockOfText() {
        String senderName = "John";
        String subject = "Lorem ipsum";
        String bigText = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).";
        Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.person_icon);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_mail)
                .setContentTitle(senderName)
                .setContentText(subject)
                .setLargeIcon(bitmapImage) // Icon in right
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .build();
        notificationManagerCompat.notify(notificationId, notification);
    }

    private void setLargeImageNotification() {
        Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.abstract_image);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_large_image)
                .setContentTitle("Screenshot captured")
                .setContentText("Tap to view your screenshot")
                .setLargeIcon(bitmapImage) // Icon in right
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmapImage)
                        .bigLargeIcon(null))
                .build();
        notificationManagerCompat.notify(notificationId, notification);
    }

    private void setProgressNotificationContent() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        progressBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        progressBuilder.setContentTitle("Download file")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_cloud_download)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        progressBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManagerCompat.notify(notificationId, progressBuilder.build());
        // When download finish, call bellow codes
        progressBuilder.setContentText("Download complete")
                .setProgress(0, 0, false);
        notificationManagerCompat.notify(notificationId, progressBuilder.build());
    }

    private void createNotificationChannel() {
        // For Android 8.0 and higher must register your app's notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setNotificationContent() {
        // Set the notification's tap action
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Demo app notification")
                .setContentText("Hello world !")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                // Calls setAutoCancel(), which automatically removes the notification when the user taps it.
                .setAutoCancel(true);

        notificationManagerCompat.notify(notificationId, builder.build());
    }

    /*
     * Basic notification:
     * https://developer.android.com/training/notify-user/build-notification#java
     *
     * Expandable notification:
     * https://developer.android.com/training/notify-user/expanded
     * */
}