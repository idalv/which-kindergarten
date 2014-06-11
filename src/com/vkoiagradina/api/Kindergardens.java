package com.vkoiagradina.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.vkoiagradina.garden.GardenSynchronizer;
import com.vkoiagradina.garden.store.GardenInfoAppEngineDatastore;
import com.vkoiagradina.garden.store.GardenInfoStore;
import com.vkoiagradina.model.GardenServiceInfo;

@Path("/kindergardens")
public class Kindergardens {
    
    public static GardenServiceInfo[] gardensToSync = {
            new GardenServiceInfo(1, "98 ОДЗ Слънчево зайче", 2011, "http://kg.sofia.bg/isodz/stat-rating/waiting/1384"),
            new GardenServiceInfo(2, "28 ОДЗ Ян Бибиян", 2011, "http://kg.sofia.bg/isodz/stat-rating/waiting/1376")
//          new GardenServiceInfo(1, "98 ОДЗ Слънчево зайче", 2011, "http://localhost:8888/kg1.html"),
//          new GardenServiceInfo(2, "28 ОДЗ Ян Бибиян", 2011, "http://localhost:8888/kg2.html")
    };
	
	@GET
	@Path("/sync")
	@Produces("text/plain")
	public String syncKindergardens() {
		try {
			new GardenSynchronizer().synchronize();
		} catch (InterruptedException e) {
			System.out.println(e);
			return "Error:" + e;
		}
		
		return "Finished";
	}
	
    @GET
    @Path("/{key}")
    @Produces("text/plain")
    public String getKindergardens(@PathParam("key") String key) {
        try {
            GardenInfoStore gardenStore = new GardenInfoAppEngineDatastore();
            return gardenStore.readGardenInfo(key).toString();
        } catch (EntityNotFoundException e) {
            System.out.println(e);
            return "Error:" + e;
        }
    }
    
    @GET
    @Produces("text/plain")
    public String getKindergardens() {
        StringBuilder result = new StringBuilder();
        try {
            GardenInfoStore gardenStore = new GardenInfoAppEngineDatastore();
            
            for (GardenServiceInfo gardenServiceInfo : Kindergardens.gardensToSync) {
                result.append(gardenStore.readGardenInfo(gardenServiceInfo).toString() + "\n");
            }
            
            return result.toString();
        } catch (EntityNotFoundException e) {
            System.out.println(e);
            return "Error:" + e;
        }
    }
}
