package com.hsikkkk.todolist

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun inviteUser(group_id: String,group_name: String, email: String) {
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
                        hashMapOf("email" to email))

                db.collection("User")
                    .document(uid)
                    .collection("group")
                    .document(group_id).set(
                        hashMapOf("name" to group_name)
                    )
            }
        }

}

fun exitGroup(group_id: String) {
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser
    db.collection("Group")
        .document(group_id)
        .collection("users")
        .document(user!!.uid).delete()

    db.collection("User")
        .document(user!!.uid)
        .collection("group")
        .document(group_id).delete()
}

fun getUserId(email: String): String {
    val db = Firebase.firestore
    Log.d("abc", "email: " + email)
    var uid = ""

    return uid
}