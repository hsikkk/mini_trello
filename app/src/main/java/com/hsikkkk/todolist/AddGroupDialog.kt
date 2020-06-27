package com.hsikkkk.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hsikkkk.todolist.databinding.DialogAddGroupBinding

class AddGroupDialogFragment(val myContext: Context) : DialogFragment() {
    private lateinit var binding: DialogAddGroupBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = layoutInflater
            binding = DialogAddGroupBinding.inflate(inflater)

            builder.setView(binding.root)
                .setMessage(R.string.dialog_add_group)
                .setPositiveButton(R.string.fire,
                    DialogInterface.OnClickListener { dialog, id ->
                        addGroup(binding.inputGroupName.text.toString(), myContext)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}