package com.raywenderlich.listmaker

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var todoListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //reference the lists_recyclerview from content_main.xml
        todoListRecyclerView = findViewById(R.id.lists_recyclerview)
        //tell recyclerView which layout to use for its items
        todoListRecyclerView.layoutManager = LinearLayoutManager(this)
        //assign custom adapter to recyclerView
        todoListRecyclerView.adapter = TodoListAdapter()

        fab.setOnClickListener {
            showCreateTodoListDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // create a standard dialog to ask user to name new todolist
    private fun showCreateTodoListDialog() {
        val dialogTitle = getString(R.string.name_of_list_prompt)
        val positiveButtonTitle = getString(R.string.create_list)
        val myDialog = AlertDialog.Builder(this)
        val todoTitleEditText = EditText(this)
        todoTitleEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        myDialog.setTitle(dialogTitle)
        myDialog.setView(todoTitleEditText)
        myDialog.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            // 1. access the adapter in the setPositiveButton closure
            val adapter = todoListRecyclerView.adapter as TodoListAdapter
            // 2. call addNewItem and pass the EditText text value as a string
            adapter.addNewItem(todoTitleEditText.text.toString())

            dialog.dismiss()
        }
        myDialog.create().show()
    }
}