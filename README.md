# NBM Center

This is an application that will host Netbeans Plugins for Netbeans it's self or for a Netbeans platform application.

## Getting started
- Download the latest jar file
- From the command line type the following:

    `> java -jar nbm-center-<version>.jar`

This will setup a H2 database and start the server and you are ready to start uploading your modules to the center.

## Available Endpoints
- POST to `/module` - uploads a NBM to be saved or updated in the center
- GET from `/module/{id}.nbm` - streams the NBM file contents
- DELETE from `/module/{id}.nbm` - removes a NBM from the center
- GET from `/catalog.xml` - streams the catalog XML used by Netbeans or a platform application
- GET from `/catalog.xml.gz` - streams the catalog XML compressed using gzip this is also can be used by Netbeans or a platform application

### Note: This application was built using Drop Wizard so you also have access to the default endpoints provided by the framework.

## FAQ
- Can I change the type of database (MySQL, Oracle, etc)?
    - To do this all you need to do is put your JDBC driver in the classpath and create your own configuration file.
