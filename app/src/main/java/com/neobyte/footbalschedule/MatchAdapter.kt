package com.neobyte.footbalschedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.neobyte.footbalschedule.MatchAdapter.TeamViewHolder
import com.neobyte.footbalschedule.models.Event
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MatchAdapter(private val events: List<Event?>,
                   private val listener: (pos: Int) -> Unit) :
    RecyclerView.Adapter<TeamViewHolder>() {

  override fun onCreateViewHolder(p0: ViewGroup,
                                  p1: Int): TeamViewHolder {
    return TeamViewHolder(
        LayoutInflater.from(p0.context).inflate(R.layout.item_match, p0, false)
    )
  }

  override fun getItemCount() = events.size

  override fun onBindViewHolder(p0: TeamViewHolder,
                                p1: Int) {
    p0.bindItem(events[p1], p1)
  }

  inner class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val matchDate: TextView = view.findViewById(R.id.tv_date)
    private val matchTime: TextView = view.findViewById(R.id.tvTime)
    private val teamName1: TextView = view.findViewById(R.id.tv_team_1)
    private val teamScore1: TextView = view.findViewById(R.id.tv_skor_team_1)
    private val teamName2: TextView = view.findViewById(R.id.tv_team_2)
    private val teamScore2: TextView = view.findViewById(R.id.tv_skor_team_2)
    private val ivAlarm: ImageView = view.findViewById(R.id.ivAlarm)

    fun bindItem(event: Event?,
                 pos: Int) {
      event?.let { _ ->
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var showMyReminder = false

        try {
          val myDate = sdf.parse(event.dateEvent)
          val cal1 = Calendar.getInstance()
          val cal2 = Calendar.getInstance().apply {
            time = myDate
            timeZone = TimeZone.getTimeZone("UTC")
          }
          showMyReminder = cal2.after(cal1)
          sdf.applyPattern("EEE, d MMM yyyy")
          matchDate.text = sdf.format(myDate)
        } catch (e: ParseException) {
          e.printStackTrace()
        }

        event.strTime?.let {
          try {
            val utcFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val time = it.substringBefore("+")

            val myTimeZone = Calendar.getInstance().timeZone
            val myZoneFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            val date = utcFormat.parse(time)
            myZoneFormat.timeZone = TimeZone.getTimeZone(myTimeZone.id)

            matchTime.text = myZoneFormat.format(date)
          } catch (e: Exception) {
            e.printStackTrace()
          }
        }

        if (showMyReminder) {
          ivAlarm.visibility = View.VISIBLE
        } else {
          ivAlarm.visibility = View.GONE
        }

        teamName1.text = event.strHomeTeam
        teamScore1.text = event.intHomeScore

        teamName2.text = event.strAwayTeam
        teamScore2.text = event.intAwayScore

        itemView.setOnClickListener { listener(pos) }
      }
    }
  }
}