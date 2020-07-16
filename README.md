# IoT For technicians

Live Objects is a Saas solution to collect data from sensors, supporting several types of connectivity (LoRa, SMS,MQTT).
Customers can manage a sensor fleet, use data from Live Objects, process data through other business applications.

The purpose of this app is to provide support for technicians during the installation of sensors already provisioned, in the Live Objects platform.
With your Live Objects user account, this application offers various functionalities:
* find device in Live Objects, combinating 4 filters (Device ID, Device Name, Group, Status Interface)
* find a device scanning a QRcode or Barcode
* edit sensor information (Name, Properties, Tags)
* take measures of the signal level (LoRa only)
* validate reception of payload messages
* assign a static location to a sensor
* visualize the static or dynamic location of a sensor into a map according to the user position
* display activity logs (LoRa only)

This app uses the API exposed by the Live Objects platforms.
All the features are customizable.

## Features
IoT For technicians displays how are handled the following data:

System Permissions:

* Internet
* Camera
* Location


## Requirements to run

* Android 6.0+

## Requirements to compile

* Android studio
* Android SDK version min. 23

### Download & compile

* Open Android studio and in starting window select:
* Checkout our project from Version Control -> Git
* Put git@github.com:DatavenueLiveObjects/Android-mobile-application-in-link-with-Live-Objects-for-field-technicians.git
* Click Clone button.

After while, when project will synchronize, in main menu select Build -> Make project (Ctrl+F9).

### Configure Google Maps key
To use the Google map feature in the application we need first configure the API key for this service.
Go to the address [https://console.cloud.google.com/apis/credentials](https://console.cloud.google.com/apis/credentials) and sign in if you need.
Then click **Create Credentials** -> **API key**. After that you need to copy generated key, open file in
*src/release/res/values/google_maps_api.xml* and replace **INSERT YOUR KEY** with new generated key.
Do the same for *src/debug/res/values/google_maps_api.xml* file.


After that edit generated key, in section **Application restrictions** select "Android apps",
in section "Restrict usage to your Android apps" click "Add an item" and in "Package name" insert
application package name "com.company.iotfortechnicians". Meanwhile you need to generate certificate:

```keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android```.

Copy generated SHA1 fingerprint and put in "SHA-1 certificate fingerprint" field, and click **Done** button.

### Run application

After successfully compile, in main menu click Run -> Run (Shift+F10)

### Description of the Qrcode functionality

1. The technicians scans the QRCode with the IoT for Technicians mobile application
2. The app read the data and extracts characters. 1234567 is the Identifier
3. The app searchs  if a device is provisionned with an Device ID containing this Identifier 1234567  from the Live Objects.(Contains)
4. The app displays details from the device



**Requirements for the format of QRcode**

No specification regarding the format of the QRCode.
Minimum Size required for the QRCode: 114pxX114px (3cmx3cm)
The “IoT for Technicians” mobile application supports the qrcode and barcode format.

**Description of data embedded into the QRCode**

Data of the QRCode is composed of caracters supported by the Live Objects Portal.
2 formats of data can be embedded into a QRcode:


**1st format:  Basic Format**

The “IoT for Technicians” mobile application can extract an simple Identifier from the QRCode.
The provider generates a simple Identifier into the QRcode.

Example:

    12345678C1FE


**2st format: an Identifier at the end of a set of caracters with separator “;”**

The “IoT for Technicians” mobile application can extract from a set of caracters, an Identifier.
The identifier must be positioned at the end of the caracters with a “;” as separator.

 Example:

    Temp;V1.0;enabler;12345678C1FE

The “IoT for Technicians” mobile application extracts 12345678C1FE as Identifier from the set of caracters.

Constraints for the search on Live Objects and the identifier
The data embedded into the Qrcode must be an identifier contained into the Device Id of the device, which is provisionned into the Live Objects Portal.
To search a device, the “IoT for Technicians” mobile application requests an API and checks if the “Device Id” contains the Identifier extracted from the QRcode.
So the Identifier must be contained into the Device Id.

## Release note

Version 1.2.0

## License

Copyright (C) 2016 Orange

This software is distributed under the terms and conditions of the 'Apache-2.0' license which can be found in the file 'license.txt' in this package distribution or at

http://www.apache.org/licenses/LICENSE-2.0

### Third-Party Code

This software includes the following third-party code which can be found in the file 'thirdparties.txt' in this package distribution:

* Android AppCompat Library V7 1.1.0 Apache License 2.0
* Play Services Maps 17.0.0 Android Software Development Kit License
* Play Services TagManager 17.0.0 Android Software Development Kit License
* ZXing Android Embedded 3.6.0 Apache License 2.0
* ZXing 3.3.3 Apache License 2.0
* Retrofit 2.8.1 Apache License 2.0
* Logging Interceptor 4.4.1 Apache License 2.0
* Material Components For Android 1.3.0-alpha01 Apache License 2.0
* Android ConstraintLayout 1.1.3 Apache License 2.0
* Legacy Support 1.1.3 Apache License 2.0
* Android Support RecyclerView V7 1.1.0 Apache License 2.0
* Android Lifecycle Extensions 2.2.0 Apache License 2.0
* Android TagView-HashTagView 1.3 Apache License 2.0
* Project Lombok 1.18.12 MIT License
* AndroidX Preference 1.1.1 Apache License 2.0
* AndroidX Security 1.1.0-alpha01 Apache License 2.0

