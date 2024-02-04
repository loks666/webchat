# 使用 OpenJDK 镜像
FROM openjdk:8-jdk-alpine

# 安装字体和字体配置
RUN apk add --no-cache fontconfig ttf-dejavu

# 从本地拷贝构建的结果
COPY ./target/webchat.jar /opt/app.jar

# 暴露端口
EXPOSE 8101

# 设置容器启动时运行的命令
ENTRYPOINT ["java","-Djava.awt.headless=true","-jar","/opt/app.jar"]
#=========================================这是在Docker打包【不推荐，在本地打好包后直接用上面的命令即可】================================================================
# 第一阶段：使用 Maven 镜像进行构建
#FROM maven:3.6.0-jdk-8-slim AS build
#
## 设置工作目录
#WORKDIR /build
#
## 创建自定义的 settings.xml 并设置阿里云镜像源
#RUN echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \
#      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
#      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 \
#                          http://maven.apache.org/xsd/settings-1.0.0.xsd"> \
#    <mirrors> \
#      <mirror> \
#        <id>alimaven</id> \
#        <name>aliyun maven</name> \
#        <url>http://maven.aliyun.com/nexus/content/groups/public/</url> \
#        <mirrorOf>central</mirrorOf> \
#      </mirror> \
#    </mirrors> \
#  </settings>' > /usr/share/maven/conf/settings.xml
#
## 拷贝 pom.xml 并下载依赖，利用 Docker 的缓存机制，避免每次构建都下载依赖
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
## 拷贝源代码并构建
#COPY src ./src
#RUN mvn clean package
#
## 第二阶段：运行阶段
#FROM openjdk:8-jdk-alpine
#
## 从构建阶段拷贝构建的结果
#COPY --from=build /build/target/webchat.jar /opt/app.jar
#
## 暴露端口
#EXPOSE 8101
#
## 设置容器启动时运行的命令
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app.jar"]

