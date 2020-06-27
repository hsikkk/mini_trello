package com.hsikkkk.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainViewModel(val group_id: String) : ViewModel() {
    val db = Firebase.firestore
    val todoLiveData = MutableLiveData<List<DocumentSnapshot>>()

    init {
        fetchData()
    }

    fun fetchData() {
        db.collection("Group")
            .document(group_id)
            .collection("todos")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    todoLiveData.value = value.documents
                }
            }
    }

    fun addTodo(todo: Todo) {
        db.collection("Group")
            .document(group_id)
            .collection("todos")
            .add(todo)
    }

    fun delTodo(todo: DocumentSnapshot) {
        db.collection("Group")
            .document(group_id)
            .collection("todos")
            .document(todo.id).delete()
    }

    fun toggelTodo(todo: DocumentSnapshot) {
        db.collection("Group")
            .document(group_id)
            .collection("todos")
            .document(todo.id)
            .update("done", !(todo.getBoolean("done") ?: false))
    }

    fun setTitle(actionBar: androidx.appcompat.app.ActionBar) {
        db.collection("Group")
            .document(group_id).get()
            .addOnSuccessListener {
            actionBar.setTitle(it.getString("name"))
        }
    }
}