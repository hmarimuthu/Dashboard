package com.etaap.utils.gsonUtils;

public class GsonBuilder {
	
	private volatile static GsonBuilder _instance;
	
	
	private GsonBuilder() {
        // preventing Singleton object instantiation from outside
    }


	public static GsonBuilder getInstanceDC() {
        if (_instance == null) {
            synchronized (GsonBuilder.class) {
                if (_instance == null) {
                    _instance = new GsonBuilder();
                }
            }
        }
        return _instance;
    }


	/*public static GsonBuilder getGsonBuilder()
	{
		
		
	}
*/





	
	
	
	
}
