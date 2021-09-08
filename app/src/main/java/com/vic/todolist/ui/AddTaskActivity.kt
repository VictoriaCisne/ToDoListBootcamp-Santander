package com.vic.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.vic.todolist.R
import com.vic.todolist.databinding.ActivityAddTaskBinding
import com.vic.todolist.datasource.TaskDataSource
import com.vic.todolist.extensions.format
import com.vic.todolist.extensions.text
import com.vic.todolist.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private val mTaskDataSource by lazy { TaskDataSource(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            mTaskDataSource.findById(taskId).let {
                binding.tilTitle.text = it.title
                binding.tilDescription.text = it.description
                binding.tilDate.text = it.date
                binding.tilTime.text = it.time

                binding.toolbar.title = getString(R.string.update_task)
                binding.btnNewTask.text = getString(R.string.update_task)
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timezone = TimeZone.getDefault()
                val offset = timezone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, DATE_PICKER_TAG)
        }

        binding.tilTime.editText?.setOnClickListener {
            val timePicker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.tilTime.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, TIME_PICKER_TAG)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnNewTask.setOnClickListener {
            val task = Task().apply {
                title = binding.tilTitle.text
                description = binding.tilDescription.text
                date = binding.tilDate.text
                time = binding.tilTime.text
                id = intent.getIntExtra(TASK_ID, 0)
            }
            mTaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
    }
}