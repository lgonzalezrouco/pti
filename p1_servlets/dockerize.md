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

First go into the Tomcat's webapps folder (where you have your my_webapp folder with your application):

    cd webapps

Then edit there a file named "Dockerfile" with the following contents:

    FROM tomcat:9
	RUN apt-get update && apt-get install -y default-jdk
	COPY my_webapp /my_webapp
	WORKDIR /
	RUN cp -r my_webapp /usr/local/tomcat/webapps

### 2.1 Building the image from the Dockerfile

	docker build -f Dockerfile -t carrental .

### 2.2 Run a container

	docker run --name carrental -d -p 8080:8080 -p 8443:8443 carrental

## 3 Deliver

Deliver a tarball containing the following:

	Dockerfile
	my_webapp


