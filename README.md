# ftp服务性能监测
通过实际操作FTP服务器，监测当前ftp的性能指标

# ftp配置优化
注意：
如果你发现ftp的login时间开销很大，是由于域名反向解析导致的，需要关掉vsftp的这个功能

修改vsftp.conf
reverse_lookup_enable=NO
／etc/init.d/vsftp restart

#文档
[ftp服务性能监测](http://zgj0315.github.io//diary/2017/02/26/ftp_server_performance_monitor.html)