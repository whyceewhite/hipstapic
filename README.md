# hipstapic

## INSTALLATION

1. Get Git.

1. [Install Brew](http://brew.sh/).

    Or, if Brew is already installed, update it.

    ```
    $ brew update
    ```

1. Install a web container such as JBoss's WildFly AS.

    ```
    $ brew install wildfly-as
    ```

1. [Install MongoDB](http://docs.mongodb.org/manual/tutorial/install-mongodb-on-os-x/).

    ```
    $ brew install mongodb
    ```

1. [Install Node.JS](https://nodejs.org/download/).

    Or, if Node is already installed, update it:

    ```
    $ npm install -g npm
    ```

1. [Install Bower](http://bower.io/#install-bower).

    ```
    $ npm install -g bower
    ```

## SETUP

### Install Javascript Libs for hipstapic-web via Bower

The javascript libraries for the hipstapic-web component are managed through Bower. When initially setting up the hipstapic-web project, you will need to issue a `bower install` command to obtain the libraries.

    $ cd hipstapic-web/src/main/webapp
    $ bower install

### Configure DEBUG Logging for WildFly AS

Modify the `wildfly-as/libexec/standalone/configuration/standalone.xml` file. In the `urn:jboss:domain:logging:2.0` subsystem, change the handlers' level to DEBUG.
Add a logger entry for the projects by specifying the `ology` package.

    <logger category="ology">
       <level name="DEBUG"/>
    </logger>



## RUNNING THE APP FOR DEVELOPMENT USING NODEJS

1. Change into the project's `webapp` directory.

    ```
    $ cd hipstapic/hipstapic-web/src/main/webapp
    ```

1. Start Node.js.

    If starting for the first time then run the npm install to grab the dependencies referenced in the package.json file.

    ```
    $ sudo npm install
    ```

    ```
    $ npm start
    ```

1. Go to http://localhost:9000.


## RUNNING THE APP FOR DEVELOPMENT USING JBOSS

1. Start mongodb.

    ```
    $ sudo mongod --config /usr/local/etc/mongod.conf
    ```

1. Start JBoss.

    ```
    $ sudo /usr/local/opt/wildfly-as/libexec/bin/standalone.sh
    ```

1. Copy the WAR to the JBoss deployment directory to hot deploy it.

    ```
    $ sudo cp hipstapic/hipstapic-web/target/hipstapic-web.war /usr/local/opt/wildfly-as/libexec/standalone/deployments

    ```

    Monitor the log to determine the status of the deployment by tailing the `server.log`:

    ```
    $ tail -f /usr/local/opt/wildfly-as/libexec/standalone/log/server.log
    ```

    The web address is:

    ```
    http://localhost:8080/hipstapic-web
    ```

1. To start a simple web server for hosting the images, then go to the directory where the images are located and run:

    ```
    $ python -m SimpleHTTPServer <port>
    ```

    Where `<port>` is a port that is not being used (i.e., 9028).
