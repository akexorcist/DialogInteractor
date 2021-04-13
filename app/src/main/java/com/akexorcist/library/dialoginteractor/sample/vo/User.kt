package com.akexorcist.library.dialoginteractor.sample.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String?,
    var name: String?,
    var age: Int = 0
) : Parcelable
