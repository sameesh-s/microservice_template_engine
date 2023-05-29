To build docker image 
----------------------   
gradle bootBuildImage

Builded image 
-------------   
docker.io/library/user:0.0.1-SNAPSHOT

To list the docker images 
-------------------------   
docker image ls | grep user 

To run the docker image 
-----------------------   
docker run -d -p 8080:8080 user:0.0.1-SNAPSHOT


To create endpoints needed for promentheus 
-------------------------------------------- 
1) expose endpoints using actuator
```
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=ALWAYS
```

2) add micrometer dependency 
```
	implementation 'io.micrometer:micrometer-registry-prometheus'
```
3) check /actuator/prometheus url exist

II, Create prometheus configuration 


III , Install and run the prometheus docker image 
```agsl
docker pull prom/prometheus

docker run -d --name=prometheus -p 9090:9090 -v /home/xcavenger/github/springboot_prometheus_grafana/user/user/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml 
```


to get info in actuator 
-----------------------
add this in build.grale 
```agsl
springBoot {
	buildInfo()
}
```
add this in application properties 
```agsl

## Configuring info endpoint
#management.endpoint.info.enabled=true
#management.endpoints.web.exposure.include=info,health
management.info.env.enabled=true
```

To create project using springCLI use the below command 
-------------------------------------------------------   
```agsl
spring init --boot-version=3.1.0 --build=gradle --java-version=11 --packaging=jar --name=usertemp --package-name=com.clim.user --groupId=com.clim.user --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT --type=gradle-project user-service
```

To list all the dependencies using springCLI 
---------------------------------------------   
```agsl
spring init --list 
```