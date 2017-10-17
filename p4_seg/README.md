# HTTP Security

## 1. Introduction

Note: This file only contains some clarifications about the .pdf document. 

## 2. Tasks

## 2.1 Basic Authentication

It is necessary to add to the processGetRequest(...) method from the HTTPServer class the following logic:

	1) Check if the browser has sent the line "Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==": You can do this by reading the class variable "lines" from the HTTPRequest object (first parameter of processGetRequest). 

	for(String line : request.lines){
		if(line.equalsIgnoreCase("Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")) {
			//found it!
			break;
		}
	}

	
	2) If not, send to the browser the basic authentication challenge:

	   out.write("HTTP/1.1 401 Unauthorized\n".getBytes());
	   out.write("WWW-Authenticate: Basic realm= WallyWorld\n".getBytes());
	   out.write("Content-Type: text/html\r\n\r\n".getBytes());
	   out.flush();
	   out.close();




