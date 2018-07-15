package com.example.abdelrahman.savealife

/**
 * Created by Abdelrahman on 6/24/2018.
 */
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat

class  Notifications(){

    val NOTIFIYTAG="new request"
    fun Notify(context: Context,message:String,number:Int){
        val intent=Intent(context,LogIn::class.java)

        val builder=NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Need your help")
                .setContentText(message)
                .setNumber(number)
                .setSmallIcon(R.drawable.abc)
                .setContentIntent(PendingIntent.getActivity(context
                        ,0,intent,PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true)

        val nm=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFIYTAG, 0, builder.build())
        }else{
            nm.notify(NOTIFIYTAG.hashCode(), builder.build())
        }

    }

}
