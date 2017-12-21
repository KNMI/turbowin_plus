# turbowin_plus


see: http://projects.knmi.nl/turbowin

created with NetBeans IDE


TurboWin+ (TurboWeb) was especially designed for running in the background eg when collecting sensor data. 
It can run for weeks in the background.  The memory footprint is low and the cpu usage is minimal and by 
using extended threading the application is always responsive. Furthermore in TurboWeb mode the app size 
is very small (<3 Mb).



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


