package com.neobyte.footbalschedule.league

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.league.SearchLeagueAdapter.LeagueViewHolder
import com.neobyte.footbalschedule.models.League

class SearchLeagueAdapter(private val list: List<League?>,
                          private val listener: (league: League) -> Unit) : RecyclerView.Adapter<LeagueViewHolder>() {

  override fun onCreateViewHolder(p0: ViewGroup,
                                  p1: Int): LeagueViewHolder {
    return LeagueViewHolder(
        LayoutInflater.from(p0.context).inflate(R.layout.item_league, p0, false))
  }

  override fun getItemCount(): Int = list.size

  override fun onBindViewHolder(p0: LeagueViewHolder,
                                p1: Int) {
    p0.bindItem(list[p1], p1)
  }

  inner class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val leagueLogo : ImageView = view.findViewById(R.id.iv_logo)
    private val leagueName: TextView= view.findViewById(R.id.tv_name)

    fun bindItem(league: League?,
                 pos: Int) {
      league?.let {
        Glide.with(itemView)
            .load(league.strLogo)
            .into(leagueLogo)

        leagueName.text = league.strLeague

        itemView.setOnClickListener { _ ->
          listener(it)
        }
      }
    }
  }
}