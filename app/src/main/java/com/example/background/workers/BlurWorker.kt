package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import java.lang.IllegalArgumentException

private const val TAG = "BlurWorker"
class BlurWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceURI = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("Blurring image", appContext)

        sleep()

        try {
            /*
            val picture = BitmapFactory.decodeResource(
                appContext.resources,
                R.drawable.android_cupcake
            )*/
            if(TextUtils.isEmpty(resourceURI)){

                Log.e(TAG, "Invalud input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val picture = BitmapFactory.decodeStream(appContext.contentResolver
                .openInputStream(Uri.parse(resourceURI)))
            val tempURI = writeBitmapToFile(appContext, blurBitmap(picture, appContext))
            makeStatusNotification("URI is $tempURI", appContext)
            val outputData = workDataOf(KEY_IMAGE_URI to tempURI.toString())
            return Result.success(outputData)
        } catch(e: Exception){
            makeStatusNotification("Error occurred blurring image", appContext)
            Log.e(TAG, "Error applying blur")
            return Result.failure()
        }



    }

}