package com.samilywang.plugins.baidupush;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

/**
 * Author: Samily Wang
 * Create-Date: 2015-06-24
 * Description: the Android part of BaiduPush plugin
 */
public class BaiduPushPlugin extends CordovaPlugin {
    /* LOG TAG */
    private static final String LOG_TAG = "BaiduPushPlugin";

    /* JS回调上下文接口 */
    public static CallbackContext NotificationClickCallbackContext = null;
    public static CallbackContext NotificationArriveCallbackContext = null;
    public static CallbackContext MessageArriveCallbackContext = null;
    public static CallbackContext BaiduPushcallbackContext = null;

    /**
     * Cordova框架中js端和native端交互的接口函数
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     * @throws JSONException
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        boolean status = true;

        try {
            if ("startWork".equals(action)) {
                final String apiKey = args.getString(0);
                PushManager.startWork(cordova.getActivity().getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, apiKey);
            } else if ("stopWork".equals(action)) {
                PushManager.stopWork(cordova.getActivity().getApplicationContext());
            } else if ("resumeWork".equals(action)) {
                PushManager.resumeWork(cordova.getActivity().getApplicationContext());
            } else if ("setTags".equals(action)) {
                List<String> tags = getTagsFromArgs(args);
                PushManager.setTags(cordova.getActivity().getApplicationContext(), tags);
            } else if ("delTags".equals(action)) {
                List<String> tags = getTagsFromArgs(args);
                PushManager.delTags(cordova.getActivity().getApplicationContext(), tags);
            } else if ("listTags".equals(action)) {
                PushManager.listTags(cordova.getActivity().getApplicationContext());
            } else if ("listenNotificationClicked".equals(action)) {
                NotificationClickCallbackContext = callbackContext;
            } else if ("listenMessage".equals(action)) {
                MessageArriveCallbackContext = callbackContext;
            } else if ("listenNotificationArrived".equals(action)) {
                NotificationArriveCallbackContext = callbackContext;
            } else {
                status = false;
            }

            //暂时挂起js－native通道
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            BaiduPushcallbackContext = callbackContext;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }


    /**
     * 将json字符串转换为列表
     *
     * @param args json字符串
     * @return tags的列表
     */
    private List<String> getTagsFromArgs(JSONArray args) throws JSONException {
        List<String> tags = null;
        args = args.getJSONArray(0);
        if (args != null && args.length() > 0) {
            int len = args.length();
            tags = new ArrayList<String>(len);
            for (int inx = 0; inx < len; inx++) {
                try {
                    tags.add(args.getString(inx));
                } catch (JSONException e) {
                    LOG.e(LOG_TAG, e.getMessage(), e);
                }
            }
        }

        return tags;
    }
}
