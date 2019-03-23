package com.akexorcist.library.dialoginteractor.sample.dialog.confirm

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogListener

interface ConfirmListener : DialogListener {
    fun onConfirmButtonClick(key: String?, data: Bundle?)

    fun onCancelButtonClick(key: String?, data: Bundle?)
}