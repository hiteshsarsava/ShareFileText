# Sharing with other app and share reader from other app


## Features

Sharing :
* Share the text.
* Share the html text.
* Share the single file.
* Supported files to share is image, audio, video and docs.
* Share multiple files.
* Compose Email to send.
* supported 3 types of data type sharing, Uri, File and file path.

Share reader :
* Read the text, audio , image , video and doc files
* Return the List of data in just one line.


## system requirement

* Minimum sdk version 21 and maximum supported 33.
* Prefer Android studio version more than 3.5


## how install or integrate in project

1. Copy paste the “sharing” folder in the Project folder.
2. Add below lines in dependencies section in app level build.gradle file
   ```api project(':sharing')```

3. Add  below lines in settings.gradle file.
   ```include ':sharing'```

4. Now sync the project or build the project.

5. Copy paste below code of intent-filter for reading data in AndroidManifest.xml file. Add this in which Activity you want to read the data.
   For example, Below we want to read data in ShareHandlerActivity file.

 ```
 <activity android:name="com.example.sharing.ShareHandlerActivity"
              android:exported="true">
              
            <intent-filter>
                <action android:name="android.intent.action.SEND" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="*/*" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.SEND_MULTIPLE" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="*/*" />
            </intent-filter>
 </activity>
 ```

Note : 

6. Copy paste the xml folder from sharing folder to project's res folder.
   sharing -> res -> xml TO app -> res

Note : 1. In the provider_paths.xml file added required paths. If your project required more configured paths then add in this file.
          This paths are required to share the files without requiring the storage permissions. Here we used the FilerProvider concept for
          secure sharing.
       2. If do not want to read the other app data then not required to set up the 5th step.

## How to use

* Use of Sharing :

1. Share composer initialisation.

 ```ShareComposer shareComposer = shareComposer = new ShareComposer(ShareContentActivity.this);```


2. Start the sharing.
	
 Text Sharing. If text is html then set mime type html. Set it as per required.
 ```
 shareComposer.shareText("Title in share dialog", "Text to share", TextMimeType.HTML);
 ```

 File sharing. files can multiple or single as per need. 
 ```
 shareComposer.shareFile("Title in share dialog", files);
 ```

* Use of data Reading :

1. Share handler initialisation.

 ```
 ShareHandler shareHandler  = new ShareHandler();
 ```
 
2. Read the data from intent.

 ```
 TaskResult taskResult = shareHandler.handle(ShareHandlerActivity.this, getIntent());
 if (taskResult.isSuccessFull()) {
     List<ShareModel> sharedData = taskResult.getShareDataList();
 }
 ```
