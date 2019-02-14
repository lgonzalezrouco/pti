# ANNEX: Dockerize your application

## 1. Install Docker

First you need to install Docker. In Linux you can check if it's already installed this way:

    docker -v

If not, you would need to install it. In Ubuntu you can do it this way:

    sudo apt-get update
    wget -qO- https://get.docker.com/ | sh
    sudo usermod -aG docker $(whoami)

It's necessary to LOGOUT to let the usermod command have effect.

Windows and OSX installation procedures can be found [here](https://docs.docker.com/install/).

NOTE: If for any reason you want to try Docker at the PTI lab classroom you would need to fix a problem with the DNS (Docker replicates the nameservers from /etc/resolv.conf but ignores the localhost entries, the public nameservers do not work because of the firewall).  
    
    nmcli dev show | grep 'IP4.DNS'
    sudo vi /etc/docker/daemon.json
        {
            "dns": ["147.83.30.71", "8.8.8.8"]
        }
    sudo service docker restart

## 2. Dockerize your application

Dockerizing (or containerizing) an application is the process of making it able to run and deploy under Docker containers. Usually the final step of a containerization process is to push a Docker image containg the application to a Docker Registry such as Docker Hub. However, for the PTI lab you need to provide the sources necessaries to build the image, i.e. a Dockerfile and the application. It's not necessary that you push the image to Docker Hub as the teacher will build the image himself.

*NOTE: A Docker volume can be used to access an application in your filesystem from within a Docker container. This is a convenient way to proceed during the development of the application. However, when moving to production, having a Docker image with everything inside makes things easier (e.g. deployment automation). So, for the containerization we will not need a Docker volume. However, we could still need one for persisting application data (not required for the PTI lab).*

First go into the folder that contains the "my_webapp" subfolder. If followed the recommended steps, this folder will be the Tomcat's root folder (apache-tomcat-9.0.5):

    cd $USER_HOME/apache-tomcat-9.0.5


*NOTE: If you did the application within a Docker container, you will need to copy the my_webapp folder from the container to the host with the [docker cp](./../../docker.md) command (we better avoid trying to run Docker within a Docker container). If you used a Docker volume to develop the application then you don't have this problem*


Then edit there a file named "Dockerfile" with the following contents:

	vi Dockerfile

    FROM tomcat:9
	COPY webapps/my_webapp /my_webapp
	WORKDIR /
	RUN cp -r my_webapp /usr/local/tomcat/webapps

### 2.1 Building the image from the Dockerfile

You will not deliver the image, and it's not necessary to push it to Docker Hub, so in theory it's not necessary to perform this step. However, you will need to do it, at least one time, to ensure that the Dockerfile that you are delivering to the teacher will work.

From the Tomcat folder run:

	docker build -f Dockerfile -t carrental .

### 2.2 Run a container

Run the container this way:

	docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental

Now check with your browser if the application is running. The teacher will get your Dockerfile and your my_webapp folder and will run the previous docker build and docker run commands.

More information about working with Docker images and containers can be found [here](./../../docker.md)

## 3 Deliver

Deliver a tarball containing the following:

	Dockerfile
	my_webapp


