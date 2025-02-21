Code illustrating the following: 
* Generation and verification of digital signature using Elliptic curve digital signature algorithm
* load the keystore provided the filename and password from user
* Extract Certificate, Public key and private key
* Display data from different sensors on the circuit to the user
* Create a connection to an MQTT client which will be used to publish/retrieve data
* Subscribe to the relevent topics to display the retrieved data
* Publish data from the sensor to the topic
* Shared data (other than certificates) is signed and verified before display
* More details on the program narrative in the "documentation" folder

### Instructions for compiling and running   
The code can be compile and run from an IDE or via command line.   

### Generate a KeyStore
cd to the directory where you want the keyStore file to be stored
execute the following command: 
###### keytool -genkey -keyalg EC -alias ALIAS -keystore ECcertif.ks
* Note: replace ALIAS with your own alias for the key. 
* When the command executes, fill in the requested information.

Execute the following command to verify the contents of the keystore
###### keytool -list -v -keystore ECcertif.ks

#### Compile and run using an IDE   
_To compile_: click the "Clean and Build Project" button.   
_To run_: click the "Run Project" button.   
   
### Compile and run using the command line   
_To compile_: 
###### mvn compile   
_To run_: 
###### mvn exec:java -Dexec.mainClass=datacomproject.mqttclientapp.JavaFX.TilesFXApp

### Verify dependencies and run tests
check owasp dependencies: 
###### mvn verify
runs tests in src/test/java: 
###### mvn test

### Contributors
Matteo Robidoux
Rim Dallali

### CREDENTIALS FOR HIVEMQ LOGIN
**make sure to copy paste the username**
username: carletondavis
password: password