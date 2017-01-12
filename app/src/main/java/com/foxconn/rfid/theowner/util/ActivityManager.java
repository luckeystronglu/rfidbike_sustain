package com.foxconn.rfid.theowner.util;

import android.app.Activity;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by panliuting on 16/3/30.
 */
public class ActivityManager {
  private LinkedList<Activity> activityLinkedList = new LinkedList<>();

  private ActivityManager() {
  }

  private static ActivityManager instance;

  public static ActivityManager getInstance(){
    if(null == instance){
      instance = new ActivityManager();
    }
    return instance;
  }

  //向list中添加Activity
  public ActivityManager addActivity(Activity activity){
    activityLinkedList.add(activity);
    return instance;
  }

  //结束特定的Activity(s)
  public ActivityManager finishActivities(Class<? extends Activity>... activityClasses){
    for (Activity activity : activityLinkedList) {
      if( Arrays.asList(activityClasses).contains( activity.getClass() ) ){
        activity.finish();
      }
    }
    return instance;
  }

  //结束所有的Activities
  public ActivityManager finishAllActivities() {
    for (Activity activity : activityLinkedList) {
      activity.finish();
    }
    return instance;
  }

  //退出栈中底部Activity
  public ActivityManager finishAllActivitiesExceptOne(Class cls) {
    for (Activity activity : activityLinkedList) {
      if (activity!=null && activity.getClass().equals(cls)) {
        continue;
      }
      if (activity!=null) activity.finish();
    }
    return instance;
  }

  /**
   * 指定的activity是否还存在
   * @param cls
   * @return true 表示存在， false未打开过或已经finish了
   */
  public boolean isActvivityExists(Class cls){
    for (Activity activity : activityLinkedList) {
      if (activity!=null && activity.getClass().equals(cls)) {
        return !activity.isFinishing();
      }
    }
    return false;
  }
}
