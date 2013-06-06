package me.particulate.limes.fddt;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoListOpenHelper extends SQLiteOpenHelper {

	// database version
	public static final int DATABASE_VERSION = 3;

	// database name
	public static final String DATABASE_NAME = "todos.db";

	// table name
	public static final String TABLE_TODOS = "todos";

	// column names
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_COMPLETED = "completed";

	private static final String DATABASE_CREATE = "create table " + TABLE_TODOS
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TITLE + " text not null, " + COLUMN_DESCRIPTION
			+ " text, " + COLUMN_COMPLETED + " integer not null)";

	public TodoListOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
		onCreate(db);
	}

	/**
	 * CRUD operations - Create, Read, Update and Delete
	 */

	/**
	 * add new todo
	 */
	void addTodo(ToDo todo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TITLE, todo.getTitle());
		values.put(COLUMN_COMPLETED, todo.isCompleted());

		db.insert(TABLE_TODOS, null, values);
		db.close();
	}

	/**
	 * update the completed status of an existing todo
	 */
	void updateTodoCompleted(ToDo todo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_COMPLETED, todo.isCompleted());

		db.update(TABLE_TODOS, values, COLUMN_ID + "=" + todo.getId(), null);
		db.close();
	}

	/**
	 * update an existing todo
	 */
	void updateTodo(ToDo todo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_TITLE, todo.getTitle());
		values.put(COLUMN_DESCRIPTION, todo.getDescription());
		values.put(COLUMN_COMPLETED, todo.isCompleted());

		db.update(TABLE_TODOS, values, COLUMN_ID + "=" + todo.getId(), null);
		db.close();
	}

	/**
	 * get single todo
	 */
	ToDo getToDo(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TODOS, new String[] { COLUMN_ID,
				COLUMN_TITLE, COLUMN_COMPLETED }, COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		cursor.close();

		ToDo todo = new ToDo(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), Boolean.parseBoolean(cursor.getString(2)));

		return todo;
	}

	/**
	 * get all todos
	 */
	public List<ToDo> getAllToDos() {
		List<ToDo> todoList = new ArrayList<ToDo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TODOS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ToDo todo = new ToDo();
			todo.setId(Integer.parseInt(cursor.getString(0)));
			todo.setTitle(cursor.getString(1));
			todo.setDescription(cursor.getString(2));
			todo.setCompleted(Integer.parseInt(cursor.getString(3)) > 0 ? true
					: false);
			todoList.add(todo);
			cursor.moveToNext();
		}

		cursor.close();
		return todoList;
	}

	/**
	 * delete todo
	 */
	public void deleteTodo(ToDo todo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TODOS, COLUMN_ID + "=?",
				new String[] { String.valueOf(todo.getId()) });
		db.close();
	}

	/**
	 * get todos count
	 */
	public int getTodosCount() {
		String countQuery = "SELECT * FROM " + TABLE_TODOS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}
}
