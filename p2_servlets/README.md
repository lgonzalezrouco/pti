# Java Servlets

##1. Quick Start

NOTE: 
    Official lab description at: http://docencia.ac.upc.es/FIB/grau/PTI/lab/_servlet/servlets.pdf
    Examples at http://docencia.ac.upc.es/FIB/grau/PTI/lab/_servlet/p2codigo.tgz


###1.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14)

    user: alumne
    pwd: sistemes


###1.2 Install Tomcat 7

Open a terminal (CTRL+ALT+T).

Check if java is installed (if not you will have to install it):

    java -version

Install Tomcat 7:

    sudo apt-get update
    sudo apt-get install tomcat7 #password = sistemes
    sudo apt-get install tomcat7-docs tomcat7-admin

Check if it's running (with the browser): http://localhost:8080/   

See configuration at: /etc/tomcat7/

Webapps at: /var/lib/tomcat7

Restart Tomcat with:

    sudo service tomcat7 stop
    sudo service tomcat7 start


###1.3 Install examples

Install git:

    sudo apt-get install git

Download the examples (if you already have the pti repository, just do a git pull):

    cd $HOME       
    git clone https://github.com/rtous/pti.git
    cd pti/p2_servlets
    ls

##3. Advanced Tomcat configuration (not necessary to complete this lab)

Open ports for external access with:

    sudo ufw allow 8080/tcp
    sudo ufw allow 8443/tcp

Enabling webapp deployment with the manager:

    sudo vi /etc/tomcat7/tomcat-users.xml
        <role rolename="manager-gui"/>
        <role rolename="admin-gui"/>
        <user username="john" password="1234" roles="manager-gui,admin-gui"/>


Check manager with: http://mbdc1.pc.ac.upc.edu:8080/manager

Enabling HTTPS. 

    sudo keytool -genkey -alias tomcat -keyalg RSA #use MYPASSWORD
    sudo chmod a+r /root/.keystore
    sudo chmod a+x /root
    sudo vi /etc/tomcat7/server.xml
        <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
                maxThreads="150" scheme="https" secure="true"
            keystoreFile="/root/.keystore" keystorePass="MYPASSWORD" 
                   clientAuth="false" sslProtocol="TLS" />
    sudo service tomcat7 stop
    sudo service tomcat7 start

Check:

     sudo tail -n 200 /var/lib/tomcat7/logs/catalina.out

Enable MYSQL access:

    wget http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.35.tar.gz
    tar -xvzf mysql-connector-java-5.1.35.tar.gz
    sudo cp mysql-connector-java-5.1.35/mysql-connector-java-5.1.35-bin.jar /usr/share/tomcat7/lib/
    sudo chmod a+r /usr/share/tomcat7/lib/mysql-connector-java-5.1.35-bin.jar 

    


