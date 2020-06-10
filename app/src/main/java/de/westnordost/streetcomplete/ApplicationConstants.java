package de.westnordost.streetcomplete;

public class ApplicationConstants
{
	public final static String
		NAME = "zażółć",
		USER_AGENT = "Zazolc" + " " + BuildConfig.VERSION_NAME, // crashes: JNI DETECTED ERROR IN APPLICATION: JNI NewString called with pending exception java.lang.IllegalArgumentException: Unexpected char 0x17c at 2 in User-Agent value: zażółć 17.3 / okhttp/3.12.0
		QUESTTYPE_TAG_KEY = NAME + ":quest_type";

	public final static double
		MAX_DOWNLOADABLE_AREA_IN_SQKM = 20,
		MIN_DOWNLOADABLE_AREA_IN_SQKM = 1;

	public final static double MIN_DOWNLOADABLE_RADIUS_IN_METERS = 600;

	public final static String DATABASE_NAME = "streetcomplete.db";

	public final static int QUEST_TILE_ZOOM = 14;

	public final static int NOTE_MIN_ZOOM = 15;

	/** How many quests to download when pressing manually on "download quests" */
	public final static int MANUAL_DOWNLOAD_QUEST_TYPE_COUNT = 10;

	/** a "best before" duration for quests. Quests will not be downloaded again for any tile
	 *  before the time expired */
	public static final long REFRESH_QUESTS_AFTER = 7L*24*60*60*1000; // 1 week in ms
	/** the duration after which quests will be deleted from the database if unsolved */
	public static final long DELETE_UNSOLVED_QUESTS_AFTER = 1L*30*24*60*60*1000; // 1 months in ms

	/** the max age of the undo history - one cannot undo changes older than X */
	public static final long MAX_QUEST_UNDO_HISTORY_AGE = 24*60*60*1000; // 1 day in ms

	public static final String AVATARS_CACHE_DIRECTORY = "osm_user_avatars";

	public static final String SC_PHOTO_SERVICE_URL = "https://westnordost.de/streetcomplete/photo-upload/"; // must have trailing /

	public static final int ATTACH_PHOTO_QUALITY = 80;
	public static final int ATTACH_PHOTO_MAXWIDTH = 1280; // WXGA
	public static final int ATTACH_PHOTO_MAXHEIGHT = 1280; // WXGA

	public static final String NOTIFICATIONS_CHANNEL_DOWNLOAD = "downloading";
}
