package com.hsikkkk.todolist

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.hsikkkk.todolist.databinding.ActivityGroupBinding
import com.hsikkkk.todolist.databinding.ItemGroupBinding

class GroupActivity : AppCompatActivity() {
    val RC_SIGN_IN = 1000
    private lateinit var binding: ActivityGroupBinding
    private val viewModel: GroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (FirebaseAuth.getInstance().currentUser == null) {
            login()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GroupActivity)
            adapter = GroupAdapter(
                emptyList(),
                this@GroupActivity
            )
        }

        binding.buttonAddGroup.setOnClickListener {
            AddGroupDialogFragment(this@GroupActivity)
                .show(supportFragmentManager, null)
        }

        viewModel.groupLiveData.observe(this, Observer {
            (binding.recyclerView.adapter as GroupAdapter).setData(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == Activity.RESULT_OK) {
                viewModel.fetchData()
            } else {
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_log_out -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun login() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                login()
            }
    }
}

class GroupAdapter(
    private var myDataset: List<DocumentSnapshot>,
    val context: Context
) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    class GroupViewHolder(val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(ItemGroupBinding.bind(view))
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = myDataset[position]

        getGroupName(group.id, holder.binding.groupText)

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, MainActivity::class.java)
            intent.apply{
                putExtra("group_id", group.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = myDataset.size

    fun setData(newData: List<DocumentSnapshot>) {
        myDataset = newData
        notifyDataSetChanged()
    }
}
