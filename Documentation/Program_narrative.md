### PROGRAM NARRATIVE

-----------

#### Description of the appliaction
* Technical details
  * The application has been coded using Maven on the Raspberry Pi. 
  * The application consists of MQTT clients connecting to the HiveMQ broker running on the HiveMQ cloud to publish and subscribe to messages.
* General Idea
  * A GUI gets launched upon completing valid information (login) to display sensor datas to and from all users running the program.

-----------

#### List of features/functionalities of the application
* The user subscribes to the relevent topics, publish their public key and retrieve the public keys published by the other users on the topic.
  * data messages are signed before publishing and verified before retrieving. If the signature is not valid, they are discarded.
  * certificates do not get signed/verified
* Once data gets retrieved, the GUI gets updated to display the latest information
  * each member have access to data of the other connected users
  * the GUI is made of 13 tiles total.
    * 1 tile x 3 users => DHT temperature sensor display (temperature + timestamp)
    * 1 tile x 3 users => DHT humidity sensor display (humidity + timestamp)
    * 1 tile x 3 users => Doorbell sensor display (timestamp)
    * 1 tile x 3 users => Motion sensor Camera display (image + timestamp)
      * image and timestamp get updated everytime motion is detected
    * 1 tile => Exit button to exit the program
  * Each row corresponds to one specific user


-----------

#### Explanation of specific design choices
* Sensors
  * what: Have and Abstract class for the sensors, then have each sensor extend that class
    * why: in order to avoid code redundancy since the callProcess method is the same for all sensors
* MQTT
  * what: hardcode allowed usernames
    * why: 
      * allows access all necessary data only.
      * only send the data to verified users 
      * only receive the data that we want and not everything to avoid flooding
      * stop from retrieving any faulty data from unwanted people
* JavaFX
  * what: Row object has been created with public Tiles
    * why: In order to minimize code redundancy (3 x row) and have access to the tiles from the outside and allow updating of the displayed values.
  * what: GUI application contains and launches Console application
    * why: to avoid having to run 2 separate programs (one console & one GUI), this way, the GUI launches right away once valid information has been entered to the console.

-----------

#### Highlight required functionalities that weren’t implemented
All functionalities have been implemented. 
However, due to unfortunate circumstances, the whole project was made by 2 members instead of 3.
Some features might not be of the best quality due to the lack of resources (members)

-----------

#### Contributors
Matteo Robidoux
Rim Dallali

*Borrowed and modified code from Carlton Davis*