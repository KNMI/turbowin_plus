# turbowin_plus


see: http://projects.knmi.nl/turbowin

created with NetBeans IDE


TurboWin+ (TurboWeb) was especially designed for running in the background eg when collecting sensor data. 
It can run for weeks in the background.  The memory footprint is low and the cpu usage is minimal and by 
using extended threading the application is always responsive. 



================================== VERSION 3.1.2 ====================================
- minor bug fix; true wind and apparent eind values in manual mode not updated on main screen
after sending obs
- JPMS (Java Platform Module System) version available
- In JPMS version updated JSerialComm library (1.3.11 -> 2.1.1)

================================== VERSION 3.1.1 ====================================
- minor bug fix; true wind and apparent wind values in manual mode not updated on main screen 
after completing wind input form

================================== VERSION 3.1.0 =====================================
[Version 3.1.0 compared to version 3.0.6]
- many smaller changes and updates

noticeable new items in this version:
- overview of configuration (maintenance) data
- Full support of OMC-140 AWS
- Full support of the Mintaka StarX
- Observed data on geographical map
- selectable wind speed units in graphs and dashboards
- Latest observation window
- New option to insert Relative Humidity


================================== VERSION 3.0.6 =====================================
[Version 3.0.6 compared to version 3.0.5]

This update contains a not visible new feature which  will not add any functionality but could be very useful: 
Automatically restoring communication to an attached device (EUCAWS, barometer, GPS)

If the serial communication (RS232/RS422) is lost e.g. due to a power break or device failure or an unplugged cable you 
have to restart the application, despite the device is powered-up again or the cable is attached again .
This is standard behavior for serial communication. If the communication between the device and TurboWin+ is via WiFi 
then sometimes the communication will be restored after a power failure or WiFi interruption but will not
always reconnect automatically. For more info see: https://www.taskjunction.com/



================================== VERSION 3.0.5 =====================================
[Version 3.0.5 compared to version 3.0.0]
- Better Dashboard scaling for low screen resolutions (and enlarged fonts accordingly)
- Changed the Dashboard background from WMO picture to (tiled) SOT-JCOMM logo (after counting the votes)
- If no data from an instrument is received the appropriate Dashboard dial is cleared immediately
- On Dashboard bottom screen added last update date-time
- if AWS/instrument connection is lost for 5 minutes (5 min. to be sure and to compensate small hick-ups), the Dashboard dials/meters are grayed (and the dial pointers are dashed). 
- For the youngest generation ships' officers: added also a full scalable digital Dashboard (see attachment)




================================== VERSION 3.0.0 =====================================

[Noticeable new items in TurboWin+ (TurboWeb) 3.0 compared to TurboWin+ (TurboWeb) 2.6]

•	New dashboard option for visualizing AWS  data (day and night mode). From this version there are now three AWS sensor data views available (textual on the main screen, week and day graphs and the dashboard dials)

•	Support of the Mintaka Star barometer (with integrated GPS) in USB and WiFi mode

•	New dashboard option for visualizing barometer data 

•	Recording of manual EUCAWS input  (IMMT logs, can be used to reward observers)

•	Preventing multiple instances running

•	More extensive event logging

•	Java 9 compatible (but also still Java 7 and java 8 compatible)


