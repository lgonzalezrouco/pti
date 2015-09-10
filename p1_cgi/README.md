# Common Gateway Interface (CGI)

##1. Apache and CGIs. Quick Start

###1.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14). 

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
    sudo service apache2 restart    

copy examples:
	    
    sudo cp *.py /usr/lib/cgi-bin
    sudo chmod 055 /usr/lib/cgi-bin/*

test in browser: http://localhost/cgi-bin/example_cgi.py

###1.5 Form+CGI template

test in browser: http://localhost:80/p1/formulari.html and submit. The script that is processing the request is /usr/lib/cgi-bin/template_cgi.py. 

###1.6 Troubleshooting

Check errors with:
    cat /var/log/apache2/error.log

Check config at:

    cat /etc/apache2/sites-enabled/000-default
    cat /etc/apache2/apache2.conf 

NOTE: Restart apache after changing the configuration with:

    sudo service apache2 restart

Apache documentation at http://httpd.apache.org/docs/2.2/

    
##2. Creating your own car rental web page 

As an example CGI you will create a simple car rental web page. It will consist in two functionalities:

- Request a new rental: A form to enter a new rental order. Input fields will include the car maker, car model, number of days and number of units. If all data is correct the total price of the rental will be returned to the user along with the data of the requested rental.
 
- Request the list of all rentals: A form asking a password (as only the administrator can see this information) that will return the list of all saved rental orders. 

Both functionalities will consist in a request form plus a response page. In case of invalid input data the request form will be shown again but alerting about the error. While the request forms may be static HTML pages, it is better to generate them from CGIs (this way they can show error messages). 

In order to keep the rentals data (to be able to list them) you will need to save the data to the disk. A single text file where each line represents a rental will be enough (though not in a real scenario). 

NOTE: Files carrental_home.html, carrental_form_new.html and carrental_form_list.html show a possible user interface. It's not compulsory to use these files within the solution (you may generate the forms dynamically from the CGIs).

###2.1 Directory structure

There are several ways to solve the problem and you are free to choose the one you prefer. A simple approach would be to program two CGIs:

    /usr/lib/cgi-bin/new.py
    /user/lib/cgi-bin/list.py

Each one will:

	1) If there's no input data just generate the form.
	2) If there's input data validate it and return the result (some info or a message error plus the form again)





