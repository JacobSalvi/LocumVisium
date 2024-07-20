# LocumVisium Android App
<img src="gitData/ic_launcher.webp" alt="Locum Visum Logo" width="170" height="170">

Locum Visium is a community-based social media Android app designed to help users discover popular local places and events near their position. 📍📱

## Overview 
<img src="gitData/Locum%20Visium.gif" alt="overview gif" width="375" height="674">

[Overview Video](https://www.youtube.com/watch?v=UocxDTZqIPg&ab_channel=MSstudioHD)

## Features
- **Discover** Local Places and Events: Easily find popular local spots and events through user-generated content.
- **Share** Your Favorite Spots: Take and share pictures and text about your favorite places, increasing their visibility.
- **Interactive Map**: Posts are displayed on an interactive map using the Google Maps API, with clickable pins for easy exploration. 
- **Social Media Feed**: View posts in a social media-like scroll-feed, showcasing pictures and descriptions of local spots.

## Technologies
- **Android API level 31**: application written in Java, android 12  
- **Google Maps API**: Used to display an interactive map with clickable pins. 
- **GoogleImagesSearch API**: To retrieve images for the mock data.
- Android Camera and GPS Integration
- **Backend Server**: Developed using Python with Flask.

   Here's the revised version of the installation and usage guide, with improved clarity and grammar:

## Python Flask Back-end  
[Back-end git](https://github.com/alessiogiovagnini/locusVisium-back-end )


## Installation
This application is designed for Android devices targeting API level 31 (Android 12).

### Set Google Maps API Key
To integrate Google Maps, you need to provide your API key. Update the `AndroidManifest.xml` file located in the `app/manifests` directory by adding your API key as shown below:

```xml
<meta-data
   android:name="com.google.android.geo.API_KEY"
   android:value="${apiKey}"
/>
```

Replace `${apiKey}` with your actual Google Maps API key.

### Configure Server IP Address
Ensure the application points to the correct server. For local installations or when using a local server, set the server IP address in the `config.properties` file located at `app/src/main/res/raw/`. Update the file as follows:

```properties
host=SERVER_IP
```

Replace `SERVER_IP` with the actual IP address of your server.