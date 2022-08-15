package com.flatcode.littletasks.Unit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DATA {
    //Database
    public static String USERS = "Users";
    public static String TASKS = "Tasks";
    public static String AUTO_TASKS = "AutoTasks";
    public static String OBJECTS = "Objects";
    public static String CATEGORIES = "Categories";
    public static String PLANS = "Plans";
    public static String CHOOSE_PLAN = "choosePlan";
    public static String POINTS = "points";
    public static String AVAILABLE_POINTS = "AVPoints";
    public static String RANK = "rank";
    public static String START = "start";
    public static String END = "end";
    public static String TOOLS = "Tools";
    public static String PRIVACY_POLICY = "privacyPolicy";
    public static String VERSION = "version";
    public static String EMAIL = "email";
    public static String BASIC = "basic";
    public static String USER_NAME = "username";
    public static String PROFILE_IMAGE = "profileImage";
    public static String TIMESTAMP = "timestamp";
    public static String ID = "id";
    public static String IMAGE = "image";
    public static String PUBLISHER = "publisher";
    public static String CATEGORY = "category";
    public static String TITLE = "title";
    public static String FAVORITES = "Favorites";
    public static String NAME = "name";
    public static String PLAN = "plan";
    //Others
    public static String DOT = ".";
    public static String EMPTY = "";
    public static int CURRENT_VERSION = 1;
    public static int SPLASH_TIME = 2000;
    public static int MIX_SQUARE = 500;
    public static int ZERO = 0;
    public static Boolean searchStatus = false;
    //Shared
    public static String PROFILE_ID = "profileId";
    public static String CATEGORY_ID = "categoryId";
    public static String TASK_ID = "taskId";
    public static String TASK_TYPE = "taskType";
    public static String TASKS_ALL = "tasksAll";
    public static String TASKS_UN_STARTED = "tasksUnStarted";
    public static String TASKS_STARTED = "tasksStarted";
    public static String TASKS_COMPLETED = "tasksCompleted";
    public static String PLAN_ID = "planId";
    public static String COLOR_OPTION = "color_option";
    public static String NEW_PLAN = "newPlan";
    //Other
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final FirebaseUser FIREBASE_USER = AUTH.getCurrentUser();
    public static final String FirebaseUserUid = FIREBASE_USER.getUid();
    public static final String WEBSITE = "";
    public static final String FB_ID = "";
}