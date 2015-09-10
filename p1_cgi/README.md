# Common Gateway Interface (CGI)

##1. Quick Start

###1.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14)

    user: alumne
    pwd: sistemes


###1.2 Install Apache 

Open a terminal (CTRL+ALT+T) and type:

    sudo apt-get update
    sudo apt-get install apache2 #password = sistemes

Check with version is installed:

    apachectl -V

test in browser: http://localhost:80


###1.3 Install examples

Install git (if necessary):

    sudo apt-get install git


Download the examples:

    cd $HOME       
    git clone https://github.com/rtous/pti.git
    cd pti/p1_cgi
    ls
  
#### 1.3.1 Static html page
    
    sudo mkdir /var/www/html/p1
    sudo cp *.html /var/www/html/p1

test in browser: http://localhost:80/p1/example.html

#### 1.3.2 Dynamic content with a CGI (a Python script)

enable CGIs:

    sudo a2enmod cgi

copy examples:
	    
    sudo cp *.py /usr/lib/cgi-bin
    sudo chmod 055 /usr/lib/cgi-bin/*

test in browser: http://localhost/cgi-bin/example_cgi.py

###1.5 Form+CGI template

test in browser: http://localhost:80/p1/formulari.html and submit. The script that is processing the request is /usr/lib/cgi-bin/template_cgi.py. 
Starting for it you can complete the lab assignment. 


###1.6 Troubleshooting

Check errors with:
    cat /var/log/apache2/error.log

Check config at:

    cat /etc/apache2/sites-enabled/000-default
    cat /etc/apache2/apache2.conf 

Apache documentation at http://httpd.apache.org/docs/2.2/

    


