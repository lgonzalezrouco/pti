# Making a JSON API in Go

##1. Introduction

A web service is a generic term for a software function that is accessible through HTTP. Traditional web services usually relied in support protocols for data exchange (e.g. SOAP) and service definition (WSDL). However, nowadays the paradigm has evolved to a simplified form, usually called web APIs. Web APIs normally rely only in plain HTTP (plus JSON for serializing the messages). Their design is usually influenced by the [REST architectural style](https://en.wikipedia.org/wiki/Representational_state_transfer), though the most part of existing web APIs do not really comply with REST principles. Nowadays, the most part of client-server systems (e.g. web applications and mobile apps) design their back end as a combination of web APIs.  

The goal of this session is to create simple web API with the Go programming language and JSON. We will not bother to follow the REST principles, so it will not be a trully RESTful API.  


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

  
## 3 A simple web server
    
A web API is a specific type of web (HTTP-based) service. Let's start by programming a basic web server with Go:   

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
    
## 4 URL routing
    
An web API exposes different functionalities. These functionalities are accessed through different URL routes or endpoints. We need a mechanism that let us map URL routes into calls to different functions in our code. The standard golang library offers a [too complex routing mechanism](https://husobee.github.io/golang/url-router/2015/06/15/why-do-all-golang-url-routers-suck.html), so we will use an external library for that (mux router from the Gorilla Web Toolkit):

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
   
##5. JSON 

Typically an endpoint has to deal with more complex input and output parameters. This is usually solved by formatting the parameters (input and/or output) with JSON. Let's modify our webserver.go to include a JSON response.

    package main

    import (
        "fmt"
        "log"
        "net/http"
        "github.com/gorilla/mux"
        "encoding/json"
    )

    type ResponseMessage struct {
        Field1 string
        Field2 string
    }

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
        res := ResponseMessage{Field1: "Text1", Field2: param}
        json.NewEncoder(w).Encode(res)
    }

Rebuild, run and open http://localhost:8080/endpoint/1234 in your browser.
   
##6. Creating your own car rental web API

As an example web API you will create a simple car rental web API. It will consist in two functionalities:

- Request a new rental: An endpoint to register a new rental order. Input fields will include the car maker, car model, number of days and number of units. If all data is correct the total price of the rental will be returned to the user along with the data of the requested rental.
 
- Request the list of all rentals: An endpoint that will return the list of all saved rental orders. 

In order to keep the rentals data (to be able to list them) you will need to save the data to the disk. A single text file where each line represents a rental will be enough (though not in a real scenario). 





