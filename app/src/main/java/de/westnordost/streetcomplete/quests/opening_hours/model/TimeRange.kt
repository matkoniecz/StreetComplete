package de.westnordost.streetcomplete.quests.opening_hours.model

import java.text.DateFormat
import java.util.*

/** A time range from [start,end). */
class TimeRange(minutesStart: Int, minutesEnd: Int, val isOpenEnded: Boolean = false)
    : CircularSection(minutesStart, minutesEnd) {

    override fun intersects(other: CircularSection): Boolean {
        if (other !is TimeRange) return false
        if (isOpenEnded && other.start >= start) return true
        if (other.isOpenEnded && start >= other.start) return true
        return loops && other.loops ||
            if (loops || other.loops) other.end > start || other.start < end
            else                      other.end > start && other.start < end
    }

    fun toStringUsing(locale: Locale, range: String): String {
        val sb = StringBuilder()
        sb.append(timeOfDayToString(locale, start))
        val displayEnd = if(end != 0) end else 60 * 24
        if (start != end || !isOpenEnded) {
            sb.append(range)
            sb.append(timeOfDayToString(locale, displayEnd))
        }
        if (isOpenEnded) sb.append("+")
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TimeRange) return false
        return other.isOpenEnded == isOpenEnded && super.equals(other)
    }

    override fun hashCode() = super.hashCode() * 2 + if (isOpenEnded) 1 else 0

    override fun toString() = toStringUsing(Locale.GERMANY, "-")

    private fun timeOfDayToString(locale: Locale, minutes: Int): String {
        val cal = GregorianCalendar()
        cal.set(Calendar.HOUR_OF_DAY, minutes / 60)
        cal.set(Calendar.MINUTE, minutes % 60)
        return DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(cal.time)
    }
}
