package me.particulate.limes.fddt;

import android.os.Parcel;
import android.os.Parcelable;

public class ToDo implements Parcelable {

	private int id;
	private String title;
	private String description;
	private boolean completed;

	public ToDo() {

	}

	public ToDo(int id, String title, boolean completed) {
		this.setId(id);
		this.setTitle(title);
		this.setCompleted(completed);
	}

	public ToDo(String title, boolean completed) {
		this.setTitle(title);
		this.setCompleted(completed);
	}

	public ToDo(String title) {
		this(title, false);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Parcelable implementation
	 */

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeByte((byte) (completed ? 1 : 0));
	}

	public static final Parcelable.Creator<ToDo> CREATOR = new Parcelable.Creator<ToDo>() {
		public ToDo createFromParcel(Parcel in) {
			return new ToDo(in);
		}

		public ToDo[] newArray(int size) {
			return new ToDo[size];
		}
	};

	private ToDo(Parcel in) {
		id = in.readInt();
		title = in.readString();
		description = in.readString();
		completed = in.readByte() == 1;
	}
}
