# Gear360App
A Gear 360 app that is opensource and should work better than the original one

### The goal of this app
This app should be smaller, not as resource intensive and comatible with most of the android devices out there.
And of course it's open source.

### Features that should be in the app
* Live View with different modes (for example lightweight which doesn't stich the footage and normal which does)
* Remote shutter
* Modify settings of the camera
* View the recorded photos and videos of the camera

### State of progress
The app is more or less a alpha version. Things are being tested, the original app is being reverse engineered and my brain is being melted. Curently some basic Bluetooth communication is possible:
* Making photos or videos
* Getting some infos about the Gear360

### Requirements
The app needs the [Samsung Accessory SDK](https://developer.samsung.com/galaxy-watch/develop/sdk) to work.
The file Addon.jar is currently unused, you can just remove it form the build.gradle file.

### How to get the app running
For connecting to the Gear360 you need the original Gear360 app.
* Open the Samsung Gear360 App and click connect
* When the Camera is connected close the app by double tapping the back button
* Open the Gear360App and click connect
* The camera should be connected

If you want to stream live video:
* You first need to get the wifi password of the camera. You can get it if you set the camera in Google Street View mode. To do that you need to power the camera off. Then Power it on again. Long press the Bluetooth/Menu Button. The click on that button again until you see Google StreetView on the screen. Press the red button and then the camera should display the password (should be an long number). I recommend to write it on a note.
* Power the camera off and on and connect to the camera as mentioned above
* Click on Dev Home then on Functions and then Live View
* Connect to the cameras wifi with the password you got earlier
* Wait a few seconds once it's connected.
* Go back to the app and click on Live Activity.
* You should seen the live stream

### Help is welcome
If you want to help me feel free to create an Issue or Pull request.
Thanks and have a great day ;)
