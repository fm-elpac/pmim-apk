# setup-deno

在 Android 上运行 `deno` (aarch64) 的新方法 (glibc-runner).

## 解释

- (1) 下载官方编译好的 deno 二进制: <https://github.com/denoland/deno/releases>

  `deno-aarch64-unknown-linux-gnu.zip`

  解压, 获得其中的 `deno` 文件.

- (2) `deno` 运行需要下列库:

  ```sh
  $ ldd deno
          libdl.so.2 => /data/data/com.termux/files/usr/glibc/lib/libdl.so.2
          libgcc_s.so.1 => /data/data/com.termux/files/usr/glibc/lib/libgcc_s.so.1
          libpthread.so.0 => /data/data/com.termux/files/usr/glibc/lib/libpthread.so.0
          libm.so.6 => /data/data/com.termux/files/usr/glibc/lib/libm.so.6
          libc.so.6 => /data/data/com.termux/files/usr/glibc/lib/libc.so.6
          ld-linux-aarch64.so.1 => /data/data/com.termux/files/usr/glibc/lib/ld-linux-aarch64.so.1
  $
  ```

- (3) 从 `termux-pacman` 下载 `glibc` 和 `gcc-libs-glibc` 软件包:

  ```sh
  curl -O https://service.termux-pacman.dev/gpkg/aarch64/gpkg.json

  curl https://service.termux-pacman.dev/gpkg/aarch64/$(cat gpkg.json | jq -r '."glibc".FILENAME') -o glibc.tar.xz

  curl https://service.termux-pacman.dev/gpkg/aarch64/$(cat gpkg.json | jq -r '."gcc-libs-glibc".FILENAME') -o gcc-libs-glibc.tar.xz
  ```

  解压, 获取 `*.so` 文件.

- (4) 使用 `patchelf` 修改 `deno`:

  ```sh
  > patchelf --set-rpath /data/local/tmp/lib --set-interpreter /data/local/tmp/lib/ld-linux-aarch64.so.1 deno
  ```

- (5) 使用 `adb push` 传输相关文件到 Android 手机, 并使用 `adb shell` 运行测试:

  ```sh
  raphael:/data/local/tmp $ pwd
  /data/local/tmp
  raphael:/data/local/tmp $ ls -l 
  total 138648
  -rwxrwxrwx 1 shell shell 141959425 2024-05-17 06:57 deno
  drwxrwxr-x 2 shell shell      4096 2024-05-17 06:54 lib
  raphael:/data/local/tmp $ ls -l lib
  total 4240
  -rwxrwxrwx 1 shell shell  241064 2024-05-17 06:53 ld-linux-aarch64.so.1
  -rwxrwxrwx 1 shell shell 2292352 2024-05-17 06:53 libc.so.6
  -rwxrwxrwx 1 shell shell   69736 2024-05-17 06:53 libdl.so.2
  -rw-rw-rw- 1 shell shell  591400 2024-05-17 06:53 libgcc_s.so.1
  -rwxrwxrwx 1 shell shell 1039216 2024-05-17 06:53 libm.so.6
  -rwxrwxrwx 1 shell shell   70120 2024-05-17 06:53 libpthread.so.0
  raphael:/data/local/tmp $ export HOME=$(pwd)
  raphael:/data/local/tmp $ ./deno --version
  deno 1.43.3 (release, aarch64-unknown-linux-gnu)
  v8 12.4.254.13
  typescript 5.4.5
  raphael:/data/local/tmp $ ./deno
  Deno 1.43.3
  exit using ctrl+d, ctrl+c, or close()
  REPL is running with all permissions allowed.
  To specify permissions, run `deno repl` with allow flags.
  > 0.1 + 0.2
  0.30000000000000004
  > Deno.version
  { deno: "1.43.3", v8: "12.4.254.13", typescript: "5.4.5" }
  >
  ```

## 相关链接

- <https://github.com/denoland/deno/issues/19759>
- <https://github.com/termux-pacman/glibc-packages/wiki/About-glibc-runner-(grun)>
- <https://github.com/NixOS/patchelf>
- <https://github.com/termux-pacman/glibc-packages>
- <https://github.com/termux-pacman/glibc-packages/tree/main/gpkg/glibc-runner>
- <https://github.com/denoland/deno>
- <https://service.termux-pacman.dev/gpkg/aarch64/>

TODO
