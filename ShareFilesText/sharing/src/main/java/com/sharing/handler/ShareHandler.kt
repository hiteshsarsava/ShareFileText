package com.sharing.handler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.sharing.R

class ShareHandler {
    private var activity: Activity? = null
    private var taskResult: TaskResult? = null

    /**
     * To handle the received data from other application via intent
     *
     * @param intent : the intent shared by other apps.
     */
    fun handle(activity: Activity, intent: Intent): TaskResult? {
        this.activity = activity
        taskResult = TaskResult.instance
        // Get intent, action and MIME type
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/html" == type) {
                handleSendText(intent) // Handle text being sent
            } else if ("text/plain" == type) {
                handleSendText(intent) // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent) // Handle single image being sent
            } else if (type.startsWith("video/")) {
                handleSendVideo(intent) // Handle single video being sent
            } else {
                handleSendData(intent)
            }
        } else if (Intent.ACTION_SEND_MULTIPLE == action && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent) // Handle multiple images being sent
            } else if (type.startsWith("video/")) {
                handleSendMultipleVideos(intent) // Handle multiple videos being sent
            } else {
                handleSendMultipleData(intent)
            }
        } else {
//            taskResult.setSuccessFull(false)
//            taskResult.setErrorMsg(activity.getString(R.string.emptyData))
            taskResult?.isSuccessFull = false
            taskResult?.errorMsg = activity.getString(R.string.emptyData)
        }
        return taskResult
    }

    /**
     * To handle the multiple data shared by app. E.g, Audio, video, text or images
     *
     * @param intent : the intent shared by other apps.
     */
    @Suppress("DEPRECATION")
    private fun handleSendMultipleData(intent: Intent) {
        val uris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
        }
        if (uris != null && uris.isNotEmpty()) {
            val shareModels: MutableList<ShareModel> = ArrayList()
            for (uri in uris) {
                shareModels.add(ShareModel("", uri, getShareType(uri)))
            }
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
        } else {
//            taskResult.setSuccessFull(false)
//            taskResult.setErrorMsg(activity!!.getString(R.string.emptyData))
            taskResult?.isSuccessFull = false
            taskResult?.errorMsg = activity?.getString(R.string.emptyData) ?: ""
        }
    }

    /**
     * To handle the text data shared by app.
     *
     * @param intent : the intent shared by other apps.
     */
    @Suppress("DEPRECATION")
    private fun handleSendData(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            val shareModels: MutableList<ShareModel> = ArrayList()
            shareModels.add(ShareModel(sharedText, null, ShareType.TEXT))
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
            return
        }
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        if (uri != null) {
            val shareModels: MutableList<ShareModel> = ArrayList()
            shareModels.add(ShareModel("", uri, getShareType(uri)))
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
            return
        }
//        taskResult.setSuccessFull(false)
//        taskResult.setErrorMsg(activity!!.getString(R.string.emptyData))
        taskResult?.isSuccessFull = false
        taskResult?.errorMsg = activity?.getString(R.string.emptyData) ?: ""
    }

    /**
     * To handle the multiple video data shared by app.
     *
     * @param intent : the intent shared by other apps.
     */
    @Suppress("DEPRECATION")
    private fun handleSendMultipleVideos(intent: Intent) {
        val videoUris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
        }
        if (videoUris != null && videoUris.isNotEmpty()) {
            // Update UI to reflect multiple videos being shared
            val shareModels: MutableList<ShareModel> = ArrayList()
            for (uri in videoUris) {
                shareModels.add(ShareModel("", uri, getShareType(uri)))
            }
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
        } else {
//            taskResult.setSuccessFull(false)
//            taskResult.setErrorMsg(activity!!.getString(R.string.emptyData))
            taskResult?.isSuccessFull = false
            taskResult?.errorMsg = activity?.getString(R.string.emptyData) ?: ""
        }
    }

    /**
     * To handle the single video data shared by app.
     *
     * @param intent : the intent shared by other apps.
     */
    @Suppress("DEPRECATION")
    private fun handleSendVideo(intent: Intent) {
        val videoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        if (videoUri != null && videoUri.toString().isNotEmpty()) {
            // Update UI to reflect video being shared
            val shareModels: MutableList<ShareModel> = ArrayList()
            shareModels.add(ShareModel("", videoUri, getShareType(videoUri)))
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
        } else {
//            taskResult.setSuccessFull(false)
//            taskResult.setErrorMsg(activity!!.getString(R.string.emptyData))
            taskResult?.isSuccessFull = false
            taskResult?.errorMsg = activity?.getString(R.string.emptyData) ?: ""
        }
    }

    /**
     * To handle the text data shared by app.
     *
     * @param intent : the intent shared by other apps.
     */
    private fun handleSendText(intent: Intent) {
        var sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        //        TaskResult taskResult = TaskResult.getInstance();
        if (sharedText != null && sharedText.isNotEmpty()) {
            // Update UI to reflect text being shared
            val shareModels: MutableList<ShareModel> = ArrayList()
            shareModels.add(ShareModel(sharedText, null, ShareType.TEXT))
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
        } else if (intent.clipData != null) {
            val shareModels: MutableList<ShareModel> = ArrayList()
            sharedText = intent.clipData!!.getItemAt(0).text.toString()
            shareModels.add(ShareModel(sharedText, null, ShareType.TEXT))
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
        } else {
//            taskResult.setSuccessFull(false)
//            taskResult.setErrorMsg(activity!!.getString(R.string.emptyData))
            taskResult?.isSuccessFull = false
            taskResult?.errorMsg = activity?.getString(R.string.emptyData) ?: ""
        }
    }

    /**
     * To handle the single image data shared by app.
     *
     * @param intent : the intent shared by other apps.
     */
    @Suppress("DEPRECATION")
    private fun handleSendImage(intent: Intent) {
        val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        if (imageUri != null && imageUri.toString().isNotEmpty()) {
            // Update UI to reflect image being shared
            val shareModels: MutableList<ShareModel> = ArrayList()
            shareModels.add(ShareModel("", imageUri, getShareType(imageUri)))
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
        } else {
//            taskResult.setSuccessFull(false)
//            taskResult.setErrorMsg(activity!!.getString(R.string.emptyData))
            taskResult?.isSuccessFull = false
            taskResult?.errorMsg = activity?.getString(R.string.emptyData) ?: ""
        }
    }

    /**
     * To handle the multiple image data shared by app.
     *
     * @param intent : the intent shared by other apps.
     */
    @Suppress("DEPRECATION")
    private fun handleSendMultipleImages(intent: Intent) {
        val imageUris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
        }
        if (imageUris != null && imageUris.isNotEmpty()) {
            // Update UI to reflect multiple images being shared
            val shareModels: MutableList<ShareModel> = ArrayList()
            for (uri in imageUris) {
                shareModels.add(ShareModel("", uri, getShareType(uri)))
            }
//            taskResult.setSuccessFull(true)
//            taskResult.setShareDataList(shareModels)
            taskResult?.isSuccessFull = true
            taskResult?.shareDataList = shareModels
        } else {
//            taskResult.setSuccessFull(false)
//            taskResult.setErrorMsg(activity!!.getString(R.string.emptyData))
            taskResult?.isSuccessFull = false
            taskResult?.errorMsg = activity?.getString(R.string.emptyData) ?: ""
        }
    }

    /**
     * To get the type of data share by app. E.g., Image, audio or video
     *
     * @param uri : the uri shared by other apps.
     */
    private fun getShareType(uri: Uri): ShareType {
        val shareType: ShareType = if (Utils.isImageFile(uri.path)) {
            ShareType.IMAGE
        } else if (Utils.isAudioFile(uri.path)) {
            ShareType.AUDIO
        } else if (Utils.isVideoFile(uri.path)) {
            ShareType.VIDEO
        } else {
            ShareType.DOC
        }
        return shareType
    }
}