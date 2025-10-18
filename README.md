# Social Services online

Build an application that shows services in Berlin (and anywhere else) as easy and supportive as possible.

## First steps
Make a copy of env.template with the most important env vars:

    cp env.template .env

To get backend application running, get a working application.yml:

    cd src/main/resources
    cp application.yml.template application.yml

To get a basic setup of entries for the database, do... 

    cd docker/socialservices/input
    cp Beratungsstellen_TEMPLATE.tsv Beratungsstellen.tsv

To get more and better data, please ask your fellows. 

Start directly with [SocialServicesApplication.java](src/main/java/de/hoogvliet/socialservices/SocialServicesApplication.java)

## More Information
* [Development Information](doc/development.md)
* This application uses Openstreetmap and https://leafletjs.com/ 