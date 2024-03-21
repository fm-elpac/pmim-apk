// WebView 相关工具
package io.github.fm_elpac.pmim_apk.im

import java.io.File

import android.content.Context
import android.os.Looper
import android.os.Handler
import android.webkit.WebView
import android.webkit.JavascriptInterface

class Wv(val c: Context, val m: Boolean) {
  var v: WebView? = null;

  fun getView(): WebView {
    return v!!;
  }

  fun createView(): WebView {
    // 启用调试 (不安全)
    //WebView.setWebContentsDebuggingEnabled(true)

    val w = WebView(c)
    // 启用 javascript
    w.getSettings().setJavaScriptEnabled(true)

    if (m) {
      w.addJavascriptInterface(Wv接口(this), "pmim_a")
    }

    v = w;

    if (m) {
      加载(加载地址())
    }
    return w;
  }

  fun 加载(url: String) {
    println("Wv: " + url)
    v?.loadUrl(url)
  }

  fun 加载地址后缀(): String {
    if (m) {
      return "#m"
    }
    return ""
  }

  fun 加载地址(): String {
    // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/ui/lo/index.html
    val 外部文件目录 = c.getExternalFilesDir(null)!!
    val 首页文件 = File(外部文件目录, "ui/lo/index.html")
    return "file://" + 首页文件.getAbsolutePath() + 加载地址后缀()
  }

  fun 读取端口(): String {
    // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/pmim/port
    val 外部文件目录 = c.getExternalFilesDir(null)!!
    val 端口文件 = File(外部文件目录, "pmim/port")
    println("Wv: 端口文件 " + 端口文件.getAbsolutePath())

    return 端口文件.readText()
  }

  fun 读取口令(): String {
    // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/pmim/server_token
    val 外部文件目录 = c.getExternalFilesDir(null)!!
    val 口令文件 = File(外部文件目录, "pmim/server_token")
    println("Wv: 口令文件 " + 口令文件.getAbsolutePath())

    return 口令文件.readText()
  }
}

open class Wv接口(val w: Wv) {
  val h = Handler(Looper.getMainLooper())

  // 获取 pmim-server 的 HTTP 端口号
  @JavascriptInterface
  fun pm_端口(): String {
    return w.读取端口()
  }

  // 读取 pmim-server 的口令
  @JavascriptInterface
  fun pm_口令(): String {
    return w.读取口令()
  }

  // 日志输出, 用于调试
  @JavascriptInterface
  fun log(t: String) {
    println("Wv.log: " + t)
  }

  // 加载 URL (重定向)
  @JavascriptInterface
  fun 加载(u: String?) {
    // 默认加载 lo 加载器
    var 地址 = u
    if ((null == 地址) || (地址.length < 1)) {
      地址 = w.加载地址()
    }
    println("Wv.load: " + 地址)

    try {
      // run on ui thread
      h.post {
        w.加载(地址)
      }
    } catch (e: Exception) {
      // TODO
      println(e)
    }
  }
}
