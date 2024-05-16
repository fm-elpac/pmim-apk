#!/system/bin/sh
echo setup.sh $1
cd $1

chmod +x lib/*
chmod +x deno

# 最后再将 `run.sh` 设置为可执行
chmod +x run.sh

echo setup ok.

# 测试运行
./run.sh --version
