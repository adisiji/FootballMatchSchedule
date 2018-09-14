package com.neobyte.footbalschedule.teams

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.teams.TeamsAdapter.TeamsViewHolder

class TeamsAdapter(private val list: List<Team?>,
                   private val listener: (team: Team, view: View) -> Unit)
  : RecyclerView.Adapter<TeamsViewHolder>() {

  override fun onCreateViewHolder(p0: ViewGroup,
                                  p1: Int): TeamsViewHolder {
    return TeamsViewHolder(
        LayoutInflater.from(p0.context).inflate(R.layout.item_team, p0, false)
    )
  }

  override fun getItemCount(): Int = list.size

  override fun onBindViewHolder(p0: TeamsViewHolder,
                                p1: Int) {
    p0.bind(list[p1])
  }

  inner class TeamsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val ivTeam: ImageView = view.findViewById(R.id.iv_team)
    private val tvTeam: TextView = view.findViewById(R.id.tv_team)

    fun bind(team: Team?) {
      team?.let {
        Glide.with(itemView)
            .load(team.strTeamLogo)
            .into(ivTeam)
        tvTeam.text = team.strTeam
        itemView.setOnClickListener { _ ->
          listener(team, ivTeam)
        }
      }
    }
  }

}