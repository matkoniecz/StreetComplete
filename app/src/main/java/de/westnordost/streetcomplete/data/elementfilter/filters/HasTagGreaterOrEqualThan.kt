package de.westnordost.streetcomplete.data.elementfilter.filters

/** key >= value */
class HasTagGreaterOrEqualThan(key: String, value: Float): CompareTagValue(key, value) {
    override val operator = ">="
    override fun compareTo(tagValue: Float) = tagValue >= value
}