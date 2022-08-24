package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.DayName
import com.dicoding.courseschedule.util.TimePickerFragment

class AddCourseActivity : AppCompatActivity() {

    enum class Button {
        ButtonStart, ButtonEnd
    }

    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var viewModel: AddCourseViewModel
    private var lastButton = Button.ButtonStart
    private var startTime = ""
    private var endTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartTime.setOnClickListener {
            timePicker(Button.ButtonStart)
        }

        binding.btnEndTime.setOnClickListener {
            timePicker(Button.ButtonEnd)
        }

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)
    }

    private fun timePicker(_button: Button) {
        lastButton = _button
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "timePicker")

        dialogFragment.setListener(object : TimePickerFragment.DialogTimeListener {
            override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
                if (lastButton == Button.ButtonStart) {
                    startTime = String.format("%02d:%02d",hour, minute)
                    binding.tvStartTime.text = startTime
                } else {
                    endTime = String.format("%02d:%02d",hour, minute)
                    binding.tvEndTime.text = endTime
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_insert -> {
                viewModel.insertCourse(binding.edtCourseName.text.toString(),
                    DayName.getByName(binding.day.selectedItem.toString()),
                    startTime, endTime, binding.edtLecture.text.toString(),
                    binding.edtNote.text.toString())
                this.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}