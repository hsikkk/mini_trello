package com.hsikkkk.todolist

import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun addUser(group_id: String, email: String) {
    val db = Firebase.firestore

    db.collection("User")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val uid = documents.first().id

                db.collection("Group")
                    .document(group_id)
                    .collection("users")
                    .document(uid).set(
                        hashMapOf("exist" to true)
                    )

                db.collection("User")
                    .document(uid)
                    .collection("group")
                    .document(group_id).set(
                        hashMapOf("exist" to true)
                    )
            }
        }
}

fun addGroup(group_name: String, context: Context) {
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser

    db.collection("Group")
        .add(hashMapOf("name" to group_name))
        .addOnSuccessListener {
            db.collection("Group")
                .document(it.id)
                .collection("users")
                .document(user!!.uid).set(
                    hashMapOf("exist" to true)
                )

            db.collection("User")
                .document(user!!.uid)
                .collection("group")
                .document(it.id).set(
                    hashMapOf("exist" to true)
                )

            val intent = Intent(context, MainActivity::class.java)
            intent.apply {
                putExtra("group_id", it.id)
            }
            context.startActivity(intent)
        }
}


fun exitGroup(group_id: String) {
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser

    db.collection("User")
        .document(user!!.uid)
        .collection("group")
        .document(group_id).delete()

    db.collection("Group")
        .document(group_id)
        .collection("users")
        .document(user!!.uid).delete()

}


fun getGroupName(id: String, textView: TextView) {
    val db = Firebase.firestore

    db.collection("Group")
        .document(id).get()
        .addOnSuccessListener {
            textView.text = it.getString("name")
        }
}