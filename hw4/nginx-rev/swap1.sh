#/bin/bash
# This shell is to swap from web2 to web1
cd /etc/nginx
sed -e s?web2:8000/activity/?web1:8000/activity/? <nginx.conf > /tmp/xxx
cp /tmp/xxx nginx.conf
service nginx reload