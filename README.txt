通过实际操作FTP服务器，监测当前ftp的性能指标
注意：
如果你发现ftp的login时间开销很大，是由于域名反向解析导致的，需要关掉vsftp的这个功能

修改vsftp.conf
reverse_lookup_enable=NO
／etc/init.d/vsftp restart
