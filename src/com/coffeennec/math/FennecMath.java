package com.coffeennec.math;

public class FennecMath {
	public final static float EPSILON = 1e-6f;
	 
	public static float convertRange(float value, float minRange, float maxRange, float minOutput, float maxOutput) {
		return (value - minRange) * (maxOutput - minOutput) / (maxRange - minRange) + minOutput;
	}
	
    public static float clamp(float value, float min, float max) {
    	if(min == max) 	return min;
    	
    	if(min > max) 	throw new IllegalArgumentException("min is greater than the max.");
    	
    	if(value < min) return min;
    		
    	if(value > max) return max;
    	
        return value;
    }
   
    public static float random(float minInclusive, float maxInclusive) {    	
    	return (float) ((Math.random() * (maxInclusive - minInclusive + 0)) + minInclusive);
    }
    
    
    public static float lerp(float value, float startRange, float endRange) {
    	return startRange + (endRange - startRange) * FennecMath.clamp(value, 0, 1);
    }
}
