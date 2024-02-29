package io.github.fm_elpac.pmim_apk

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val c = ProotSetup(this)
    c.开始()

    // TODO
  }
}
