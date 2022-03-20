sudo docker ps -a -q --filter "name=cruit" | grep -q . && docker stop cruit && docker rm cruit | true

sudo docker rmi minor/cruit:1.0

sudo docker pull minor/cruit:1.0

docker run -d -p 8080:8080 --name cruit minor/cruit:1.0

docker rmi -f $(docker image -f "dangling=true" -q) || true