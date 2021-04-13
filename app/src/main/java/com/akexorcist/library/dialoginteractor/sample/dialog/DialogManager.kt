package com.akexorcist.library.dialoginteractor.sample.dialog

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.akexorcist.library.dialoginteractor.sample.dialog.alert.AlertDialog
import com.akexorcist.library.dialoginteractor.sample.dialog.confirm.ConfirmDialog
import com.akexorcist.library.dialoginteractor.sample.dialog.edit.EditDialog
import com.akexorcist.library.dialoginteractor.sample.vo.User

object DialogManager {
    fun showAlert(
        message: String? = null,
        key: String? = null,
        data: Bundle? = null,
        fragmentManager: FragmentManager
    ) {
        AlertDialog.Builder()
            .setMessage(message)
            .setKey(key)
            .setData(data)
            .build()
            .show(fragmentManager)
    }

    fun showConfirm(
        title: String? = null,
        message: String? = null,
        key: String? = null,
        data: Bundle? = null,
        fragmentManager: FragmentManager
    ) {
        ConfirmDialog.Builder()
            .setTitle(title)
            .setMessage(message)
            .setKey(key)
            .setData(data)
            .build()
            .show(fragmentManager)
    }

    fun showEdit(
        message: String? = null,
        user: User? = null,
        key: String? = null,
        data: Bundle? = null,
        fragmentManager: FragmentManager
    ) {
        EditDialog.Builder()
            .setMessage(message)
            .setUser(user)
            .setKey(key)
            .setData(data)
            .build()
            .show(fragmentManager)
    }
}
