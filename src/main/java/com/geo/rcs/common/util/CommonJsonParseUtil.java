package com.geo.rcs.common.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by geo on 2019/5/31.
 */
public class CommonJsonParseUtil {
    public static Map<String, Object> parse(String jsonStr) {

               Map<String, Object> result = null;

                if (null != jsonStr) {
                         try {

                                JSONObject jsonObject = new JSONObject(jsonStr);
                                result = parseJSONObject(jsonObject);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                     } // if (null != jsonStr)

               return result;
           }

         public  static Object parseValue(Object inputObject) throws JSONException {
                Object outputObject = null;

                if (null != inputObject) {
                        if (inputObject instanceof JSONArray) {
                         outputObject = parseJSONArray((JSONArray) inputObject);
                            } else if (inputObject instanceof JSONObject) {
                                outputObject = parseJSONObject((JSONObject) inputObject);
                            } else if (inputObject instanceof String || inputObject instanceof Boolean || inputObject instanceof Integer) {
                                outputObject = inputObject;
                            }
                   }

                 return outputObject;
             }
          public  static List<Object> parseJSONArray(JSONArray jsonArray) throws JSONException {

                 List<Object> valueList = null;
               if (null != jsonArray) {
                        valueList = new ArrayList<Object>();

                         for (int i = 0; i < jsonArray.length(); i++) {
                                 Object itemObject = jsonArray.get(i);
                                if (null != itemObject) {
                                         valueList.add(parseValue(itemObject));
                                    }
                           } // for (int i = 0; i < jsonArray.length(); i++)
                   } // if (null != valueStr)
        return valueList;
           }

          public  static  Map<String, Object> parseJSONObject(JSONObject jsonObject) throws JSONException {

                Map<String, Object> valueObject = null;
                if (null != jsonObject) {
                        valueObject = new HashMap<String, Object>();

                         Iterator<String> keyIter = jsonObject.keys();
                       while (keyIter.hasNext()) {
                                String keyStr = keyIter.next();
                                Object itemObject = jsonObject.opt(keyStr);
                                 if (null != itemObject) {
                                      valueObject.put(keyStr, parseValue(itemObject));
                                  } // if (null != itemValueStr)

                            } // while (keyIter.hasNext())
                    } // if (null != valueStr)

                return valueObject;
            }
}
