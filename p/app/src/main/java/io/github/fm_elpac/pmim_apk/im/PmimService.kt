package io.github.fm_elpac.pmim_apk.im

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.content.Intent
import android.content.ServiceConnection
import android.content.ComponentName
import android.os.IBinder

// Android 输入法服务, 仅关注面向 Android 系统的接口部分
class PmimService : InputMethodService() {
  val v = ImView(this)

  // 生命周期函数
  override fun onCreate() {
    super.onCreate()
    // 用于调试 (服务生命周期), 下同
    println("PmimService.onCreate()")

    class Sc : ServiceConnection {
      override fun onBindingDied(name: ComponentName) {
        println("PmimService::ServiceConnection.onBindingDied()")
      }

      override fun onNullBinding(name: ComponentName) {
        println("PmimService::ServiceConnection.onNullBinding()")
      }

      override fun onServiceConnected(name: ComponentName, service: IBinder) {
        println("PmimService::ServiceConnection.onServiceConnected()")
      }

      override fun onServiceDisconnected(name: ComponentName) {
        println("PmimService::ServiceConnection.onServiceDisconnected()")
      }
    }

    // 启动 deno
    bindService(
      Intent(this, ServerService::class.java),
      Sc(),
      BIND_AUTO_CREATE or BIND_IMPORTANT
    )
  }

  override fun onCreateInputView(): View {
    println("PmimService.onCreateInputView()")

    return v.onCreateInputView()
  }

  override fun onBindInput() {
    super.onBindInput()
    println("PmimService.onBindInput()")
  }
  override fun onUnbindInput() {
    super.onUnbindInput()
    println("PmimService.onUnbindInput()")
  }

  // 软键盘显示
  override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
    println("PmimService.onStartInputView()")
  }
  // 软键盘隐藏
  override fun onFinishInput() {
    println("PmimService.onFinishInput()")
  }

  override fun onDestroy() {
    super.onDestroy()
    println("PmimService.onDestroy()")
  }

  // 通用输入事件
  override fun onGenericMotionEvent(event: MotionEvent): Boolean {
    // TODO
    return false
  }

  // 键按下
  override fun onKeyDown(keycode: Int, event: KeyEvent): Boolean {
    // TODO
    return false
  }

  // 键重复
  override fun onKeyMultiple(keycode: Int, count: Int, event: KeyEvent): Boolean {
    // TODO
    return false
  }

  // 键释放
  override fun onKeyUp(keycode: Int, event: KeyEvent): Boolean {
    // TODO
    return false
  }

  // 预留接口: 关闭软键盘
  fun im_隐藏键盘() {
    //hideWindow()

    requestHideSelf(0)
  }

  // 预留接口: 输入文本
  fun im_提交文本(文本: String) {
    currentInputConnection.commitText(文本, 1)
  }

  fun 发送键事件(事件: KeyEvent) {
    currentInputConnection.sendKeyEvent(事件)
  }

  // 预留接口: 发送编辑器默认动作 (比如: 搜索)
  fun im_发送编辑器默认动作(来自回车键: Boolean) {
    sendDefaultEditorAction(来自回车键)
  }

  // 预留接口: 发送字符
  fun im_发送键字符(c: Char) {
    sendKeyChar(c)
  }

  // 预留接口: 获取选择的文本 (复制)
  fun im_取选择的文本(): String? {
    return currentInputConnection.getSelectedText(0)?.toString()
  }

  // 预留接口: 设置选择的文本 (比如: 全选)
  fun im_设选择(开始: Int, 结束: Int) {
    currentInputConnection.setSelection(开始, 结束)
  }
}
