package com.vkoiagradina.garden.store;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.vkoiagradina.model.Garden;
import com.vkoiagradina.model.GardenServiceInfo;

public interface GardenInfoStore {
	String storeGardenInfo(Garden garden);
	Garden readGardenInfo(String key) throws EntityNotFoundException;
    Garden readGardenInfo(GardenServiceInfo serviceInfo)
            throws EntityNotFoundException;
}
