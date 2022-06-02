# batch-06-book-shop-demo-team-a
## Project Setup

### Run build
./gradlew build

### Run Application locally
Application expects to run with postgres database, install it if not available.

#### Install Postgres
brew install postgresql@14
(Enter password as postgres for the command below)
createuser -P -s postgres

#### Create Database
createdb bookshop-db

#### Add Test Data to DB
psql bookshop-db < src/main/resources/data_scripts/Insert_Books.sql
psql bookshop-db < src/main/resources/data_scripts/Insert_Reviews.sql

#### Run application
java -jar build/libs/bookshop-0.0.1-SNAPSHOT.jar
