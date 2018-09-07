package com.neobyte.footbalschedule

import android.support.test.runner.AndroidJUnitRunner
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins

class RxTestRunner : AndroidJUnitRunner() {
  override fun onStart() {
    RxJavaPlugins.setInitComputationSchedulerHandler(
        Rx2Idler.create("RxJava 2.x Computation Scheduler")
    )
    RxJavaPlugins.setInitIoSchedulerHandler(
        Rx2Idler.create("RxJava 2.x IO Scheduler")
    )
    RxAndroidPlugins.setInitMainThreadSchedulerHandler(
        Rx2Idler.create("RxJava 2.x MainThread Scheduler")
    )
    super.onStart()
  }

  override fun getProcessName()= "idling resource %d".format(hashCode())
}