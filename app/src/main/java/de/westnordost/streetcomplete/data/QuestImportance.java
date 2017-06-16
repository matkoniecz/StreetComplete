package de.westnordost.streetcomplete.data;

public class QuestImportance
{
	/* useful to push currently developed quest to top */
	public static final int DEVELOPED_NOW = 0;

	/** Notes always have the highest importance, because they shall occlude "normal" quests
	 *  (as a note at the exact same position inherently references a problem in the map) */
	public static final int NOTE = 1;
	/* Nowadays, oftentimes a note is created to merely denote that i.e. "housenumber is missing",
	*  "name is missing" etc. However, with this software hopefully gaining some popularity, this is
	*  not necessary or meaningful anymore because those things are created automatically then */

	/* importnat for me */
	public static final int BICYCLE_PRIME = 3;
	public static final int BICYCLE = 4;

	/** Solving this quest will fix data that is shown as invalid or erroneous in QA tools */
	public static final int ERROR = 5;

	/** Solving this quest will fix data that is shown as warnings in QA tools */
	public static final int WARNING = 15;

	/** Solving this quest will complement important/very useful data that is used by many data
	 *  consumers */
	public static final int MAJOR = 20;

	/** Solving this quest will complement useful data that is used by some data consumers */
	public static final int MINOR = 50;

	/** Solving this quest will complement data that is used for a very specific use case of the map
	 * */
	public static final int EXTRA = 100;

	/** Solving this quest will complement data that is defined in the wiki but has no concrete uses
	 *  (yet). It is collected for the sake of mapping it in case this might make sense later */
	public static final int INSIGNIFICANT = 200;

	private QuestImportance()
	{
		// do not instantiate, just a constants class. Not using an enum here because it should
		// be possible to use other values than the given ones here.
	}
}
