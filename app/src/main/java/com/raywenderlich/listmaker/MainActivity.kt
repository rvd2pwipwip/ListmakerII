package com.raywenderlich.listmaker

import android.content.Intent
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

    companion object {
        const val INTENT_LIST_KEY = "list"
    }
    //access recycler view
    private lateinit var todoListRecyclerView: RecyclerView
    //access data manager with context
    private val listDataManager = ListDataManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //get lists from list data manager
        val lists = listDataManager.readLists()

        //reference the lists_recyclerview from content_main.xml
        todoListRecyclerView = findViewById(R.id.lists_recyclerview)
        //tell recyclerView which layout to use for its items
        todoListRecyclerView.layoutManager = LinearLayoutManager(this)
        //assign adapter to recyclerView and pass in the lists from listDataManager
        todoListRecyclerView.adapter = TodoListAdapter(lists)

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
            // 2. create empty task list and pass editText value as title
            val list = TaskList(todoTitleEditText.text.toString())
            // 3. save the list
            listDataManager.saveList(list)
            //update recycler view with new list of task lists
            adapter.addList(list)
            dialog.dismiss()
        }
        myDialog.create().show()
    }

    //open the DetailActivity with a passed list
    private fun showTaskListItems(list: TaskList) {
        //access the activity with an intent that passes the context and the activity class
        val taskListItem = Intent(this, DetailActivity::class.java)
        //add an extra that passes an intent list key
        taskListItem.putExtra(INTENT_LIST_KEY, list) //must implement Parcel in TaskList
        //start the activity
        startActivity(taskListItem)
    }
}