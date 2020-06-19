package com.hsikkkk.todolist

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun inviteUser(group_id: String) {
    val db = Firebase.firestore
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
    var uid =""
    db.collection("User")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener {documents->
            uid = documents.first().id
        }

    return uid
}