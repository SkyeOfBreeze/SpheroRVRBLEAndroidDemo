## Sphero RVR Motor control Demo for Android ##

This sample app shows working code to control the Sphero motors via Bluetooth LE on Android.

When the API comes out I may make a more in depth SDK to use instead of this demo, as this only controls the motors without aid from the IMU.

I use this as is for streaming my Robot, but it can be built off of and improved.

## How to build ##

Download Android Studio 3.5 and install and run from there. Android Studio may also require Java to be installed first.

## How to operate ##

Before you run it, if the device is running Android 6.0 or above, searching for Bluetooth devices requires location permission. 
After installing, enable this permission in the app settings. This page has info about why this is needed. The app only uses it for bluetooth scanning. https://developer.android.com/guide/topics/connectivity/bluetooth

When running you will have the ability to change speeds. Halfway point will be the stop position, full right will be speed in one direction, 
and full left willl be the other direction. For the left direction, do not go all the way to the left, or the motors may not move.

The text field is left over from the old code and is not functional.


## Operating on Remo.TV, a telepresence site (Currently under development) ##

This section will hopefully explain how to setup a robot using the RVR for Remo.TV. This uses the Android smartphone as the streaming device and streams it to the website. 
It may make the phone get hot and drain the battery life quickly.

To operate this on Remo.TV (These steps will evolve if they don't help people as is):

1. Attach a phone mount to the robot (3D printed or store bought that can be mounted to the mounting plate)
2. Install the Remo Controller for Android APK from here (may contain bugs, but is mostly functional): https://github.com/remotv/controller-for-android/releases
3. Install this Demo app
4. Go into the app permissions for the demo app and enable the Remo.TV Control Socket Broadcast
5. Follow the setup guide in the ReadMe of remotv/controller-for-android
6. Go into settings and select click on the Robot settings section
7. Change the Connection Type to RemoBroadcaster
8. Change the ProtocolType to ArduinoTranslator (Might be renamed to raw text sender or something similar in the future)
9. Once settings are entered, hit the back button for settings and power on the robot.
10. Open this demo app and connect to RVR, leaving the screen on (may be improved later on)
11. Robot should now be controllable.

A note about the site:

Remo.TV allows anyone on the Internet to drive the robot around, and streams video and audio (optional), and can speak if TTS is enabled.
This can be set to a private link that only people with the link can access, but should probably be avoided in a classroom setting if setting to public.

The source of this Repo mainly came from https://github.com/DFRobot/BlunoBasicDemo, and is licensed under the GNU General Public License v3.0. 
Changes to that based on the initial commit to this repo can be found in the attached diff.txt, from there you can look at the history for future changes past the initial commit


