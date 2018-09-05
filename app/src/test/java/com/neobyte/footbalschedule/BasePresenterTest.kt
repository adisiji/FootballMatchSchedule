package com.neobyte.footbalschedule

import org.junit.ClassRule

open class BasePresenterTest {

  companion object {
    @ClassRule
    @JvmField
    val schedulers = RxSchedulerRule()
  }

}