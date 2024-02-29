package io.github.fm_elpac.pmim_apk.im

import java.io.File

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.webkit.WebView
import android.webkit.JavascriptInterface
import android.os.Looper
import android.os.Handler
import android.os.SystemClock

// 输入法软键盘界面 (WebView)
class ImView(val p: PmimService) {

  fun 设置高度dp(view: View, 高: Float) {
    // dp -> px
    val d = p.resources.displayMetrics.density
    val px = 高 * d + 0.5f
    println("  dp = " + 高 + "  d = " + d + "  px = " + px)

    view.setLayoutParams(LayoutParams(-1, px.toInt()))
  }

  // 设置软键盘高度
  private fun 初始化高度(view: View): View {
    设置高度dp(view, 320f)

    val l = LinearLayout(p)
    l.addView(view)
    return l
  }

  fun onCreateInputView(): View {
    // 启用调试 (不安全)
    //WebView.setWebContentsDebuggingEnabled(true)

    // 创建 WebView
    val w = WebView(p)
    // 启用 javascript
    w.getSettings().setJavaScriptEnabled(true)

    w.addJavascriptInterface(接口(this, p, w), "pmim_a")

    val URL = 加载地址()
    println("ImView: " + URL)

    w.loadUrl(URL)
    return 初始化高度(w)
  }

  fun 加载地址(): String {
    // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/ui/lo/index.html
    val 外部文件目录 = p.getExternalFilesDir(null)!!
    val 首页文件 = File(外部文件目录, "ui/lo/index.html")
    return "file://" + 首页文件.getAbsolutePath()
  }

  // Android 剪切板
  fun 剪切板(): ClipboardManager {
    return p.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  }

  fun 格式化剪切板数据(数据: ClipData, c: Context): List<String> {
    val 计数 = 数据.getItemCount()
    val 结果: MutableList<String> = mutableListOf()
    for (i in 0..(计数 - 1)) {
      结果.add(数据.getItemAt(i).coerceToText(c).toString())
    }
    return 结果
  }

  // 获取剪切板文本
  fun im_剪切板取文本(): List<String> {
    val d = 剪切板().getPrimaryClip()
    if (null != d) {
      return 格式化剪切板数据(d, p)
    }
    return listOf()
  }

  // 设置剪切板文本
  fun im_剪切板设文本(t: String) {
    剪切板().setPrimaryClip(ClipData.newPlainText(t, t))
  }

  // 清空剪切板
  fun im_剪切板清空() {
    剪切板().clearPrimaryClip()
  }

  class 接口(val v: ImView, val p: PmimService, val w: WebView) {
    val h = Handler(Looper.getMainLooper())

    // 输入: 提交文本
    @JavascriptInterface
    fun im_提交(t: String) {
      p.im_提交文本(t)
    }

    // 关闭软键盘
    @JavascriptInterface
    fun im_隐藏键盘() {
      println("ImView: im_隐藏键盘")

      p.im_隐藏键盘()
    }

    // 发送编辑器默认动作
    @JavascriptInterface
    fun im_发送默认(来自回车键: Boolean) {
      p.im_发送编辑器默认动作(来自回车键)
    }

    // 发送字符
    @JavascriptInterface
    fun im_发送键字符(c: Char) {
      p.im_发送键字符(c)
    }

    // 获取选择的文本 (复制)
    @JavascriptInterface
    fun im_取选择的文本(): String? {
      return p.im_取选择的文本()
    }

    // 设置选择的文本 (比如: 全选)
    @JavascriptInterface
    fun im_设选择(开始: Int, 结束: Int) {
      p.im_设选择(开始, 结束)
    }

    // 设置软键盘高度 (dp)
    @JavascriptInterface
    fun im_设置高度dp(高: Float) {
      v.设置高度dp(w, 高)
    }

    // 创建 KeyEvent
    // meta: ctrl/shift/alt 等按键状态
    fun 创建键事件(keycode: Int, action: Int, meta: Int = 0): KeyEvent {
      // 标准化 meta
      val m = KeyEvent.normalizeMetaState(meta)
      val time = SystemClock.uptimeMillis()
      val ke = KeyEvent(
        time, // downTime
        time, // eventTime
        action, // action
        keycode, // code
        0, // repeat
        m // metaState
      )
      // 软键盘标志
      return KeyEvent.changeFlags(ke, KeyEvent.FLAG_SOFT_KEYBOARD)
    }

    // 发送键按下
    @JavascriptInterface
    fun im_发送键按下(键码: Int, meta: Int = 0) {
      p.发送键事件(创建键事件(键码, KeyEvent.ACTION_DOWN, meta))
    }

    // 发送键释放
    @JavascriptInterface
    fun im_发送键释放(键码: Int, meta: Int = 0) {
      p.发送键事件(创建键事件(键码, KeyEvent.ACTION_UP, meta))
    }

    // 发送按下并松开一个按键
    @JavascriptInterface
    fun im_发送键点击(键码: Int, meta: Int = 0) {
      im_发送键按下(键码, meta)
      im_发送键释放(键码, meta)
    }

    // 发送退格键 (backspace)
    @JavascriptInterface
    fun im_发送键_退格() {
      im_发送键点击(KeyEvent.KEYCODE_DEL)
    }

    // 发送删除键 (编辑键区 delete)
    @JavascriptInterface
    fun im_发送键_删除() {
      im_发送键点击(KeyEvent.KEYCODE_FORWARD_DEL)
    }

    // 发送 home 键 (编辑键区)
    @JavascriptInterface
    fun im_发送键_home() {
      im_发送键点击(KeyEvent.KEYCODE_MOVE_HOME)
    }

    // 发送 end 键 (编辑键区)
    @JavascriptInterface
    fun im_发送键_end() {
      im_发送键点击(KeyEvent.KEYCODE_MOVE_END)
    }

    // 发送 pageup 键 (编辑键区)
    @JavascriptInterface
    fun im_发送键_上页() {
      im_发送键点击(KeyEvent.KEYCODE_PAGE_UP)
    }

    // 发送 pagedown 键 (编辑键区)
    @JavascriptInterface
    fun im_发送键_下页() {
      im_发送键点击(KeyEvent.KEYCODE_PAGE_DOWN)
    }

    // 发送回车键 (enter)
    @JavascriptInterface
    fun im_发送键_回车() {
      im_发送键点击(KeyEvent.KEYCODE_ENTER)
    }

    // 方向键: 上
    @JavascriptInterface
    fun im_发送键_上() {
      im_发送键点击(KeyEvent.KEYCODE_DPAD_UP)
    }

    // 方向键: 下
    @JavascriptInterface
    fun im_发送键_下() {
      im_发送键点击(KeyEvent.KEYCODE_DPAD_DOWN)
    }

    // 方向键: 左
    @JavascriptInterface
    fun im_发送键_左() {
      im_发送键点击(KeyEvent.KEYCODE_DPAD_LEFT)
    }

    // 方向键: 右
    @JavascriptInterface
    fun im_发送键_右() {
      im_发送键点击(KeyEvent.KEYCODE_DPAD_RIGHT)
    }

    // 全选 (Ctrl+A)
    @JavascriptInterface
    fun im_全选() {
      im_发送键点击(KeyEvent.KEYCODE_A, KeyEvent.META_CTRL_LEFT_ON)
    }

    // 撤销 (Ctrl+Z)
    @JavascriptInterface
    fun im_撤销() {
      im_发送键点击(KeyEvent.KEYCODE_Z, KeyEvent.META_CTRL_LEFT_ON)
    }

    // 重做 (Ctrl+Shift+Z)
    @JavascriptInterface
    fun im_重做() {
      im_发送键点击(KeyEvent.KEYCODE_Z, KeyEvent.META_CTRL_LEFT_ON or KeyEvent.META_SHIFT_LEFT_ON)
    }

    // 获取剪切板文本
    @JavascriptInterface
    fun im_剪切板取文本(): List<String> {
      return v.im_剪切板取文本()
    }

    // 设置剪切板文本
    @JavascriptInterface
    fun im_剪切板设文本(t: String) {
      v.im_剪切板设文本(t)
    }

    // 清空剪切板
    @JavascriptInterface
    fun im_剪切板清空() {
      v.im_剪切板清空()
    }

    // 获取 pmim-server 的 HTTP 端口号
    @JavascriptInterface
    fun pm_端口(): String {
      // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/pmim/port
      val 外部文件目录 = p.getExternalFilesDir(null)!!
      val 端口文件 = File(外部文件目录, "pmim/port")
      println("ImView: 端口文件 " + 端口文件.getAbsolutePath())

      return 端口文件.readText()
    }

    // 读取 pmim-server 的口令
    @JavascriptInterface
    fun pm_口令(): String {
      // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/pmim/server_token
      val 外部文件目录 = p.getExternalFilesDir(null)!!
      val 口令文件 = File(外部文件目录, "pmim/server_token")
      println("ImView: 口令文件 " + 口令文件.getAbsolutePath())

      return 口令文件.readText()
    }

    // 日志输出, 用于调试
    @JavascriptInterface
    fun log(t: String) {
      println("ImView.log: " + t)
    }

    // 加载 URL (重定向)
    @JavascriptInterface
    fun 加载(u: String?) {
      // 默认加载 lo 加载器
      var 地址 = u
      if ((null == 地址) || (地址.length < 1)) {
        地址 = v.加载地址()
      }
      println("ImView.load: " + 地址)

      try {
        // run on ui thread
        h.post {
          w.loadUrl(地址)
        }
      } catch (e: Exception) {
        // TODO
        println(e)
      }
    }
  }
}
