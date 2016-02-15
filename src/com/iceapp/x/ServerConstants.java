package com.iceapp.x;

public class ServerConstants {
    
    public final static String STAT_LOGGER_NAME = "analytics";
    
    public final static String STAT_LOGGER_APPENDER =  "ANALYTICS-FILE-ROLLING";
    
    public final static String STAT_LOGGER_MARKER = "STAT";
    
	public static final String CATALINA_HOME = "catalina.home";

	public static final String DECLARE_DEAD_TIME_PROP = "declareDeadTime";

	public static final String DEAD_USER_CLEANER_SLEEP_TIME_PROP = "userCleanerSleepTime";

	public static final String RELOAD_LICENSES = "reloadLicenses";

	public static final String INIT_LICENSE_SERVICE = "initializeFLicenseService";

	public static final String SESSION_EXPIRE_TIME_PROP = "sessionExpireTime";

	/**
	 * time in minutes
	 */
	public static final int DEFAULT_SESSION_EXPIRE_TIME = 2;

	public static final int DEFAULT_TIME_TO_DECLARE_DEAD = 5;
	
	public static final int DEFAULT_SLEEP_TIME = 1;

	public static final String EQUAL = "=";

	public static final String COMMA = ",";

	public static final String COMMENT = "#";

	public static final String AT_RATE = "@";
	
	public static final String TAB = "\t";
	
	public static final String PIPE = "|";

	public static final String EVERYONE = "EVERYONE";

	public static final String USER_ACCESS_FILENAME = "users.access";

	public static final String GROUP_ACCESS_FILENAME = "groups.access";

	public static final String SESSION_LOGIN_PARAM = "login";

	public static final String LOGIN_ACCOUNT_FILENAME = "login.access";

	public static final String PROXY_FILENAME = "proxy.access";

	public static final String PROXY_KEY = "proxy";

	public static final String PORT_KEY = "port";

	public static final String USERNAME_KEY = "user";

	public static final String PASSWORD_KEY = "passwd";

	/**
	 * error messages
	 */
	public static final int INVALID_DEDICATED_ERROR_CODE = 1;
	public static final String INVALID_DEDICATED_ERROR_MSG = "Cannot create the group. Number of Dedicated license(s) cannot be more than the available licenses.";

	public static final int INVALID_MAXIMUM_ERROR_CODE = 2;
	public static final String INVALID_MAXIMUM_ERROR_MSG = "Cannot create the group. Number of Maximum license(s) cannot be more than the available licenses.";

	public static final int PERMISSION_GROUPNAME_INUSE_ERROR_CODE = 3;
	public static final String PERMISSION_GROUPNAME_INUSE_ERROR_MSG = "Group Name GROUP_NAME is in use. Change the group name.";

	public static final int PERMISSION_GROUP_IN_USE_ERROR_CODE = 4;
	public static final String PERMISSION_GROUP_IN_USE_ERROR_MSG = "Member(s) of this group are accessing license(s): LICENSE_FLAVOURS_INUSE.NEW_LINE Go to Users and remove the member(s) of this group or retry when the license is not in use.";

	
	public static String getErrorMessage(int errorCode) {
		String errorMsg = "";
		switch (errorCode) {
		case 1:
			errorMsg = INVALID_DEDICATED_ERROR_MSG;
			break;
		case 2:
			errorMsg = INVALID_MAXIMUM_ERROR_MSG;
			break;
		case 3:
			errorMsg = PERMISSION_GROUPNAME_INUSE_ERROR_MSG;
			break;
		case 4:
			errorMsg = PERMISSION_GROUP_IN_USE_ERROR_MSG;
			break;
		default:
			break;
		}

		return errorMsg;
	}
}
