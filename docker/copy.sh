#!/bin/sh

# 创建所有必需的目录
echo "Creating required directories..."
mkdir -p ./mysql/db
mkdir -p ./nginx/html/dist
mkdir -p ./ruoyi/gateway/jar
mkdir -p ./ruoyi/auth/jar
mkdir -p ./ruoyi/visual/monitor/jar
mkdir -p ./ruoyi/modules/system/jar
mkdir -p ./ruoyi/modules/file/jar
mkdir -p ./ruoyi/modules/job/jar
mkdir -p ./ruoyi/modules/word/jar

# 复制 sql
echo "begin copy sql "
cp ../sql/ry_20240629.sql ./mysql/db
cp ../sql/ry_config_20240902.sql ./mysql/db

# 复制 html
echo "begin copy html "
cp -r ../ruoyi-ui/dist/** ./nginx/html/dist

# 复制 jar
echo "begin copy ruoyi-gateway "
cp ../ruoyi-gateway/target/ruoyi-gateway.jar ./ruoyi/gateway/jar

echo "begin copy ruoyi-auth "
cp ../ruoyi-auth/target/ruoyi-auth.jar ./ruoyi/auth/jar

echo "begin copy ruoyi-visual "
cp ../ruoyi-visual/ruoyi-monitor/target/ruoyi-visual-monitor.jar  ./ruoyi/visual/monitor/jar

echo "begin copy ruoyi-modules-system "
cp ../ruoyi-modules/ruoyi-system/target/ruoyi-modules-system.jar ./ruoyi/modules/system/jar

echo "begin copy ruoyi-modules-file "
cp ../ruoyi-modules/ruoyi-file/target/ruoyi-modules-file.jar ./ruoyi/modules/file/jar

echo "begin copy ruoyi-modules-job "
cp ../ruoyi-modules/ruoyi-job/target/ruoyi-modules-job.jar ./ruoyi/modules/job/jar

echo "begin copy ruoyi-modules-word "
cp ../ruoyi-modules/word/target/word.jar ./ruoyi/modules/word/jar

echo "All files copied successfully!"