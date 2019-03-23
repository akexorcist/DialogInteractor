package com.akexorcist.library.dialoginteractor.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.akexorcist.library.dialoginteractor.sample.dialog.DialogViewModel
import com.akexorcist.library.dialoginteractor.sample.dialog.alert.AlertDialog
import com.akexorcist.library.dialoginteractor.sample.dialog.alert.AlertListener
import com.akexorcist.library.dialoginteractor.sample.dialog.confirm.ConfirmDialog
import com.akexorcist.library.dialoginteractor.sample.dialog.confirm.ConfirmListener
import com.akexorcist.library.dialoginteractor.sample.dialog.edit.EditDialog
import com.akexorcist.library.dialoginteractor.sample.dialog.edit.EditListener
import com.akexorcist.library.dialoginteractor.sample.vo.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: DialogViewModel by lazy { ViewModelProviders.of(this).get(DialogViewModel::class.java) }

    companion object {
        const val KEY_SIMPLE_MESSAGE = "com.akexorcist.library.dialoginteractor.sample.key_simple_message"
        const val KEY_SHOW_USER_INFO = "com.akexorcist.library.dialoginteractor.sample.key_show_user_info"
        const val KEY_CONFIRM_DELETE_USER = "com.akexorcist.library.dialoginteractor.sample.key_confirm_delete_user"
        const val KEY_EDIT_USER_INFO = "com.akexorcist.library.dialoginteractor.sample.key_edit_user_info"

        const val EXTRA_USER_NAME = "com.akexorcist.library.dialoginteractor.sample.extra_user_name"
        const val EXTRA_USER_INFO = "com.akexorcist.library.dialoginteractor.sample.extra_user_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAlertSimpleMessage.setOnClickListener {
            AlertDialog.Builder()
                    .setMessage(getString(R.string.simple_message))
                    .setKey(KEY_SIMPLE_MESSAGE)
                    .build()
                    .show(supportFragmentManager)
        }

        buttonUserInfoAlert.setOnClickListener {
            val name = "Akexorcist"
            AlertDialog.Builder()
                    .setMessage(getString(R.string.hello_to_user, name))
                    .setKey(KEY_SHOW_USER_INFO)
                    .setData(Bundle().apply {
                        putString(EXTRA_USER_NAME, name)
                    })
                    .build()
                    .show(supportFragmentManager)
        }

        buttonConfirmDeleteUser.setOnClickListener {
            val user = User(
                    id = "0000001",
                    name = "Akexorcist",
                    age = 28
            )
            ConfirmDialog.Builder()
                    .setTitle(getString(R.string.delete_user_information_title, user.name))
                    .setMessage(getString(R.string.delete_user_information_message))
                    .setKey(KEY_CONFIRM_DELETE_USER)
                    .setData(Bundle().apply {
                        putParcelable(EXTRA_USER_INFO, user)
                    })
                    .build()
                    .show(supportFragmentManager)
        }

        buttonEditUserInfo.setOnClickListener {
            val user = User(
                    id = "0000001",
                    name = "Akexorcist",
                    age = 28
            )
            EditDialog.Builder()
                    .setMessage(getString(R.string.change_the_name))
                    .setUser(user)
                    .setKey(KEY_EDIT_USER_INFO)
                    .setData(Bundle().apply {
                        putParcelable(EditDialog.EXTRA_USER, user)
                    })
                    .build()
                    .show(supportFragmentManager)
        }

        viewModel.alert.observe(this, object : AlertListener {
            override fun onOkButtonClick(key: String?, data: Bundle?) {
                when (key) {
                    KEY_SIMPLE_MESSAGE -> completeSimpleMessageAlert()
                    KEY_SHOW_USER_INFO -> completeUserInfoAlert(data)
                }
            }
        })

        viewModel.confirm.observe(this, object : ConfirmListener {
            override fun onConfirmButtonClick(key: String?, data: Bundle?) {
                when (key) {
                    KEY_CONFIRM_DELETE_USER -> confirmDeleteUser(data)
                }
            }

            override fun onCancelButtonClick(key: String?, data: Bundle?) {
                when (key) {
                    KEY_CONFIRM_DELETE_USER -> cancelDeleteUser(data)
                }
            }
        })

        viewModel.edit.observe(this, object : EditListener {
            override fun onEditSuccess(user: User?, key: String?, data: Bundle?) {
                when (key) {
                    KEY_EDIT_USER_INFO -> editUserComplete(user, data)
                }
            }

            override fun onEditFailure(error: String?, key: String?, data: Bundle?) {
                when (key) {
                    KEY_EDIT_USER_INFO -> editUserFailure(error)
                }
            }
        })
    }

    private fun completeSimpleMessageAlert() {
        // Do something
    }

    private fun completeUserInfoAlert(data: Bundle?) {
        val name: String? = data?.getString(EXTRA_USER_NAME)
        name?.let {
            Toast.makeText(this, getString(R.string.alert_user_information_completed, name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDeleteUser(data: Bundle?) {
        val user: User? = data?.getParcelable(EXTRA_USER_INFO)
        user?.let {
            Toast.makeText(this, getString(R.string.user_information_deleted, it.name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelDeleteUser(data: Bundle?) {
        val user: User? = data?.getParcelable(EXTRA_USER_INFO)
        user?.let {
            Toast.makeText(this, getString(R.string.user_information_alive, it.name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun editUserComplete(newUser: User?, data: Bundle?) {
        val oldUser: User? = data?.getParcelable(EditDialog.EXTRA_USER)
        if (newUser != null && oldUser != null) {
            Toast.makeText(this, getString(R.string.user_name_changed, oldUser.name, newUser.name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun editUserFailure(error: String?) {
        error?.let {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}
