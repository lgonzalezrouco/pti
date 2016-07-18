# Making a RESTful JSON API in Go

##1. Introduction

The goal of this session is to create a dynamic web page using the Apache HTTP Server and CGIs. The description of the web page to develop is provided in section 3. You can choose the programming language 
you'd like to use (C, Python, Perl, PHP, etc.) and you can install and configure the Apache Server the way you want. However, in order to help you, we provide an example using Python, and, in Section 2, we explain one possible way to install and configure the Apache HTTP Server.


##2. Setup

###2.1 Booting the machine 

Conventional room: Select a Linux image and login with your credentials.

Operating Systems room: Select the latest Ubuntu imatge (e.g. Ubuntu 14) with credentials user=alumne and pwd:=sistemes

###2.2 Prerequisites

It's not indispensable but strongly recommended that you have git installed. If not, for a Linux machine just do:

    sudo apt-get install git

It would be also good if you have an account in any git-compliant hosting service such as GitHub or Bitbucket.

###2.3 Install Go

Download Go from https://golang.org/dl/ (>80 MB !)

###2.4 Setup a directory hierarchy 

(check [this](https://golang.org/doc/code.html) for more info in how to write Go code)

Create a directory to contain your golang workspace (e.g. $HOME/go) and the examples of this session ($HOME/go/src/examplesGo): 

    cd
    mkdir $HOME/go
    mkdir $HOME/go/src
    mkdir $HOME/go/src/examplesGo

Set the GOPATH environment variable to point to that location

    export GOPATH=$HOME/go

It is recommended that you create a git repository for the code of this session this way:
    
    cd $HOME/go/src/examplesGo
    git remote add origin https://github.com/YOUR_GITHUB_USER/examplesGo.git
    git push -u origin master

  
#### 2.3.1 Static html page
    
A RESTful API is a specific type of web (HTTP-based) service. Let's start by programming a basic web server with Go:   

Create a directory for this program:

    mkdir $HOME/go/src/examplesGo/webserver

Edit $HOME/go/src/examplesGo/webserver/webserver.go

    package main

    import (
        "fmt"
        "html"
        "log"
        "net/http"
    )

    func main() {
        http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
            fmt.Fprintf(w, "Hello, %q", html.EscapeString(r.URL.Path))
        })

        log.Fatal(http.ListenAndServe(":8080", nil))

    } 

Build (will create an executable within $HOME/go/bin/webserver):

    go install examplesGo/webserver

Run:

    $HOME/go/bin/webserver

test in browser: http://localhost:8080
    

   

#### 2.3.2 Dynamic content with a CGI (a Python script)

enable CGIs:

    sudo a2enmod cgi
    sudo service apache2 restart    

copy examples:
        
    sudo cp *.py /usr/lib/cgi-bin
    sudo chmod 055 /usr/lib/cgi-bin/*

test in browser: http://localhost/cgi-bin/example_cgi.py

###2.5 Form+CGI template

test in browser: http://localhost:80/p1/formulari.html and submit. The script that is processing the request is /usr/lib/cgi-bin/template_cgi.py. 

###2.6 Troubleshooting

Check errors with:
    cat /var/log/apache2/error.log

Check config at:

    cat /etc/apache2/sites-enabled/000-default
    cat /etc/apache2/apache2.conf 

NOTE: Restart apache after changing the configuration with:

    sudo service apache2 restart

Apache documentation at http://httpd.apache.org/docs/2.2/

    
##3. Creating your own car rental web page 

As an example CGI you will create a simple car rental web page. It will consist in two functionalities:

- Request a new rental: A form to enter a new rental order. Input fields will include the car maker, car model, number of days and number of units. If all data is correct the total price of the rental will be returned to the user along with the data of the requested rental.
 
- Request the list of all rentals: A form asking a password (as only the administrator can see this information) that will return the list of all saved rental orders. 

Both functionalities will consist in a request form plus a response page. In case of invalid input data the request form will be shown again but alerting about the error. While the request forms may be static HTML pages, it is better to generate them from CGIs (this way they can show error messages). 

In order to keep the rentals data (to be able to list them) you will need to save the data to the disk. A single text file where each line represents a rental will be enough (though not in a real scenario). 

NOTE: Files carrental_home.html, carrental_form_new.html and carrental_form_list.html show a possible user interface. It's not compulsory to use these files within the solution (you may generate the forms dynamically from the CGIs).

###3.1 Directory structure

There are several ways to solve the problem and you are free to choose the one you prefer. A simple approach would be to program two CGIs:

    /usr/lib/cgi-bin/new.py
    /user/lib/cgi-bin/list.py

Each one will:

    1) If there's no input data just generate the form.
    2) If there's input data validate it and return the result (some info or a message error plus the form again)

You don't need to program the CGIs from scratch, you replicate template_cgi.py.

In order to write/read the orders to a disk file you can use a comma-separated values format (CSV) and the csv python module. Take a look to ANNEX2 for an example.


