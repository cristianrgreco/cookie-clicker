Cookie Clicker
==============

Java Maven project which plays the game [Cookie Clicker](http://orteil.dashnet.org/cookieclicker/) as efficiently as 
possible.

The idea is to automate playing the game as a real person would, and get to the
highest amount of 'cookies per second' as fast as possible, without any JS hacks.

Note that currently the application supports playing in both Firefox and Chrome. The ```main``` method can be altered
to choose between the two. Note that if you use Chrome, you must have a valid driver present, and that the location
of this driver be provided via a VM argument:

```java -Dchrome.webdriver.driver=/Users/user/Downloads/chromedriver -jar cookie-clicker.jar```

More information about the Chrome driver can be found here: [Chrome Driver Wiki](https://code.google.com/p/selenium/wiki/ChromeDriver)
