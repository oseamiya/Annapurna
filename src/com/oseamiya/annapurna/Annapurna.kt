package com.oseamiya.annapurna

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.google.appinventor.components.annotations.SimpleEvent
import com.google.appinventor.components.annotations.SimpleFunction
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent
import com.google.appinventor.components.runtime.ComponentContainer
import com.google.appinventor.components.runtime.EventDispatcher
import com.lightcompressorlibrary.CompressionListener
import com.lightcompressorlibrary.VideoCompressor
import com.lightcompressorlibrary.VideoQuality
import com.lightcompressorlibrary.config.Configuration

class Annapurna(container: ComponentContainer) : AndroidNonvisibleComponent(container.`$form`()) {
    private val context : Context

    @SimpleEvent
    fun OnStart() {
        EventDispatcher.dispatchEvent(this, "OnStart")
    }
    @SimpleEvent
    fun OnSuccess() {
        EventDispatcher.dispatchEvent(this, "OnSuccess")
    }

    @SimpleEvent
    fun OnFailure(failureMessage: String?) {
        EventDispatcher.dispatchEvent(this, "OnFailure", failureMessage)
    }

    @SimpleEvent
    fun OnProgress(percent: Float) {
        EventDispatcher.dispatchEvent(this, "OnProgress", percent)
    }

    @SimpleEvent
    fun OnCancelled() {
        EventDispatcher.dispatchEvent(this, "OnCancelled")
    }
    @SimpleFunction
    fun CompressVideo(sourceUrl : String , destinationPath : String ) {
        VideoCompressor.start( context = context , srcUri = Uri.parse(sourceUrl) , destPath = destinationPath ,
            listener = object : CompressionListener {
            override fun onStart() {
                OnStart()
            }

            override fun onSuccess() {
                OnSuccess()
            }

            override fun onFailure(failureMessage: String) {
                OnFailure(failureMessage)
            }
            override fun onProgress(percent: Float) {
                (context as Activity).runOnUiThread { OnProgress(percent) }
            }

            override fun onCancelled() {
                OnCancelled()
            }
        } ,
            configureWith = Configuration(
            quality = VideoQuality.MEDIUM,
            frameRate = 24,
            isMinBitrateCheckEnabled = true,
            videoBitrate = 3677198
        ))
    }

    init {
        context = container.`$context`()
    }
}



