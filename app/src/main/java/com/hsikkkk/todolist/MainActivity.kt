package com.hsikkkk.todolist

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.hsikkkk.todolist.databinding.ActivityMainBinding
import com.hsikkkk.todolist.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val group_id = intent.getStringExtra("group_id")

        viewModel = MainViewModel(group_id ?: "")

        val actionBar: ActionBar? = supportActionBar
        viewModel.setTitle(actionBar!!)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = TodoAdapter(
                emptyList(),
                onClickDeleteIcon = {
                    viewModel.delTodo(it)
                },
                onToggleItem = {
                    viewModel.toggelTodo(it)
                }
            )
        }

        binding.addButton.setOnClickListener {
            val todo = Todo(binding.editText.text.toString())
            viewModel.addTodo(todo)
        }

        viewModel.todoLiveData.observe(this, Observer {
            (binding.recyclerView.adapter as TodoAdapter).setData(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.todo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val group_id = intent.getStringExtra("group_id")

        return when (item.itemId) {
            R.id.action_invite_user -> {
                AddUserDialogFragment(group_id).show(supportFragmentManager, null)
                true
            }
            R.id.action_exit_group -> {
                ExitGroupDialogFragment(group_id, this@MainActivity)
                    .show(supportFragmentManager, null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

class TodoAdapter(
    private var myDataset: List<DocumentSnapshot>,
    val onClickDeleteIcon: (todo: DocumentSnapshot) -> Unit,
    val onToggleItem: (todo: DocumentSnapshot) -> Unit
) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(ItemTodoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = myDataset[position]
        holder.binding.todoText.text = todo.getString("text") ?: ""

        if (todo.getBoolean("done") == true) {
            holder.binding.todoText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        } else {
            holder.binding.todoText.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }

        holder.binding.deleteImageView.setOnClickListener {
            onClickDeleteIcon.invoke(todo)
        }

        holder.binding.root.setOnClickListener {
            onToggleItem.invoke(todo)
        }
    }

    override fun getItemCount(): Int = myDataset.size

    fun setData(newData: List<DocumentSnapshot>) {
        myDataset = newData
        notifyDataSetChanged()
    }
}
