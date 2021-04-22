package pl.duch.dybuk87.core.date

import android.content.Context
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Months
import org.joda.time.Years
import org.joda.time.format.DateTimeFormat
import pl.duch.dybuk87.core.R

fun DateTime?.getDateFriendly(context: Context) =
    if (this == null) {
        ""
    } else {
        val now = DateTime.now()
        val duration = Duration(this, now)

        when {
            now.minusMinutes(1).isBefore(this) ->
                context.getString(R.string.now)

            now.minusDays(1).isBefore(this) ->
                context.getString(R.string.today)

            now.minusDays(2).isBefore(this) ->
                context.getString(R.string.yestarday)

            now.year() == year() -> {
                val formatter = DateTimeFormat.forPattern("dd MMMM")
                toString(formatter)
            }

            else -> {
                val formatter = DateTimeFormat.forPattern("dd MMMM yyyy")
                toString(formatter)
            }
        }
    }

fun DateTime?.getDateTimeAgo(context: Context) =
    if (this == null) {
        ""
    } else {
        val now = DateTime.now()
        val duration = Duration(this, now)

        when {
            now.minusMinutes(1).isBefore(this) ->
                context.getString(R.string.now)

            now.minusHours(1).isBefore(this) ->
                context.resources.getQuantityString(R.plurals.date_time_ago_minutes, duration.standardMinutes.toInt(), duration.standardMinutes)

            now.minusDays(1).isBefore(this) ->
                context.resources.getQuantityString(R.plurals.date_time_ago_hours, duration.standardHours.toInt(), duration.standardHours)

            now.minusMonths(1).isBefore(this) ->
                context.resources.getQuantityString(R.plurals.date_time_ago_days, duration.standardDays.toInt(), duration.standardDays)

            now.minusYears(1).isBefore(this) -> {
                val months = Months.monthsBetween(toLocalDate(), now.toLocalDate()).months
                context.resources.getQuantityString(R.plurals.date_time_ago_months, months, months)
            }

            else -> {
                val years = Years.yearsBetween(toLocalDate(), now.toLocalDate()).years
                context.resources.getQuantityString(R.plurals.date_time_ago_year, years, years)
            }
        }
    }