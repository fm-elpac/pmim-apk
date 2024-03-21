package io.github.fm_elpac.pmim_apk.im

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.ComponentName
import android.os.IBinder

class Sc : ServiceConnection {
  override fun onBindingDied(name: ComponentName) {
    println("Sc.onBindingDied()")
  }

  override fun onNullBinding(name: ComponentName) {
    println("Sc.onNullBinding()")
  }

  override fun onServiceConnected(name: ComponentName, service: IBinder) {
    println("Sc.onServiceConnected()")
  }

  override fun onServiceDisconnected(name: ComponentName) {
    println("Sc.onServiceDisconnected()")
  }
}

fun 启动deno(c: Context) {
  c.bindService(
    Intent(c, ServerService::class.java),
    Sc(),
    Context.BIND_AUTO_CREATE or Context.BIND_IMPORTANT
  )
}
