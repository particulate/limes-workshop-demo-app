package me.particulate.limes.fddt;

import java.util.List;

import me.particulate.limes.fddt.ToDoDialogFragment.ToDoDialogListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ToDoDialogListener {

	private EditText addTodoEditText;
	private ListView todoListView;

	ArrayAdapter<ToDo> todoArrayAdapter;

	TodoListOpenHelper dbHelper;

	ToDo selectedToDo = null;
	
	List<ToDo> todos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get the views
		addTodoEditText = (EditText) findViewById(R.id.addTodoEditText);
		todoListView = (ListView) findViewById(R.id.todoListView);

		// create the sqlite database helper
		dbHelper = new TodoListOpenHelper(this);

		// load all todos from database
		todos = dbHelper.getAllToDos();

		// create an array adapter with a multiple choice layout and link with
		// todos list
		todoArrayAdapter = new ArrayAdapter<ToDo>(this,
				android.R.layout.simple_list_item_multiple_choice, todos);

		// bind the adapter to the todo list view
		todoListView.setAdapter(todoArrayAdapter);

		// activate multiple choice mode
		todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		// for each todo item set the checked attribute
		for (int i = 0; i < todos.size(); i++) {
			todoListView.setItemChecked(todoArrayAdapter.getPosition(todos
					.get(i)), todos.get(i).isCompleted());
		}

		// set long click listener to remove a todo item
		todoListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				selectedToDo = (ToDo) parent.getItemAtPosition(position);

				new ToDoDialogFragment().show(getSupportFragmentManager(),
						"TodoDialogFragment");
				return false;
			}
		});

		// set click listener to check a todo item
		todoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SparseBooleanArray checked = todoListView
						.getCheckedItemPositions();
				ToDo todo = (ToDo) parent.getItemAtPosition(position);
				todo.setCompleted(checked.get(position));
				dbHelper.updateTodoCompleted(todo);
			}
		});
	}

	@Override
	protected void onRestart() {
		super.onResume();
		Toast.makeText(this, "Go on!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				ToDo todo = (ToDo) intent.getParcelableExtra("result");
				dbHelper.updateTodo(todo);
				todoArrayAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * Click Listener for the 'add'-button. Creates and saves the todo in the
	 * database.
	 */
	public void addTodo(View view) {
		if (addTodoEditText.getText().toString().length() != 0) {
			ToDo todo = new ToDo(addTodoEditText.getText().toString());
			dbHelper.addTodo(todo);
			todoArrayAdapter.add(todo);
			todoArrayAdapter.notifyDataSetChanged();
			addTodoEditText.setText("");
		}
	}

	@Override
	public void onDialogDeleteClick() {
		dbHelper.deleteTodo(selectedToDo);
		todoArrayAdapter.remove(selectedToDo);
		todoArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDialogEditClick() {
		Intent intent = new Intent(this, EditToDoActivity.class);
		intent.putExtra("todo_item", selectedToDo);
		startActivityForResult(intent, 1);
	}
}
