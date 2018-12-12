# BitBay hourly market rates

Service publish bitabay market rates hourly.

Branch 'master' has in-memory infrastucture.

Branch `infra/sql` infrastucture implemented with MariaDB for domain and Redis for cache and 24 hours backup.

## How to run

Application requires docker compositor and free 8080 port.

 1. Start containers `docker-compose up`
 2. Execute command `java -jar target/marketrates-0.0.1-SNAPSHOT.jar`
  
NOTE: When You run first time, it takes ~30 minutes when application will get ready. In this time app download data from bitbay service. You can work with app during data is downloaded, but You should be aware that database is not fully sycnchronized.
 
## Usage example

Get rates from GNT-PLN market at 2018-09-29 and 6,12,16 hours:
`http://localhost:8080/markets/GNT-PLN/rates/2018-09-29?hours=6&hours=12&hours=16`

## Schemas

### BitBay integration

![Integration schema](https://jgprogram.files.wordpress.com/2018/11/bitbayintegrationschema.png)

### Architecture

![Architecture schema](https://jgprogram.files.wordpress.com/2018/11/architectureschema.png)

## Author
- Jacek Gzel
