# Directory Housekeeper
Contains a DirectoryHousekeeper class that provides simple directory housekeeping functionality. Clients must declare a bean of type DirectoryHousekeeper and provide it with:
* The directory path to be housekept
* The number of days to retain files.
* Optionally, a filename regex to match files eligible for housekeeping.
The above properties should be injected into the bean whilst, separately, a directory.housekeeping.schedule property must be set in the Environment that specifies a cron expression that governs when the housekeeping process should run. These separate strategies for injecting the different properties allow multiple beans to be configured in the case where multiple directories need to be housekept by the same microservice.  
