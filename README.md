# Social Services online

Build an application that shows services in Berlin (and anywhere else) as easy and supportive as possible

## Backend:
* Spring Boot with RestService
* Data are stored statically at the moment, it's the easiest way
* /social delivers a list of saved locations
* I am using actuator for some simple monitoring issues


## Frontend:
* next.js
* React

### PostgreSQL-DB via docker
Install postgres CLIENT (!) on your workstation. You will need this 
for checking the database. Check, if your IDE offers a possibility as well.
IntelliJ does ;-)

    docker run --name socialdb -e POSTGRES_USER=social -e POSTGRES_PASSWORD=social2024 -e POSTGRES_DB=socialdb -p 5432:5432 -d postgres

To check, id this db isr reachable, use

    psql -h localhost -U social socialdb

We need prisma for that:

    cd nextjs-dashboard
    npm install prisma @prisma/client
    npx prisma init

Attention! If this installation fails, please check the node version. There is an issue
with node@23 (see https://github.com/prisma/prisma/issues/25463). Downgrade to node@22.

    CREATE TABLE IF NOT EXISTS revenue (month VARCHAR(4) NOT NULL UNIQUE, revenue INT NOT NULL);
    CREATE TABLE IF NOT EXISTS customers (id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, image_url VARCHAR(255) NOT NULL);
    CREATE TABLE IF NOT EXISTS invoices (id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, customer_id UUID NOT NULL, amount INT NOT NULL, status VARCHAR(255) NOT NULL, date DATE NOT NULL);
    CREATE TABLE IF NOT EXISTS users ( id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, name VARCHAR(255) NOT NULL, email TEXT NOT NULL UNIQUE, password TEXT NOT NULL);


### Monitoring

    http://localhost:8080/actuator/health

    http://localhost:8080/actuator/metrics
Is activated, it's the easiest way to see, if metric does work and how long this app needs. I don't know when this
application fails with this in-memory-approach, so it's better to measure as soon as possible.

There is no problem with 50 entries ;-)