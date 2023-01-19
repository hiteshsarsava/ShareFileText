package com.sharing.handler

import android.net.Uri
import java.io.Serializable

class ShareModel(text: String, fileUri: Uri?, shareType: ShareType) : Serializable {
    var text = ""
    var fileUri: Uri?
    var shareType: ShareType

    init {
        this.text = text
        this.fileUri = fileUri
        this.shareType = shareType
    }
}