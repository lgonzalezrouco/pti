# Common Gateway Interface (CGI)

##1. Quick Start

###1.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14)

user: alumne
pwd: sistemes


###1.2 Install Apache 

Open a terminal (CTRL+ALT+T) and type

    sudo apt-get install apache2 #password = sistemes

test in browser: http://localhost:80


###1.3 Install examples

    cd $HOME
    mkdir p1
    cd p1 

#### 1.3.1 Static html page

    wget http://docencia.ac.upc.es/FIB/grau/PTI/lab/_cgi/pti-html.tar.gz
    tar -xvf cgi-examples.tar 
    sudo mkdir /var/www/p1
    sudo cp html/* /var/www/p1

test in browser: http://localhost:80/p1/formulari.html


#### 1.3.2 Dynamic content with a CGI (a Python script)

    wget http://docencia.ac.upc.es/FIB/grau/PTI/lab/_cgi/cgi-examples.tar
    tar -xvzf pti-html.tar.gz
    sudo cp cgi-bin/jj-python.py /usr/lib/cgi-bin
    sudo chmod 055 /usr/lib/cgi-bin/*

test in browser: http://localhost/cgi-bin/jj-python.py


###1.4 Troubleshooting

Check errors with:
    cat /var/log/apache2/error.log

Check config at:
    cat /etc/apache2/sites-enabled/000-default
    cat /etc/apache2/apache2.conf 

    


