package br.com.dextra.dexboard;

import java.util.Collections;
import javax.cache.*;
import com.google.gson.JsonArray;

@SuppressWarnings("unchecked")
public class MemCache {

	public Cache cache;
    private String key = "0";

    public JsonArray getCache() {
    	JsonArray value;
	    // Get the value from the cache.
	    value = (JsonArray)cache.get(key);
	    return value;
	}

   public void setCache(JsonArray dadosPlanilha) {
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // Put the value into the cache.
	    cache.put(key, dadosPlanilha);
   	}

}
