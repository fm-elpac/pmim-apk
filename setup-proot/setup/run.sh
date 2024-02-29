#!/system/bin/sh
export LD_LIBRARY_PATH=$(pwd)

export PROOT_TMP_DIR=$(pwd)/tmp
export PROOT_LOADER=$(pwd)/loader

./proot \
  --bind=debian12_aarch64/tmp:/dev/shm \
  --bind=/sys \
  --bind=/proc/self/fd/2:/dev/stderr \
  --bind=/proc/self/fd/1:/dev/stdout \
  --bind=/proc/self/fd/0:/dev/stdin \
  --bind=/proc/self/fd:/dev/fd \
  --bind=/proc \
  --bind=/dev/urandom:/dev/random \
  --bind=/dev \
  --bind=/data \
  --bind=/storage \
  --bind=/sdcard \
  -L \
  --kernel-release=6.2.1-PRoot-Distro \
  --sysvipc \
  --link2symlink \
  --kill-on-exit \
  --cwd=/ \
  --change-id=0:0 \
  --rootfs=debian12_aarch64 \
  /usr/bin/deno "$@"
