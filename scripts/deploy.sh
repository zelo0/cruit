sudo docker ps -a -q --filter "name=cruit" | grep -q . && docker stop cruit && docker rm cruit | true

sudo docker rmi lonelylee/cruit:1.0

sudo docker pull lonelylee/cruit:1.0

docker run -d -p 8080:8080 -v /home/ec2-user:/config -v /home/ec2-user:/logs --name cruit lonelylee/cruit:1.0

docker rmi -f $(docker images -f "dangling=true" -q) || true