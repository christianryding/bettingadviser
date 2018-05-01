# BettingAdviser

## About
Give suggestions on good odds from pinnacle betting site. This is done by pulling odds and event information from pinnacles API. After it compares the home, away and draw odds versus older odds. If a specific odds have dropped more than 10 percent before game starts, it is considered a good bet. It will then send an email to notify you about all these events with good odds.

## Usage
#### Current run arguments in given order is:
- Username for Pinnacle
- Password for Pinnacle
- E-mail address to send to
- E-mail address to send from
- Password for E-mail address to send from

Example given:
```java
java -jar BettingAdviser.jar myPinnacleUsername myPinnaclePassword mailAddressIWantToSendNotificationsTo mailAddressIWantToSendNotificationsFrom mailAddressIWantToSendNotificationsFromPassword
```
These are required for the program to work, below are optional parameters that can be set.

#### To set parameters
Create a new Compare instance and set applicable parameters for each operation. You must set all of required parameters for the constructor while you don't have to set optional parameters with set methods.
```java
Compare compare = new Compare(username, password, mailTo, mailFrom, mailFromPassw);
compare.setSportID(new SPORT_IDS().ESPORT);	// set sport
compare.setTimeInterval(10); 				// set time interval
compare.setPercent(0.9); 					// set percent
compare.start();
```

## Requirements
- Java 1.8 or later
- Pinnacle Sports Account (must be funded)

## Third Party Libraries and Dependencies
- [google-gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)
- [org-json](https://mvnrepository.com/artifact/org.json/json/20180130)
- [javax-mail](https://mvnrepository.com/artifact/javax.mail/mail)
- [pinnacle-api-client](https://github.com/gentoku/pinnacle-api-client)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details