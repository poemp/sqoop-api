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