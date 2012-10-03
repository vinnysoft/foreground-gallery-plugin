	    Copyright 2012 Bruno Carreira - Lucas Farias - Rafael Luna - Vinícius Fonseca. 

		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

		Unless required by applicable law or agreed to in writing, software
		distributed under the License is distributed on an "AS IS" BASIS,
		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		See the License for the specific language governing permissions and
   		limitations under the License.  

Foreground Gallery Plugin for Phonegap (Cordova) 2.1.0.

Originally by: 	- Bruno Carreira
				- Lucas Farias
				- Rafael Luna
				- Vinicius Fonseca

The default Phonegap (Cordova) Gallery Plugin calls the native camera and this makes Android Garbage Collector to kill background applications. This plugin avoid your application to go background and be killed by Garbage Collector with other applications. We used the Phonegap source code and modified it to avoid this problem. 

Adding the plugin to your project

    1) To install the plugin, move the file gallery.js to your project's www folder and include a reference to it in your html files.
    2) Put the Java files in your /src folder.
    3) Include into /res/xml/config.xml the plugin <plugin name="Gallery" value="path to your ForegroundGalleryLauncher.java"/>    
	4) Put the strings.xml in your /res/values folder.
	5) Put the gallery.xml in your /res/layout folder.
	6) Put commons-codec-1.6.jar and cordova-2.1.0.jar files in your /lib folder.
	7) In you AndroidManifest.xml, put this permissions:
			<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
					    
		And declare the Gallery Activity:
			<activity
            android:name=".GalleryActivity"
            android:label="ForegroundGalleryPlugin"
            android:screenOrientation="portrait" />

Using the plugin

	See the sample project.