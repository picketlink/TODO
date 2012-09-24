AeroGear TODO Application
=========================

What is it?
-----------

The _AeroGear TODO Application_ currently contains one client project, a web application which is already optimized for mobile! In the future it will contain different client projects for iOS, Android or Apache Cordova..

The project also contains a server side project, which offers several REST / JAX-RS endpoints.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on JBoss AS 7 or JBoss Enterprise Application Platform 6.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are
required. 

Mobile web support is limited to Android and iOS devices.  It should run on HP,
and Black Berry devices as well.  Windows Phone, and others will be supported as 
jQuery Mobile announces support.
 
With the prerequisites out of the way, you're ready to build and deploy.

System requirements
-------------------

To build the projects simply run the following command from the root directory:

    mvn clean install

This will build the _ear_ file with:
- the web application (_todo-www.war_)
- the REST endpoints (_todo-server.war_)

The application will be automaticaly deployed to **$JBOSS_HOME/standalone/deployments**

Starting the server
-------------------------

Once you have done that, you need to start the JBoss container. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat
    
Note: Adding "-b 0.0.0.0" to the above commands will allow external clients (phones, tablets, 
desktops, etc...) connect through your local network.
      
For example

    $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 

The client application will be running at the following URL <http://localhost:8080/todo-www/>.a

Login instructions
------------------

This application aims to show different

For demo purposes:

username: john
password: 123

Notes
------

This is a working progress using picketbox timed releases, for this reason add this configuration file https://gist.github.com/b8359b1b824ff8dfcf0c at your $HOME/.m2 folder. 
