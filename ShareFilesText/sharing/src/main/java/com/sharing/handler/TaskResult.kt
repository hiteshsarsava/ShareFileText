package com.sharing.handler

class TaskResult {
    var isSuccessFull = false
    var shareDataList: List<ShareModel> = ArrayList()
    var errorMsg = ""

    companion object {
        var instance: TaskResult? = null
            get() {
                if (field == null) {
                    field = TaskResult()
                }
                return field
            }
            private set
    }
}