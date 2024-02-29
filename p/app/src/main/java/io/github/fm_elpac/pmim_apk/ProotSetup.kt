package io.github.fm_elpac.pmim_apk

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

import android.content.Context

// 初始化 proot 二进制文件包
class ProotSetup(val c: Context) {

  fun 开始() {
    println("ProotSetup.开始()")

    val t = Thread(初始化线程(c))
    t.start()
  }

  class 初始化线程(val c: Context) : Runnable {
    var 数据目录: File = File("/")
    var 缓存目录: File = File("/")
    var 文件目录: File = File("/")
    var 外部文件目录: File = File("/")
    var 外部缓存目录: File = File("/")
    var obb目录: File = File("/")
    var 安装目标目录: File = File("/")
    var 安装来源目录: File = File("/")

    // 一种简单的获取命令执行结果的方式
    var p_i = 0

    fun 获取目录() {
      数据目录 = c.getDataDir()
      缓存目录 = c.getCacheDir()
      文件目录 = c.getFilesDir()
      外部文件目录 = c.getExternalFilesDir(null)!!
      外部缓存目录 = c.getExternalCacheDir()!!
      obb目录 = c.getObbDir()
      println("ProotSetup  数据目录 " + 数据目录.getAbsolutePath())
      println("ProotSetup  缓存目录 " + 缓存目录.getAbsolutePath())
      println("ProotSetup  文件目录 " + 文件目录.getAbsolutePath())
      println("ProotSetup  外部文件目录 " + 外部文件目录.getAbsolutePath())
      println("ProotSetup  外部缓存目录 " + 外部缓存目录.getAbsolutePath())
      println("ProotSetup  obb目录 " + obb目录.getAbsolutePath())

      安装目标目录 = File(文件目录, "setup")
      安装来源目录 = File(外部文件目录, "setup")
      println("ProotSetup  安装目标目录 " + 安装目标目录.getAbsolutePath())
      println("ProotSetup  安装来源目录 " + 安装来源目录.getAbsolutePath())
    }

    fun 复制目录(来源: Path, 目标: Path) {
      Files.walk(来源).forEach {
        Files.copy(
          it,
          目标.resolve(来源.relativize(it)),
          StandardCopyOption.REPLACE_EXISTING)
      }
    }

    fun 执行命令(命令: ProcessBuilder): Boolean {
      p_i += 1
      println("ProotSetup  执行命令 (" + p_i + ") " + 命令.command())
      //命令.redirectOutput(ProcessBuilder.Redirect.INHERIT)
      //命令.redirectError(ProcessBuilder.Redirect.INHERIT)
      命令.redirectOutput(File(外部缓存目录, "p_log-o-" + p_i + ".txt"))
      命令.redirectError(File(外部缓存目录, "p_log-e-" + p_i + ".txt"))
      val p = 命令.start()
      val c = p.waitFor()
      if (0 != c) {
        println("ProotSetup  退出码 " + c)
        return true
      }
      return false
    }

    fun 初始化安装目录(): Boolean {
      获取目录()

      if (安装目标目录.exists()) {
        // 检查删除请求
        val 删除标记 = File(外部文件目录, "rm")
        println("ProotSetup  检查删除标记 " + 删除标记.getAbsolutePath())
        if (删除标记.exists()) {
          println("ProotSetup  开始删除")
          执行命令(ProcessBuilder("rm", "-r", 安装目标目录.getAbsolutePath()))
          执行命令(ProcessBuilder("rm", "-r", 删除标记.getAbsolutePath()))
          return true
        }
        println("ProotSetup  已安装")
        return false
      }
      if (!安装来源目录.exists()) {
        println("ProotSetup 安装来源目录不存在")
        return true
      }
      // 复制安装目录
      println("ProotSetup  开始复制")

      复制目录(安装来源目录.toPath(), 安装目标目录.toPath())

      println("ProotSetup  复制完毕")
      // 执行安装脚本
      val 初始化 = File(安装目标目录, "setup.sh")
      if (执行命令(ProcessBuilder("chmod", "+x", 初始化.getAbsolutePath()))) {
        return true
      }
      if (执行命令(ProcessBuilder("echo", "666"))) {
        return true
      }
      if (执行命令(ProcessBuilder("ls", "-l", 初始化.getAbsolutePath()))) {
        return true
      }
      if (执行命令(ProcessBuilder(初始化.getAbsolutePath(), 安装目标目录.getAbsolutePath()))) {
        return true
      }

      println("ProotSetup  初始化完毕")
      return false
    }

    override fun run() {
      println("ProotSetup::初始化线程")

      if (初始化安装目录()) {
        return
      }
      // TODO
    }
  }
}
