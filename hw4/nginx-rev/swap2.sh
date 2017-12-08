#/bin/bash
# This shell is to swap from web1 to web2
cd /etc/nginx
sed -e s?web1:8000/activity/?web2:8000/activity/? <nginx.conf > /tmp/xxx
cp /tmp/xxx nginx.conf
service nginx reload 
