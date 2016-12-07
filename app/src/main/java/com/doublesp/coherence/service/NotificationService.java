package com.doublesp.coherence.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.doublesp.coherence.R;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public void onCreate() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference sharedWithReference = firebaseDatabase.getReference().child(
                ConstantsAndUtils.NOTIFY).child(ConstantsAndUtils.getOwner(this));

        sharedWithReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String val = (String) dataSnapshot.getValue();
                    postNotification(val);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void postNotification(String val) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setAutoCancel(true)
                .setOngoing(false);

        StringBuilder sharableContentBuilder = new StringBuilder();
        sharableContentBuilder
                .append("http://doublesp.com/shared/")
                .append(val);

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(sharableContentBuilder.toString()));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child(ConstantsAndUtils.NOTIFY).
                child(ConstantsAndUtils.getOwner(this)).removeValue();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
