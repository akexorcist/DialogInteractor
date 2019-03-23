package com.akexorcist.library.dialoginteractor.sample.dialog.alert

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogListener

interface AlertListener : DialogListener {
    fun onOkButtonClick(key: String?, data: Bundle?)
}
