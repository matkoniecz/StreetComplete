package de.westnordost.osmagent.data.osmnotes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.Date;

import javax.inject.Inject;

import de.westnordost.osmagent.data.AQuestDao;
import de.westnordost.osmagent.data.QuestStatus;
import de.westnordost.osmagent.util.Serializer;
import de.westnordost.osmapi.notes.Note;

public class OsmNoteQuestDao extends AQuestDao<OsmNoteQuest>
{
	private final Serializer serializer;
	private final SQLiteStatement add, replace;

	@Inject public OsmNoteQuestDao(SQLiteOpenHelper dbHelper, Serializer serializer)
	{
		super(dbHelper);
		this.serializer = serializer;

		String sql = OsmNoteQuestTable.NAME + " ("+
				OsmNoteQuestTable.Columns.QUEST_ID+","+
				OsmNoteQuestTable.Columns.NOTE_ID+","+
				OsmNoteQuestTable.Columns.QUEST_STATUS+","+
				OsmNoteQuestTable.Columns.COMMENT+","+
				OsmNoteQuestTable.Columns.LAST_UPDATE+
				") values (?,?,?,?,?);";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		add = db.compileStatement("INSERT OR IGNORE INTO " + sql);
		replace = db.compileStatement("INSERT OR REPLACE INTO " +sql);
	}

	@Override protected String getTableName()
	{
		return OsmNoteQuestTable.NAME;
	}

	@Override protected String getMergedViewName()
	{
		return OsmNoteQuestTable.NAME_MERGED_VIEW;
	}

	@Override protected String getIdColumnName()
	{
		return OsmNoteQuestTable.Columns.QUEST_ID;
	}

	@Override protected String getLatitudeColumnName()
	{
		return NoteTable.Columns.LATITUDE;
	}

	@Override protected String getLongitudeColumnName()
	{
		return NoteTable.Columns.LONGITUDE;
	}

	@Override protected String getQuestStatusColumnName()
	{
		return OsmNoteQuestTable.Columns.QUEST_STATUS;
	}

	@Override protected long executeInsert(OsmNoteQuest quest, boolean replace)
	{
		SQLiteStatement stmt = replace ? this.replace : this.add;

		if(quest.getId() != null)
		{
			stmt.bindLong(1, quest.getId());
		}
		else
		{
			stmt.bindNull(1);
		}
		stmt.bindLong(2, quest.getNote().id);
		stmt.bindString(3, quest.getStatus().name());
		if(quest.getComment() != null)
		{
			stmt.bindString(4, quest.getComment());
		}
		else
		{
			stmt.bindNull(4);
		}

		stmt.bindLong(5, quest.getLastUpdate().getTime());

		long result = stmt.executeInsert();
		stmt.clearBindings();
		return result;
	}

	@Override protected ContentValues createNonFinalContentValuesFrom(OsmNoteQuest quest)
	{
		ContentValues values = new ContentValues();
		values.put(OsmNoteQuestTable.Columns.QUEST_STATUS, quest.getStatus().name());
		values.put(OsmNoteQuestTable.Columns.LAST_UPDATE, quest.getLastUpdate().getTime());

		if(quest.getComment() != null)
		{
			values.put(OsmNoteQuestTable.Columns.COMMENT, quest.getComment());
		}

		return values;
	}

	@Override protected ContentValues createFinalContentValuesFrom(OsmNoteQuest quest)
	{
		ContentValues values = new ContentValues();
		if(quest.getNote() != null)
		{
			values.put(OsmNoteQuestTable.Columns.NOTE_ID, quest.getNote().id);
		}
		return values;
	}

	@Override protected OsmNoteQuest createObjectFrom(Cursor cursor)
	{
		int colQuestId = cursor.getColumnIndexOrThrow(OsmNoteQuestTable.Columns.QUEST_ID),
			colNoteId = cursor.getColumnIndexOrThrow(OsmNoteQuestTable.Columns.NOTE_ID),
			colQuestStatus = cursor.getColumnIndexOrThrow(OsmNoteQuestTable.Columns.QUEST_STATUS),
			colComment = cursor.getColumnIndexOrThrow(OsmNoteQuestTable.Columns.COMMENT),
			colLastUpdate = cursor.getColumnIndexOrThrow(OsmNoteQuestTable.Columns.LAST_UPDATE);

		long questId = cursor.getLong(colQuestId);

		String comment = null;
		if(!cursor.isNull(colComment))
		{
			comment = cursor.getString(colComment);
		}
		QuestStatus status = QuestStatus.valueOf(cursor.getString(colQuestStatus));

		Date lastUpdate = new Date(cursor.getLong(colLastUpdate));

		Note note = null;
		if(!cursor.isNull(colNoteId))
		{
			note = NoteDao.createObjectFrom(serializer, cursor);
		}

		return new OsmNoteQuest(questId, note, status, comment, lastUpdate);
	}
}
