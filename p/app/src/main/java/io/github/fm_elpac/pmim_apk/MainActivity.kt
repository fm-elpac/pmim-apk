package io.github.fm_elpac.pmim_apk

import android.app.Activity
import android.os.Bundle

import io.github.fm_elpac.pmim_apk.im.启动deno
import io.github.fm_elpac.pmim_apk.im.Wv

class MainActivity : Activity() {
  val w: Wv = Wv(this, true)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    println("MainActivity.onCreate()")

    // proot 安装 (deno)
    val c = ProotSetup(this)
    c.开始()

    启动deno(this)

    // 显示 WebView
    setContentView(w.createView())
  }
}
