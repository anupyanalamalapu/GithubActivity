#/bin/bash

function killitif {
    docker ps -a  > /tmp/yy_xx$$
    if grep --quiet $1 /tmp/yy_xx$$
     then
     echo "killing older version of $1"
     docker rm -f `docker ps -a | grep $1  | sed -e 's: .*$::'`
   fi
}


IMG=""
LIVE=""

if [ "$1" == "web1" ]; then
    IMG="old";
    LIVE="web2";
else
    IMG="new";
    LIVE="web1";
fi
killitif $1
sleep 5
docker run --network default --name=$1 $IMG &
if [ "$1" == "web1" ]; then
    echo "HERE1"
    sleep 20 && docker exec proxy_1 /bin/bash /bin/swap1.sh
else
    echo "HERE2"
   sleep 20 && docker exec proxy_1 /bin/bash /bin/swap2.sh
fi
killitif $LIVE