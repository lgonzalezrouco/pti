# Java Servlets

## 1. Quick Start

NOTE: 
    Old lab description at: http://docencia.ac.upc.es/FIB/grau/PTI/lab/_servlet/servlets.pdf
    Examples at http://docencia.ac.upc.es/FIB/grau/PTI/lab/_servlet/p2codigo.tgz


### 1.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14)

    user: alumne
    pwd: sistemes


### 1.2 Install Tomcat 7

Open a terminal (CTRL+ALT+T).

Let's start by updating the Ubuntu's Package Index:

    sudo apt-get update

Check if a Java SDK is installed:

    javac -version

If not installed do the following to install OpenJDK:

    sudo apt-get install default-jdk

Install Tomcat 7:

    sudo apt-get install tomcat7 
    sudo apt-get install tomcat7-docs tomcat7-admin

Start the service:

    	sudo service tomcat7 start

(Note: We will work with a system-wide Tomcat instance (as a service). It is also possible to work with a user-oriented instance (without the need of root rights). https://help.ubuntu.com/lts/serverguide/tomcat.html))

Check if it's running (with the browser): http://localhost:8080/ 

Tomcat files can be found in the following locations:

    Configuration at: /etc/tomcat7/ 
    (Note: configuration files in /var/lib/tomcat7/conf are just the defaults)
    Webapps at: /var/lib/tomcat7/webapps
    Logs at: /var/lib/tomcat7/logs

You can restart Tomcat with:

    sudo service tomcat7 stop
    sudo service tomcat7 start


### 1.3 Create and display a simple HTML page

    sudo mkdir /var/lib/tomcat7/webapps/my_webapp
    sudo vi /var/lib/tomcat7/webapps/my_webapp/index.html

        <html>
            <h1>Hello World!</h1>
        </html>

Check: http://localhost:8080/my_webapp

### 1.4 Create and simple servlet

    sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF
    sudo vi /var/lib/tomcat7/webapps/my_webapp/WEB-INF/web.xml

        <web-app>
            <servlet>
              <servlet-name>my_servlet</servlet-name>
              <servlet-class>mypackage.MyServlet</servlet-class>
            </servlet>
            <servlet-mapping>
              <servlet-name>my_servlet</servlet-name>
              <url-pattern>/my_servlet</url-pattern>
            </servlet-mapping>
        </web-app>
    
    sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes
    sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage
    sudo vi /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage/MyServlet.java

        package mypackage;
        import java.io.*;
        import javax.servlet.*;
        import javax.servlet.http.*;
        public class MyServlet extends HttpServlet {
          public void doGet(HttpServletRequest req, HttpServletResponse res)
                            throws ServletException, IOException {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><big>I'm a servlet!!</big></html>");
          }
        }

    sudo javac -cp /usr/share/tomcat7/lib/servlet-api.jar /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage/*.java

It's necessary to restart Tomcat after changing .class files:

    sudo service tomcat7 stop
    ps -aux | grep tomcat       (to ensure that the service has really stopped)
    sudo service tomcat7 start

Check browser:

        http://localhost:8080/my_webapp/my_servlet

Check errors (replace XX-XX by the current date): 
        
        sudo tail -n 200 /var/lib/tomcat7/logs/localhost.2018-XX-XX.log
        sudo tail -n 200 /var/lib/tomcat7/logs/catalina.out

It'ss useful to open a dedicated terminal and check errors continuously:

        sudo tail -f 200 /var/lib/tomcat7/logs/localhost.2018-XX-XX.log

Troubleshooting: Sometimes you get a "java.lang.ClassNotFoundException: mypackage.MyServlet" because Tomcat wasn't properly restarted. Ensure that Tomcat really stopped before starting it again (stop it again and do a ps -aux | grep tomcat) and kill the process otherwise. 


## 2 Lab assignment: Creating your own car rental web page (this time with servlets).

You have to program a web application that does exactly the same as in session 1 (CGIs) but this time using Tomcat and servlets. You will create a simple car rental web page. It will consist in two functionalities:

- Request a new rental: A form to enter a new rental order. Input fields will include the car maker, car model, number of days and number of units. If all data is correct the total price of the rental will be returned to the user along with the data of the requested rental.
 
- Request the list of all rentals: A form asking a password (as only the administrator can see this information) that will return the list of all saved rental orders. 

Both functionalities will consist in a request form plus a response page. To make it simple it's recommended that the request forms are static HTML pages and the responses are HTML dynamically generated from the servlets. For simplicity, in case of invalid input we will not show the request form again (though you can do it if you want).

In order to keep the rentals data (to be able to list them) you will need to save the data to the disk. A single text file where each line represents a rental will be enough (though not in a real scenario). We recommend you using JSON for writing/reading rental orders to disk. We have included json-simple-1.1.1.jar (http://www.mkyong.com/java/json-simple-example-read-and-write-json/).


### 2.1 Install the provided sources
In order to help you, some files are provided:

- An HTML index file: carrental_home.html
- A rental request HTML form: carrental_form_new.html (it calls CarRentalNew.java)
- A rentals list request HTML form: carrental_form_list.html (it calls CarRentalList.java)
- Two servlets (partially programmed): CarRentalNew.java and CarRentalList.java.
- A JSON library: json-simple-1.1.1.jar.

NOTE: It's not compulsory to use these files within the solution (you may, for instance, prefer to generate the forms dynamically from the servlets).

In order to install the provided files perform the following steps:

Download the files:

    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p2_servlets/carrental_home.html
    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p2_servlets/carrental_form_new.html
    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p2_servlets/carrental_form_list.html
    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p2_servlets/CarRentalNew.java
    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p2_servlets/CarRentalList.java
    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p2_servlets/json-simple-1.1.1.jar

Copy the files to Tomcat and compile the servlets:
    
    sudo cp carrental_home.html /var/lib/tomcat7/webapps/my_webapp/
    sudo cp carrental_form_new.html /var/lib/tomcat7/webapps/my_webapp/
    sudo cp carrental_form_list.html /var/lib/tomcat7/webapps/my_webapp/
    sudo cp CarRentalNew.java /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage
    sudo cp CarRentalList.java /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage
    sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF/lib
    sudo cp json-simple-1.1.1.jar /var/lib/tomcat7/webapps/my_webapp/WEB-INF/lib        
    sudo javac -cp /usr/share/tomcat7/lib/servlet-api.jar:/var/lib/tomcat7/webapps/my_webapp/WEB-INF/lib/json-simple-1.1.1.jar /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage/*.java

Add two new servlet definitions to web.xml:

        sudo vi /var/lib/tomcat7/webapps/my_webapp/WEB-INF/web.xml

        <web-app>
            <servlet>
              <servlet-name>new</servlet-name>
              <servlet-class>mypackage.CarRentalNew</servlet-class>
            </servlet>
            <servlet-mapping>
              <servlet-name>new</servlet-name>
              <url-pattern>/new</url-pattern>
            </servlet-mapping>
            <servlet>
              <servlet-name>list</servlet-name>
              <servlet-class>mypackage.CarRentalList</servlet-class>
            </servlet>
            <servlet-mapping>
              <servlet-name>list</servlet-name>
              <url-pattern>/list</url-pattern>
            </servlet-mapping>
        </web-app>

        sudo service tomcat7 stop
        sudo service tomcat7 start

Check the following link and its sublinks:
	
	http://localhost:8080/my_webapp/carrental_home.html

Now add the necessary code to CarRentalNew.java and CarRentalList.java to make the application work properly.

## ANNEX 1. With Docker (in your laptop)

If you have Docker installed in your laptop you can perform the previous steps over a clean Ubuntu container this way:

    docker run -it --name pti_p2 -p 8080:8080 ubuntu bash

Within the container you will do some things in a different way:

    - Don't use 'sudo'. 
    - Install vim and git.
    - Install default-jdk (apt-get install default-jdk) instead of Oracle's Java. 
    - service tomcat7 stop may not work properly within the container. Kill the process manually.

If you want to use Docker at the FIB's teaching lab take a look to the ANNEX 3 of the first lab session (CGIs).

## ANNEX 2. Advanced Tomcat configuration (not necessary to complete this lab)

Open ports for external access with:

    sudo ufw allow 8080/tcp
    sudo ufw allow 8443/tcp

Enabling webapp deployment with the manager:

    sudo vi /etc/tomcat7/tomcat-users.xml
        <role rolename="manager-gui"/>
        <role rolename="admin-gui"/>
        <user username="john" password="1234" roles="manager-gui,admin-gui"/>

Check manager with: http://localhost:8080/manager

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

    


