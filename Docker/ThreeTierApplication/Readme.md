##Introduction
This is a sample implementation of 3-tier application running inside Docker containers. The containers comprises of nginx loadbalancer, play based web server, mongo database and redis cache; one instance each. It is recommended to get acquainted with concepts of Docker, Dockerfile and Docker compose tool.

##Details
- The project acts a reference for how to run a 3 tier application inside Docker, separating the DB, Redis from Webserver implementation.
- All components run in different containers and they are linked to each other statically during start up.
- The DB file of Mongo container is mapped to one of the host folders. This is to demonstrate the common usecase to persist the DB when the ephemeral containers can go down anytime.
- Containers 
- Optional: It is possible to map a remote NFS fileshare to containers. This is achieved using Docker volume plugins. Refer to this [document](https://docs.google.com/a/egnaroinc.com/document/d/18Tqvh0X2qosOp0UAaBSEs0z5ErFahgZHV4cTxcPzQyk/edit?usp=sharing_eid&ts=5799c5dc "Convoy Docker Volume Plugin") for details on how to get this setup.
- Multicontainer orchestration is acheived using Docker compose tool. It builds image from Dockerfile, spins off containers and links them.
- Note that Play server Dockerfile points to pre-built image (by Prasad). Anyone changing the Play source (under /src) is requested to create a Play dist, push the image and change the Dockerfile. This overhead is essential since running Play dist required one time preparation of application secret key. Creating a separate image was the only way to provide seamless experience. This step can be eliminated if we can generate Play dist without app secret keys.

##Known limitation
Since all the containers are statically linked at startup by docker compose, if one of these containers goes down and a new instance is comes up, other containers wont be aware of it. There are altrenatives to fix this (Service Discovery) issue. But those are beyond the scope of this demo project.

##Steps to run the application
1. Install latest Docker from [here](https://get.docker.com/). As mentioned on the console after successful installation, add current user to Docker usergroup to avoid sudo for each docker command.
1. Install latest Docker compose tool from [here](https://docs.docker.com/compose/install/).
1. Download this folder from Git to local directory.
1. A local folder to map DB file for Mongo DB is created. Details are in .yml file.
1. Goto folder where docker-compose.yml file is present and run **docker-compose up**. This will take sometime since it downloads all base images from DockerHub, creates custom layers to those images and starts containers on local machine.
1. Connect to webserver using http://localhost. If all goes well, you will see a number on the page. It indicates the no. of times the page is visited. Every refresh increments the visited count, stores in redis and mongo. Hence multiple webserver instances can be added and linked to LB and they can share the same info from Redis. Mongo persists the data (mapped to host directory). Hence the server will resume from last count if it is restarted.
1. Use http://localhost/data to retrieve contents of DB.
1. To check Mongo DB persistence, bring down the container using **docker-compose stop** and do **docker-compose start**. This will not delete the container images. But **docker-compose down** will destroy containers. You will have to rebuild the containers using docker-compose up.

##Note to committers who change src code

- Once the code is updated and tested, create a dist for the project using 'activator dist'
- Then use the Dockerfile present [here](https://hub.docker.com/r/prasadrk/play-3-tier-server/) as reference and create your own image (use docker build cmd) and push the image (docker push) to DockerHub.
- Update the Dockerfile in ThreeTierApplication to point to new Docker image.


Have fun with Dockers!
