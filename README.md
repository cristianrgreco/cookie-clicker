Cookie Clicker
==============

Java Maven project which plays the game [Cookie Clicker](http://orteil.dashnet.org/cookieclicker/) as efficiently as 
possible, as a real person would, without any JavaScript hacks. The definition of 'as efficiently as possible' is 
increasing the amount of cookies per second as high and as fast as possible.

Note that currently the application supports playing in both Firefox and Chrome. The ```main``` method can be altered
to choose between the two. Note that if you use Chrome, you must have a valid driver present, and that the location
of this driver be provided via a VM argument:

```
java -Dchrome.webdriver.driver=/Users/user/Downloads/chromedriver -jar cookie-clicker.jar
```

More information about the Chrome driver can be found here:
[Chrome Driver Wiki](https://code.google.com/p/selenium/wiki/ChromeDriver).
