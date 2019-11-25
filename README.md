# sqoop-api
sqoop-api

#远程登陆认证
默认的SUSE系统配置是不允许在程序中进行远程登陆认证的，为了使用远程壳程序需要做如下的修改：
``````````````
vi /etc/ssh/sshd_config
``````````````
``````````````
将PasswordAuthentication配置项改成yes
将MaxStartups（允许同时远程登陆的最大用户数）配置项改成需要的值，通常不应太高，默认为10亦可。

service sshd restart
``````````````

到Sqoop配置文件中$SQOOP_HOME/bin/configure-sqoop，注释掉Accumulo检查

``````````````
#if [ ! -d "${ACCUMULO_HOME}" ]; then
#  echo "Warning: $ACCUMULO_HOME does not exist! Accumulo imports will fail."
#  echo 'Please set $ACCUMULO_HOME to the root of your Accumulo installation.'
#fi
``````````````
在$SQOOP_HOME/bin目录下面修改configure-sqoop文件，注释掉以下内容
``````````````
#if [ -z "${HCAT_HOME}" ]; then
#  if [ -d "/usr/lib/hive-hcatalog" ]; then
#    HCAT_HOME=/usr/lib/hive-hcatalog
#  elif [ -d "/usr/lib/hcatalog" ]; then
#    HCAT_HOME=/usr/lib/hcatalog
#  else
#    HCAT_HOME=${SQOOP_HOME}/../hive-hcatalog
#    if [ ! -d ${HCAT_HOME} ]; then
#       HCAT_HOME=${SQOOP_HOME}/../hcatalog
#    fi
#  fi
#fi
#if [ -z "${ACCUMULO_HOME}" ]; then
#  if [ -d "/usr/lib/accumulo" ]; then
#    ACCUMULO_HOME=/usr/lib/accumulo
#  else
#    ACCUMULO_HOME=${SQOOP_HOME}/../accumulo
#  fi
#fi

## Moved to be a runtime check in sqoop.
#if [ ! -d "${HCAT_HOME}" ]; then
#  echo "Warning: $HCAT_HOME does not exist! HCatalog jobs will fail."
#  echo 'Please set $HCAT_HOME to the root of your HCatalog installation.'
#fi

#if [ ! -d "${ACCUMULO_HOME}" ]; then
#  echo "Warning: $ACCUMULO_HOME does not exist! Accumulo imports will fail."
#  echo 'Please set $ACCUMULO_HOME to the root of your Accumulo installation.'
#fi
``````````````
