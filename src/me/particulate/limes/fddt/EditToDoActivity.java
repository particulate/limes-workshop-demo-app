package me.particulate.limes.fddt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditToDoActivity extends Activity {

	EditText titleEditText;
	EditText descriptionEditText;
	Button saveBtn;
	Button cancelBtn;

	ToDo todo;

	TodoListOpenHelper dbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_to_do);

		titleEditText = (EditText) findViewById(R.id.titleEditText);
		descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);

		Intent intent = getIntent();
		todo = (ToDo) intent.getParcelableExtra("todo_item");

		titleEditText.setText(todo.getTitle());
		descriptionEditText.setText(todo.getDescription());

		dbHelper = new TodoListOpenHelper(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_to_do, menu);
		return true;
	}

	public void save(View view) {
		todo.setDescription(descriptionEditText.getText().toString());
		todo.setTitle(titleEditText.getText().toString());
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", todo);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void cancel(View view) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		this.finish();
	}
}
