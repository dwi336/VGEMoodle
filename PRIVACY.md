# Data protection

The purpose of this Privacy notice is to inform the user how the app uses data and how they are protected.

The App just needs two permissions: INTERNET (https://developer.android.com/reference/android/Manifest.permission#INTERNET) and WRITE_EXTERNAL_STORAGE(https://developer.android.com/reference/android/Manifest.permission#WRITE_EXTERNAL_STORAGE). INTERNET is used to access the configured external Moodle instance. Per default the URL of the Moodle instance of the Ville-Gymnasium in Erftstadt is configured (https://www.ville-moodle.de/login/index.php). WRITE_EXTERNAL_STORAGE is required to upload files to or download files from the Moodle instance.

In terms of personal information, the app itself saves the username and password for the associated Moodle instance. These are stored in the app's local storage area as AES256 encrypted preferences (https://developer.android.com/reference/androidx/security/crypto/EncryptedSharedPreferences). In addition, bookmarks can be saved for pages of the Moodle instance. These are also stored in the app's local storage area.

In addition to the app, users access an external Moodle instance. The operator of the Moodle instance is responsible for compliance with data protection requirements when using the service. When using the external Moodle instance, data from the accessed websites is cached. This data may also contain personal data. The cached data are also stored in the app's local storage area. Access to data in the app's local storage area is limited by access control mechanism on the app.

User access to the app and the locally stored data can be additionally protected with a numeric PIN. If the PIN is forgotten, the app and the saved data can no longer be used. The app will then offer the option to erase all settings.
