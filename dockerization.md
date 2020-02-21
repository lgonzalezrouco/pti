# ANNEX: Dockerize your application

## 1. Docker

*If you don't have Docker installed or if you've never used it let's read [this](./../../docker.md) first.

## 2. Dockerize your application

Dockerizing (or containerizing) an application is the process of making it able to run and deploy under Docker containers. Usually the final step of a containerization process is to push a Docker image containg the application to a Docker Registry such as Docker Hub. However, for the PTI lab you need to provide the sources necessaries to build the image, i.e. a Dockerfile and the application. It's not necessary that you push the image to Docker Hub as the teacher will build the image himself.

*NOTE: A Docker volume can be used to access an application in your filesystem from within a Docker container. This is a convenient way to proceed during the development of the application. However, when moving to production, having a Docker image with everything inside makes things easier (e.g. deployment automation). So, for the containerization we will not need a Docker volume. However, we could still need one for persisting application data (not required for the PTI lab).*

First go into the folder that contains the "my_webapp" subfolder. If you followed the recommended steps, this folder will be the Tomcat's webapps folder:

    cd $USER_HOME/apache-tomcat-9.0.5/webapps


*NOTE: If you did the application within a Docker container, you will need to copy the my_webapp folder from the container to the host with the [docker cp](./../../docker.md) command (we better avoid trying to run Docker within a Docker container). If you used a Docker volume to develop the application then you don't have this problem*


Then edit there a file named "Dockerfile" with the following contents:

	vi Dockerfile

    FROM tomcat:9
	COPY my_webapp /my_webapp
	WORKDIR /
	RUN cp -r my_webapp /usr/local/tomcat/webapps

*The official reference for writing a Dockerfile can be found [here](https://docs.docker.com/engine/reference/builder/).*

### 2.1 Building the image from the Dockerfile

You will not deliver the image, and it's not necessary to push it to Docker Hub, so in theory it's not necessary to perform this step. However, you will need to do it, at least one time, to ensure that the Dockerfile that you are delivering to the teacher will work.

From the folder containing the "my_webapp" subfolder (e.g. $USER_HOME/apache-tomcat-9.0.5/webapps) run:

	docker build -f Dockerfile -t carrental .

*This command will first send the files specified by the PATH (the local directory . in this case) to the Docker daemon (as the Docker daemon is not necessarily running in your machine). These files will be the "build context". Then the Dockerfile will be processed line by line. First, the FROM instruction specifies the Base Image from which you are building (one with Tomcat 9 in this case). Then the COPY command will copy all the files within the my_webapp folder (that you sent to the daemon as part of the build context) into a new /my_webapp folder within the image. This folder is finally copied into the /usr/local/tomcat/webapps.*

### 2.2 Run a container

Run the container this way:

	docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental

Now check with your browser if the application is running. The teacher will get your Dockerfile and your my_webapp folder and will run the previous docker build and docker run commands.

More information about working with Docker images and containers can be found [here](./../../docker.md)

## 3 Deliver

Deliver a tarball containing the following:

	Dockerfile
	my_webapp


