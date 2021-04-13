package com.akexorcist.library.dialoginteractor.sample.dialog.edit

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogListener
import com.akexorcist.library.dialoginteractor.sample.vo.User

interface EditListener : DialogListener {
    fun onEditSuccess(user: User?, key: String?, data: Bundle?)

    fun onEditFailure(error: String?, key: String?, data: Bundle?)
}
