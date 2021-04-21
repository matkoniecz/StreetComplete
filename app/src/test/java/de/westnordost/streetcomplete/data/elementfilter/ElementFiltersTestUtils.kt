package de.westnordost.streetcomplete.data.elementfilter

import de.westnordost.streetcomplete.testutils.node
import de.westnordost.streetcomplete.data.elementfilter.filters.ElementFilter

import java.text.SimpleDateFormat
import java.util.*

/** Returns the date x days in the past */
fun dateDaysAgo(daysAgo: Float): Date {
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.SECOND, -(daysAgo * 24 * 60 * 60).toInt())
    return cal.time
}

fun ElementFilter.matches(tags: Map<String,String>, date: Date? = null): Boolean =
    matches(node(tags = tags, date = date))

val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
