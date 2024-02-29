package io.github.fm_elpac.pmim_apk.im

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Binder

// 运行 deno 的服务
class ServerService : Service() {
  var w: 工作线程? = null
  var t: Thread? = null

  override fun onCreate() {
    println("ServerService.onCreate()")

    // 启动工作线程
    w = 工作线程(
      getFilesDir(),
      getExternalFilesDir(null)!!,
      getExternalCacheDir()!!
    )
    t = Thread(w)
    t?.start()
  }

  override fun onBind(i: Intent): IBinder {
    println("ServerService.onBind()")

    // TODO
    return Binder()
  }

  override fun onDestroy() {
    println("ServerService.onDestroy()")

    // 停止工作线程
    w?.运行 = false
    t?.interrupt()
  }

  class 工作线程(
    val 文件目录: File,
    val 外部文件目录: File,
    val 外部缓存目录: File): Runnable {
    var 运行 = true

    override fun run() {
      println("ServerService::工作线程")

      while (运行) {
        try {
          运行deno()
        } catch (e: Exception) {
          println(e)
        }

        Thread.sleep(1000)
      }
    }

    fun 当前时间(): String {
      val f = SimpleDateFormat("yyyyMMdd'-'HHmmss")
      return f.format(Date())
    }

    fun 运行deno() {
      println("开始运行 deno")

      // /data/user/0/io.github.fm_elpac.pmim_apk/files/pmim_run/
      val 内部运行目录 = File(文件目录, "pmim_run")
      // 删除运行目录
      val 命令1 = "rm -r " + 内部运行目录.getAbsolutePath()
      println("ServerService: 执行 " + 命令1)
      val p1 = Runtime.getRuntime().exec(命令1)
      p1.waitFor()
      // 创建运行目录
      val 命令2 = "mkdir -p " + 内部运行目录.getAbsolutePath()
      println("ServerService: 执行 " + 命令2)
      val p2 = Runtime.getRuntime().exec(命令2)
      p2.waitFor()

      // /data/user/0/io.github.fm_elpac.pmim_apk/files/pmim_run/us
      val unix接口文件 = File(内部运行目录, "us")
      // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/pmim
      val 数据库目录 = File(外部文件目录, "pmim")

      // deno 目录
      val deno目录 = File(外部文件目录, "deno")

      // /data/user/0/io.github.fm_elpac.pmim_apk/files/setup/run.sh
      val 安装目标目录 = File(文件目录, "setup")
      val 启动文件 = File(安装目标目录, "run.sh")
      // /storage/emulated/0/Android/data/io.github.fm_elpac.pmim_apk/files/server/main.ts
      val 入口文件 = File(外部文件目录, "server/main.ts")

      // 生成日志文件名
      val 时间 = 当前时间()
      val 输出日志 = File(外部缓存目录, "deno-" + 时间 + "-o.log.txt")
      val 错误日志 = File(外部缓存目录, "deno-" + 时间 + "-e.log.txt")

      val 命令 = ProcessBuilder(
        // 设置环境变量
        "env",
        "DENO_DIR=" + deno目录.getAbsolutePath(),
        "PMIMS_US=" + unix接口文件.getAbsolutePath(),
        "XDG_RUNTIME_DIR=" + 外部文件目录.getAbsolutePath(),
        "PMIMS_DB=" + 数据库目录.getAbsolutePath(),
        "PMIMS_PORT=0",
        "PMIMS_ANDROID=1",

        启动文件.getAbsolutePath(),
        "run",
        "-A",
        "--unstable-kv",
        入口文件.getAbsolutePath()
      )
      命令.redirectOutput(输出日志)
      命令.redirectError(错误日志)
      命令.directory(安装目标目录)

      println("ServerService: 执行 " + 命令.command())
      // 运行命令
      val 进程 = 命令.start()
      val c = 进程.waitFor()
      if (0 != c) {
        println("ServerService: 退出码 " + c)
      }
    }
  }
}
