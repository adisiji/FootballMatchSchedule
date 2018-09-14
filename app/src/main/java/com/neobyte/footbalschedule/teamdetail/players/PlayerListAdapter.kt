package com.neobyte.footbalschedule.teamdetail.players

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.models.Player
import com.neobyte.footbalschedule.teamdetail.players.PlayerListAdapter.PlayerListViewHolder

class PlayerListAdapter(private val playerList: List<Player?>,
                        private val listener: (player: Player, view: View) -> Unit) :
    RecyclerView.Adapter<PlayerListViewHolder>() {

  override fun onCreateViewHolder(p0: ViewGroup,
                                  p1: Int): PlayerListViewHolder {
    return PlayerListViewHolder(
        LayoutInflater.from(p0.context).inflate(R.layout.item_player_team, p0, false))
  }

  override fun getItemCount(): Int = playerList.size

  override fun onBindViewHolder(p0: PlayerListViewHolder,
                                p1: Int) {
    p0.bind(playerList[p1])
  }

  inner class PlayerListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val ivPlayer: ImageView = view.findViewById(R.id.iv_player)
    private val tvPlayerName: TextView = view.findViewById(R.id.tv_player_name)
    private val tvPlayerPos: TextView = view.findViewById(R.id.tv_player_position)

    fun bind(player: Player?) {
      player?.let {
        Glide.with(itemView)
            .load(player.strThumb)
            .into(ivPlayer)
        tvPlayerName.text = player.strPlayer
        tvPlayerPos.text = player.strPosition
        itemView.setOnClickListener { _ ->
          listener(it, ivPlayer)
        }
      }
    }

  }
}