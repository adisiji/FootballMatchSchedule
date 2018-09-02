package com.neobyte.footbalschedule.favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.neobyte.footbalschedule.R

class FavoriteFragment : Fragment() {

  private val ARG_PARAM1 = "param1"
  private val ARG_PARAM2 = "param2"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_favorite, container, false)
  }

}
