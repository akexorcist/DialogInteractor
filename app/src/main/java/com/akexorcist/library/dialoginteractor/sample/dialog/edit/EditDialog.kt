package com.akexorcist.library.dialoginteractor.sample.dialog.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.akexorcist.library.dialoginteractor.sample.R
import com.akexorcist.library.dialoginteractor.sample.databinding.DialogEditBinding
import com.akexorcist.library.dialoginteractor.sample.dialog.DialogViewModel
import com.akexorcist.library.dialoginteractor.sample.vo.User

class EditDialog : InteractorDialog<EditMapper, EditListener, DialogViewModel>() {
    private lateinit var binding: DialogEditBinding
    private var message: String? = null
    private var user: User? = null

    companion object {
        const val EXTRA_MESSAGE = "com.akexorcist.library.dialoginteractor.sample.dialog.edit.extra_message"
        const val EXTRA_USER = "com.akexorcist.library.dialoginteractor.sample.dialog.edit.extra_user"
        const val EXTRA_EDITED_USER = "com.akexorcist.library.dialoginteractor.sample.dialog.edit.extra_edited_user"
        const val EXTRA_ERROR_MESSAGE =
            "com.akexorcist.library.dialoginteractor.sample.dialog.edit.extra_error_message"

        fun newInstance(message: String?, user: User?, key: String?, data: Bundle?) = EditDialog().apply {
            arguments = createBundle(key, data).apply {
                putString(EXTRA_MESSAGE, message)
                putParcelable(EXTRA_USER, user)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        message = when (savedInstanceState) {
            null -> arguments?.getString(EXTRA_MESSAGE)
            else -> savedInstanceState.getString(EXTRA_MESSAGE)
        }
        user = when (savedInstanceState) {
            null -> arguments?.getParcelable(EXTRA_USER)
            else -> savedInstanceState.getParcelable(EXTRA_USER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonConfirm.setOnClickListener {
            onConfirmButtonClick()
        }

        binding.buttonCancel.setOnClickListener {
            onCancelButtonClick()
        }

        binding.textViewMessage.text = message ?: ""

        if (savedInstanceState == null) {
            fillUserName()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_MESSAGE, message)
        outState.putParcelable(EXTRA_USER, user)
    }

    override fun bindViewModel() = DialogViewModel::class.java

    override fun bindLauncher(viewModel: DialogViewModel) = viewModel.edit

    private fun fillUserName() {
        user?.let {
            binding.editTextName.setText(it.name)
        } ?: run {
            onEmptyUserInfo()
            dismiss()
        }
    }

    private fun onConfirmButtonClick() {
        val name = binding.editTextName.text.toString()
        user?.let {
            if (name == it.name) {
                onNameNotChanged()
            } else {
                val newUser = it.copy(name = name)
                onEditUserInfoSuccess(newUser)
            }
        } ?: run {
            onEmptyUserInfo()
        }
    }

    private fun onCancelButtonClick() {
        val error = getString(R.string.edit_user_info_cancel)
        getListener().onEditFailure(error, getKey(), getData())
        dismiss()
    }

    private fun onEditUserInfoSuccess(user: User) {
        getListener().onEditSuccess(user, getKey(), getData())
        dismiss()
    }

    private fun onEmptyUserInfo() {
        val error = getString(R.string.user_info_not_found)
        getListener().onEditFailure(error, getKey(), getData())
        dismiss()
    }

    private fun onNameNotChanged() {
        Toast.makeText(context, R.string.user_name_not_changed, Toast.LENGTH_SHORT).show()
    }

    class Builder {
        private var message: String? = null
        private var user: User? = null
        private var key: String? = null
        private var data: Bundle? = null

        fun setMessage(message: String?): Builder = this.apply {
            this.message = message
        }

        fun setUser(user: User?): Builder = this.apply {
            this.user = user
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): EditDialog = newInstance(message, user, key, data)
    }
}
