package com.akexorcist.library.dialoginteractor.sample.dialog

import androidx.lifecycle.ViewModel
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.sample.dialog.alert.AlertMapper
import com.akexorcist.library.dialoginteractor.sample.dialog.confirm.ConfirmMapper
import com.akexorcist.library.dialoginteractor.sample.dialog.edit.EditMapper

class DialogViewModel : ViewModel() {
    val alert = DialogLauncher(AlertMapper::class.java)

    val confirm = DialogLauncher(ConfirmMapper::class.java)

    val edit = DialogLauncher(EditMapper::class.java)
}