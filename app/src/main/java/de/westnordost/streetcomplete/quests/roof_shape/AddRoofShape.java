package de.westnordost.streetcomplete.quests.roof_shape;

import android.os.Bundle;

import java.util.ArrayList;

import javax.inject.Inject;

import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType;
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder;
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataDao;
import de.westnordost.streetcomplete.quests.AbstractQuestAnswerFragment;

public class AddRoofShape extends SimpleOverpassQuestType
{
	@Inject public AddRoofShape(OverpassMapDataDao overpassServer)
	{
		super(overpassServer);
	}

	@Override
	protected String getTagFilters()
	{
		return " ways, relations with " +
				" building ~ house|residential|apartments|detached|terrace|farm|hotel|dormitory|houseboat|" +
				"school|civic|college|university|public|hospital|kindergarten|transportation|train_station|"+
				"retail|commercial|warehouse|industrial|manufacture" +
				" and !roof:shape";
	}

	public AbstractQuestAnswerFragment createForm()
	{
		return new AddRoofShapeForm();
	}

	public void applyAnswerTo(Bundle answer, StringMapChangesBuilder changes)
	{
		ArrayList<String> values = answer.getStringArrayList(AddRoofShapeForm.OSM_VALUES);
		if(values != null  && values.size() == 1)
		{
			changes.add("roof:shape", values.get(0));
		}
	}

	@Override public String getCommitMessage()
	{
		return "Add roof shapes";
	}

	@Override public String getIconName() {	return "roof_shape"; }
}
