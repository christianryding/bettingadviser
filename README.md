# BettingAdviser

## About
Give suggestions on good odds from pinnacle betting site. This is done by pulling odds and event information from pinnacles API. After it compares the home, away and draw odds versus older odds. If a specific odds have dropped more than 10 percent before game starts, it is considered a good bet. It will then send an email to notify you about all these events with good odds.

## Usage
#### Current run arguments in given order is:
- Username for Pinnacle
- Password for Pinnacle
- E-mailaddress to send from
- Password for E-mailaddress to send from
- E-mailaddresses to send to (multiple addresses can be given)

Example given:
```java
java -jar BettingAdviser.jar myPinnacleUsername myPinnaclePassword mailAddressIWantToSendNotificationsFrom mailAddressIWantToSendNotificationsFromPassword mailAddressIWantToSendNotificationsToNr1 mailAddressIWantToSendNotificationsToNrX..
```
These are required for the program to work, below are optional parameters that can be set.

#### To set parameters
In ShowBets.java, a Compare instance exists where you can set optional settings with given set methods, example below.
```java
Compare compare = new Compare(username, password, mailFrom, mailFromPassw, mailTo);
compare.setSportID(new SPORT_IDS().ESPORT);	// set sport to cover, default is SOCCER
compare.setTimeInterval(5);	// set update interval, default is 10 minutes 
compare.setPercent(0.95);	// set margin in percent (0.95=5%), default is (0.9=10%) 
compare.setLowerMargin(1.3);	// set lower margin for odds, default is 1.2
compare.setUpperMargin(3.4);	// set higher margin for odds, default is 3.5
compare.setCheckLiveEvents(true);	// set true if live events should be covered, default is false
compare.setTimeRange(8);	// set time range for hours before gamestart, default is to only include events that starts 7 hours before gamestart
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