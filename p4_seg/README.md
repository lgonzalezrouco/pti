# HTTP Security

## 1. Introduction

Note: This file only contains some clarifications about the .pdf document. 

## 2. Tasks

### 2.1 HTTPServer

Download the HTTPServer code and execute it:

	mkdir p4
	d p4
	wget http://docencia.ac.upc.es/FIB/grau/PTI/lab/_seg/pti-p6-codigo.tar.gz
	tar xzvf pti-p6-codigo.tar.gz
	javac *.java
	java -cp . HTTPServer &

    [1] 13795
    HTTPServer version 1.0
    HTTPServer is listening on port 8000.

Test with your browser: http://localhost:8000. The server sends the file index.html to the browser.

Test with your own client: 

	java -cp . Browser http://localhost:8000

### 2.1 Basic Authentication without SSL (only with a commercial browser as client)

1. Modify the processGetRequest method within HTTPServer.java: 
 
	 void processGetRequest(HTTPRequest request,BufferedOutputStream outStream)
	   throws IOException {  
	    if (!request.checkBasicAuthentication())
	        sendBasicAuthenticationUnauthorized(outStream);
	    else {
	      ...the previous code...
	    }
	 }

2. Update the following within the HTTPRequest class within HTTPServer.java: 

    Vector<String> lines = new Vector<String>(); 
 
3. Add to the HTTPRequest class within HTTPServer.java: 

    public boolean checkBasicAuthentication() {    
        for(String line : lines){        
            if(line.equalsIgnoreCase("Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")) 
                return true;        
        }
        return false;    

4. Add to the HTTPRequest class within HTTPServer.java:         
               
     void sendBasicAuthenticationUnauthorized(BufferedOutputStream out) {
      try {   
       out.write("HTTP/1.1 401 Unauthorized\n".getBytes());
       out.write("WWW-Authenticate: Basic realm= WallyWorld\n".getBytes());    
       out.write("Content-Type: text/html\r\n\r\n".getBytes());
       out.flush();
       out.close();
       System.out.println("Response sent");   
      }catch(Exception e){   
       e.printStackTrace();
      }
     }
      
5 Test it   

Open with a browser http://localhost:8000 and type: username="Aladdin", pwd="open sesame" 

COMMENT: Don't need to apply basic authentication to Browser.java.
   



