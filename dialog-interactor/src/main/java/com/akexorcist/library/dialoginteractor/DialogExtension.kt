package com.akexorcist.library.dialoginteractor

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModel

fun <MAPPER : EventMapper<LISTENER>, LISTENER : DialogListener, VM : ViewModel> InteractorDialog<MAPPER, LISTENER, VM>.createBundle(
        key: String? = null,
        data: Bundle? = null
): Bundle = Bundle().apply {
    putString(InteractorDialog.EXTRA_KEY, key)
    putBundle(InteractorDialog.EXTRA_DATA, data)
}

fun Activity.getDialogByTag() {

}