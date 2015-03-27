# hipstapic

## INSTALLATION

1. Get Git.

1. Install a web container such as JBoss.

1. [Install MongoDB](http://docs.mongodb.org/manual/tutorial/install-mongodb-on-os-x/).

1. [Install Node.JS](https://nodejs.org).

1. [Install Bower](http://bower.io/#install-bower).

    ```
    $ npm install -g bower
    ```

## SETUP

The javascript libraries for the hipstapic-web component are managed through Bower. When initially setting up the hipstapic-web project, you will need to issue a `bower install` command to obtain the libraries.

    $ cd hipstapic-web/src/main/webapp
    $ bower install

## RUNNING THE APP FOR DEVELOPMENT USING JBOSS

1. Start mongodb.

    ```
    $ sudo mongod
    ```

1. Start JBoss.

    ```
    $ sudo /usr/local/jboss/bin/standalone.sh
    ```

1. Copy the WAR to the JBoss deployment directory to hot deploy it.

    ```
    $ sudo cp hipstapic/hipstapic-web/target/hipstapic-web.war /usr/local/jboss/standalone/deployments
    ```

    Monitor the log to determine the status of the deployment by tailing the `server.log`:

    ```
    $ tail -f /usr/local/jboss/standalone/log/server.log
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
