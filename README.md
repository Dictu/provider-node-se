# provider-node SE
Toop-node acting as TOOP provider for Sweden
TOOP Provider connects to the Swedish Chamber of Commerce test service: https://ssbtgu-accept2.bolagsverket.se/ssbtgu-dft-web-2/SsbtServicePorts/Test/SsbtTestService?wsdl

# First of all #
This project depends on:
- JDK 8 
- Apache Maven 3x (https://maven.apache.org/)
- Lombok (https://projectlombok.org)

All three need to be installed, configured and running properly.

Note that 8 is in use here due to the incompatibility with Java9 for the needed J2EE WebService dependencies (i.e. javax.ws).

# Country specific implementation #
The way forward would be to implement the eu.toop.node.provider.ProviderService interface which would need to use a country specific Chamber of Commerce service much like the eu.toop.node.service.ChamberOfCommerceProviderService

# Check out #
Use GIT clone to get a local copy of this repo.

# Configuration #
Before running check server.port inside:
./src/main/resources/application.properties
Also outgoing-proxy can be configured here (proxy is ignored when no value is given)

The application.properties can be placed next to the jar and it will be picked up automatically.

# Building #
You will need to build the toopapi project first!
Build using:
mvn clean install

# Running #
Run the application inside ./target/
java -jar <jar-name>.jar

# OR #
mvn spring-boot:run

# Browse to # 
TOOP Provider URL:
http://localhost:<server.port>/toopnode/provider/provide?id=818511752

 