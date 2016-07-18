# Making a JSON API in Go

##1. Introduction

A web service is a generic term for a software function that is accessible through HTTP. Traditional web services usually relied in support protocols for data exchange (e.g. SOAP) and service definition (WSDL). However, nowadays the paradigm has evolved to a simplified form, usually called web APIs. Web APIs normally rely only in plain HTTP plus JSON for serializing the messages. Their design is usually influenced by the [REST architectural style](https://en.wikipedia.org/wiki/Representational_state_transfer), though the most part of web APIs do not really comply with REST principles. 



A Web API is a development in Web services where emphasis has been moving to simpler representational state transfer (REST) based communications

The goal of this session is to create basic JSON API with golang.  



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

Create a directory to contain your golang workspace (e.g. $HOME/go): 

    cd
    mkdir $HOME/go
    mkdir $HOME/go/src

Set the GOPATH environment variable to point to that location

    export GOPATH=$HOME/go

It is recommended that you create a git repository (e.g. "pti_golang") for the code of this session within $HOME/go/src. If you have a github account you can do it directly from the command line:

    curl -u 'YOUR_GITHUB_USER' https://api.github.com/user/repos -d '{"name":"pti_golang"}'
    cd $HOME/go/src
    git clone https://github.com/YOUR_GITHUB_USER/pti_golang

Let's write and test a first program in golang:

    cd $HOME/go/src/pti_golang
    mkdir hello
    cd hello
    wget https://raw.githubusercontent.com/rtous/pti/master/goREST/src/hello/hello.go
    go install pti_golang/hello
    $HOME/go/bin/hello

Don't forget to commit your changes

    cd $HOME/go/src/pti_golang
    git add .
    git commit -m "first commit"
    git push

  
#### 2.4 A simple web server
    
A RESTful API is a specific type of web (HTTP-based) service. Let's start by programming a basic web server with Go:   

Create a directory for this program:

    mkdir $HOME/go/src/pti_golang/webserver

Edit $HOME/go/src/pti_golang/webserver/webserver.go

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

    go install pti_golang/webserver

Run:

    $HOME/go/bin/webserver

test in browser: http://localhost:8080
    
#### 2.5 URL routing
    
An API exposes different functionalities. These functionalities are accessed through different URL routes or endpoints. We need a mechanism that let us map URL routes into calls to different functions in our code. The standard golang library offers a [too complex routing mechanism](https://husobee.github.io/golang/url-router/2015/06/15/why-do-all-golang-url-routers-suck.html), so we will use an external library for that (mux router from the Gorilla Web Toolkit):

    go get github.com/gorilla/mux

Let's modify our webserver.go to add some routes:

    package main

    import (
        "fmt"
        "log"
        "net/http"
        "github.com/gorilla/mux"
    )

    func main() {

    router := mux.NewRouter().StrictSlash(true)
    router.HandleFunc("/", Index)
    router.HandleFunc("/endpoint/{param}", endpointFunc)

    log.Fatal(http.ListenAndServe(":8080", router))
    }

    func Index(w http.ResponseWriter, r *http.Request) {
        fmt.Fprintln(w, "Service OK")
    }

    func endpointFunc(w http.ResponseWriter, r *http.Request) {
        vars := mux.Vars(r)
        param := vars["param"]
        fmt.Fprintln(w, "You are calling with param:", param)
    }

Rebuild, run and open http://localhost:8080/endpoint/1234 in your browser.
   

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


