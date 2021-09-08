package com.vic.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.vic.todolist.databinding.ActivityMainBinding
import com.vic.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }
    private val mTaskDataSource by lazy { TaskDataSource(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fabAddTask.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource(baseContext).deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == RESULT_OK) updateList()
    }

    private fun updateList() {
        if (mTaskDataSource.getList().isEmpty()) {
            binding.rvTasks.visibility = GONE
            binding.includedLayout.layoutEmptyState.visibility = VISIBLE
        } else {
            binding.includedLayout.layoutEmptyState.visibility = GONE
            adapter.submitList(mTaskDataSource.getList())
            binding.rvTasks.visibility = VISIBLE
        }
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}