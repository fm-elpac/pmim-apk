# pmim-apk

<https://github.com/fm-elpac/pmim-apk>

胖喵输入法: Android 应用

![CI](https://github.com/fm-elpac/pmim-apk/actions/workflows/ci.yml/badge.svg)

镜像:

- <https://bitbucket.org/fm-elpac/pmim-apk/>
- <https://codeberg.org/fm-elpac/pmim-apk>
- <https://notabug.org/fm-elpac/pmim-apk>
- <https://gitlab.com/fm-elpac/pmim-apk>

---

本输入法是跨平台的, 这是 Android 平台的版本.

- 项目主页 (拼音核心): <https://github.com/fm-elpac/pmim>

- GNU/Linux (ibus) 版本: <https://github.com/fm-elpac/pmim-ibus>

Android 版本只提供源代码, 不提供编译好的 apk 文件. 请下载源代码自己使用 Android
Studio 编译. 和编译普通的 apk 一样, 没有需要特别注意的地方.

---

- 安装说明请见: [doc/安装.md](./doc/安装.md)

- 本地开发运行请见: [doc/开发调试.md](./doc/开发调试.md)

## 相关文章

- 《Android 输入法框架简介》
  - <https://www.bilibili.com/read/cv31912233/>
  - <https://zhuanlan.zhihu.com/p/683489173>
  - <https://juejin.cn/post/7343905368621940772>
  - <https://blog.csdn.net/secext2022/article/details/136246340>

- 《在 Android 运行 GNU/Linux 二进制程序 (proot)》
  - <https://www.bilibili.com/read/cv32154318/>
  - <https://zhuanlan.zhihu.com/p/684273410>
  - <https://juejin.cn/post/7343902139822096420>
  - <https://blog.csdn.net/secext2022/article/details/136333781>

- 《高版本 Android 如何访问 sdcard/Android/data 目录中的文件 (翻译)》
  - <https://www.bilibili.com/read/cv32665054/>
  - <https://zhuanlan.zhihu.com/p/684280668>
  - <https://juejin.cn/post/7343902139822161956>
  - <https://blog.csdn.net/secext2022/article/details/136335220>

## 例行更新维护策略

适用于本仓库 (pmim-apk). 当下列条件任意一条满足时,
本仓库的软件需要发布新的维护版本 (版本号 `x.y.z` 其中 `z` + 1). "更新所有依赖"
并重新编译 (构建):

- deno 发布新版本 (版本号 `x.y.z` 其中 `x` 或 `y` 变化)

- 各依赖或本仓库发布重要的安全更新 (修复比较严重的安全漏洞)

当前重要依赖的版本号:

- deno 1.42.4

  <https://github.com/denoland/deno>

## LICENSE

GNU General Public License v3.0 or later (SPDX Identifier: `GPL-3.0-or-later`)

<https://spdx.org/licenses/GPL-3.0-or-later.html>
