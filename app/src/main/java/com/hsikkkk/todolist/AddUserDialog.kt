package com.hsikkkk.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hsikkkk.todolist.databinding.DialogAddUserBinding

class AddUserDialogFragment(val group_id: String, val group_name: String) : DialogFragment() {
    private lateinit var binding: DialogAddUserBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = layoutInflater
            binding = DialogAddUserBinding.inflate(inflater)

            builder.setView(binding.root)
                .setMessage(R.string.dialog_add_user)
                .setPositiveButton(R.string.fire,
                    DialogInterface.OnClickListener { dialog, id ->
                        addUser(group_id, group_name, binding.inputEmail.text.toString())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}