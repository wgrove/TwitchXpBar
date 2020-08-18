# TwitchXpBar
A Twitch XP that determines your streamer "Level" based on followers and subs. Written in Java.

The tool was designed to be easy to use (and was built quickly, as you may see by the code).

The following important notes must addressed:

1. This application requires OBS StreamLabels to be installed and running for the XP to work.

2. When running the application for the first time, a config file will be generated in the launch directoy.

3. In order for the application to find the stream labels local files, you must open the generated xpBar.properties
and set the "StreamLabelsDirectoryPath" to point to the location specified in the StreamLabels application. This can be found by going to the settings in OBS StreamLabels.

4. All Colors of the XP Bar can be customized using the RGB values within the properties files.

5. When capturing the window in OBS, you must add a filter to key out the XP Bar key color.