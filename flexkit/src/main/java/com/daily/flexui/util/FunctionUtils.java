package com.daily.flexui.util;

public class FunctionUtils {

    public static float getFunctionValue(float x, float maxX, float minX, float maxY, float minY, float yValue){
        if(x > maxX){
            x = maxX;
        }else if(x < minX){
            x = minX;
        }
        return ((maxY - minY)/(maxX-minX))*x+yValue;
    }
}
