package com.example.demo.Utils;

import java.lang.reflect.Field;

public class Utils {
    public static void injectObject(Object target, String fieldName, Object toInject){
        boolean isPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            //check if accessible
            if ( !f.isAccessible() ){
                f.setAccessible(true);
                isPrivate = true;
            }

            // set to toInject Object
            f.set(target, toInject);

            if( isPrivate ){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
