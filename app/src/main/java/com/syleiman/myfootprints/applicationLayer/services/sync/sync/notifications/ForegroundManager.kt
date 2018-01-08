package com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R

class ForegroundManager(private val service: Service) : IForegroundManager
{
    private var isInForegroundMode = false

    private val notificationManager: NotificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val defaultIcon: Bitmap = BitmapFactory.decodeResource(service.resources, R.mipmap.ic_launcher)         // App icon

    companion object
    {
        private val NotificationId = 449445465         // Some unique id
    }

    /** Turn on foreground mode for service */
    @Synchronized override fun startForegroundMode()
    {
        if (!isInForegroundMode)
        {
            isInForegroundMode = true
            service.startForeground(NotificationId, createNotification(0, 0, null))
        }
    }

    /** Turn off foreground mode for service */
    @Synchronized override fun stopForegroundMode()
    {
        notificationManager.cancel(NotificationId)
        service.stopForeground(true)
        isInForegroundMode = false
    }

    /**
     * Update progressBar on foreground panel
     * @param value current progressBar value (<= @maxValue)
     * @param maxValue max progressBar value
     */
    override fun updateProgress(value: Int, maxValue: Int, text: String) = notificationManager.notify(NotificationId, createNotification(value, maxValue, text))

    private fun createNotification(progressValue: Int, maxProgressValue: Int, text: String?): Notification
    {
        return Notification.Builder(service).
                setSmallIcon(R.mipmap.ic_launcher).
                setLargeIcon(defaultIcon).
                setContentTitle(App.application.
                getString(R.string.app_name)).
                setProgress(maxProgressValue, progressValue, false).
                setContentText(text).
                build()
    }
}