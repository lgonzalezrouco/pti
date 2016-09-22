# XML Processing with Java

## 1. Introduction

The purpose of this assignment is to learn how to process XML documents with Java.

## 2. Setup

### 2.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14)

    user: alumne
    pwd: sistemes


### 2.2 Download the example and the libraries

    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p3_xml/Example.java
    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p3_xml/example.xml
    wget https://gitlab.fib.upc.edu/pti/pti/raw/master/p3_xml/example.xslt

    wget http://www.jdom.org/dist/binary/archive/jdom-1.0.zip
    unzip jdom-1.0.zip

    wget http://apache.rediris.es/xalan/xalan-j/binaries/xalan-j_2_7_2-bin-2jars.tar.gz
    tar xzvf xalan-j_2_7_2-bin-2jars.tar.gz

### 2.3 Set the Java classpath and run the example

Set the Java classpath this way:

    export CLASSPATH=./xalan-j_2_7_2/xalan.jar:./xalanj_2_7_2/xercesImpl.jar:./jdom-1.0/build/jdom.jar:.

Now build the example:

    javac Example.java

And run it:

    java Example

## 3 Lab assignment 

You have to program an console application with the following behavior:

### 3.1 reset

Command:

    java CarRental reset

The application will create a new XML document (in memory) with the following structure:
    
    <?xml version="1.0" encoding="UTF-8"?>
    <carrental>
    </carrental>

Once created, the application will save it to a file carrental.xml. If the file already exists, its previous contents will be lost.

### 3.2 new

Command:

    java CarRental new

The application will 1) Ask the user (through the console) the data of a new rental (car model, etc.); 2) Read the carrental.xml XML document into memory; 3) Add a new element to the document with the following structure :
    
    <?xml version="1.0" encoding="UTF-8"?>
    <carrental>
        <car vin="123fhg5869705iop90">
          <make>Toyota</make>
          <model>Celica</model>
          ...
        </car>
    </carrental>

And 4) the application will save new document including the new rental into carrental.xml.

### 3.3 list

Command:

    java CarRental list

The application will read the carrental.xml XML document into memory and pretty print it to the console.

### 3.4 xslt

Command:

    java CarRental xslt

The application will read the carrental.xml XML document into memory, transform it into HTML with an XSLT stylesheet and print it to the console. You can reuse the stylesheet from the example (car.xsl), but you would need to change it




