package com.sharing.composer

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.core.content.FileProvider
import java.io.File

class ShareComposer(private val context: Context?) {
    init {
        if (context == null) {
            throw RuntimeException("The context given is null")
        }
    }

    /**
     * To share the text with other apps via intent sharing
     *
     * @param title : Title of the share.
     * @param text  : Actual text to share with app
     */
    fun shareText(title: String, text: String, textMimeType: TextMimeType) {
        val builder = IntentBuilder(context!!)
            .setType(textMimeType.mimeType)
            .setChooserTitle(title)
        if (textMimeType == TextMimeType.HTML) {
            builder.setHtmlText(text)
        } else {
            builder.setText(text)
        }
        builder.startChooser()
    }

    /**
     * To share the text with an email format. Email sender app will receive this in its completed form.
     *
     * @param title        : Title of the share.
     * @param emailMsg     : Email message to send.
     * @param emailSubject : Subject of email.
     * @param setEmailTo   : arrays of the email receivers.
     */
    fun sendEmail(
        title: String,
        emailMsg: String,
        emailSubject: String,
        setEmailTo: Array<String?>
    ) {
        val builder = IntentBuilder(context!!)
            .setType(TextMimeType.TEXT.mimeType)
            .setChooserTitle(title)
            .setText(emailMsg)
        builder.setSubject(emailSubject)
        builder.setEmailTo(setEmailTo)
        builder.startChooser()
    }

    /**
     * To share the file with other apps via intent sharing
     *
     * @param title : Title of the share.
     * @param uri   : Content Uri of the file.
     */
    fun shareFile(title: String, uri: Uri) {
        var uri = uri
        val cacheFile = File(uri.path)
        uri = FileProvider.getUriForFile(context!!, context.packageName + ".provider", cacheFile)
        var mimeType = context.contentResolver.getType(uri)
        if (mimeType == null) {
            mimeType = ImageMimeType.JPEG.mimeType
        }
        val intent = IntentBuilder(context)
            .setType(mimeType)
            .setStream(uri)
            .setChooserTitle(title)
            .createChooserIntent()
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * To share the file with other apps via intent sharing
     *
     * @param title : Title of the share.
     * @param path  : path of the file
     */
    fun shareFile(title: String, path: String) {
        shareFile(title, Uri.parse(path))
    }

    /**
     * To share the file with other apps via intent sharing
     *
     * @param title : Title of the share.
     * @param file  : file object.
     */
    fun shareFile(title: String, file: File) {
        shareFile(title, Uri.fromFile(file))
    }

    /**
     * To share the multiple files with other apps via intent sharing
     *
     * @param title : Title of the share.
     * @param uris  : Content Uris of the image.
     */
    fun shareFile(title: String, uris: Array<Uri?>) {
        val builder = IntentBuilder(context!!)
            .setChooserTitle(title)
        for (uri in uris) {
            val cacheFile = File(uri!!.path)
            val newUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                cacheFile
            )
            builder.addStream(newUri)
            var mimeType = context.contentResolver.getType(newUri)
            if (mimeType == null) {
                mimeType = ImageMimeType.JPEG.mimeType
            }
            builder.setType(mimeType)
        }
        val intent = builder.createChooserIntent()
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        //                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * To share the file with other apps via intent sharing
     *
     * @param title : Title of the share.
     * @param files : Arrays of file object
     */
    fun shareFile(title: String, files: Array<File?>) {
        val uris = arrayOfNulls<Uri>(files.size)
        for (i in files.indices) {
            uris[i] = Uri.fromFile(files[i])
        }
        shareFile(title, uris)
    }

    /**
     * To share the file with other apps via intent sharing
     *
     * @param title        : Title of the share.
     * @param pathsOfFiles : Arrays of file object
     */
    fun shareFile(title: String, pathsOfFiles: Array<String?>) {
        val uris = arrayOfNulls<Uri>(pathsOfFiles.size)
        for (i in pathsOfFiles.indices) {
            uris[i] = Uri.parse(pathsOfFiles[i])
        }
        shareFile(title, uris)
    }

    /**
     * To share the text with other apps via intent sharing
     *
     * @param title : Title of the share.
     * @param text  : Actual text to share with app
     */
    fun shareTextWithFile(
        title: String,
        text: String,
        textMimeType: TextMimeType,
        pathsOfFiles: Array<String?>
    ) {
        val builder = IntentBuilder(context!!)
            .setChooserTitle(title)
        if (textMimeType === TextMimeType.HTML) {
            builder.setHtmlText(text)
        } else {
            builder.setText(text)
        }
        if (pathsOfFiles.isNotEmpty()) {
            val uris = arrayOfNulls<Uri>(pathsOfFiles.size)
            for (i in pathsOfFiles.indices) {
                uris[i] = Uri.parse(pathsOfFiles[i])
            }
            for (uri in uris) {
                val cacheFile = File(uri!!.path)
                val newUri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    cacheFile
                )
                builder.addStream(newUri)
                var mimeType = context.contentResolver.getType(newUri)
                if (mimeType == null) {
                    mimeType = ImageMimeType.JPEG.mimeType
                }
                builder.setType(mimeType)
            }
            val intent = builder.createChooserIntent().putExtra(Intent.EXTRA_TEXT, text)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            //                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            try {
                context.startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            builder.setType(textMimeType.mimeType)
            builder.startChooser()
        }
    }
}