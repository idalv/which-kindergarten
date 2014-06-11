package com.vkoiagradina.garden.store;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.vkoiagradina.model.Candidate;
import com.vkoiagradina.model.Garden;
import com.vkoiagradina.model.GardenServiceInfo;

public class GardenInfoAppEngineDatastore implements GardenInfoStore {

	private static final long NON_SOCIAL = 2;
    private static final long SOCIAL = 1;

    @Override
	public String storeGardenInfo(Garden garden) {
	    Entity gardenEntity = storeGarden(garden);
		storeSocialCandidates(gardenEntity, garden);
		storeNonSocialCandidates(gardenEntity, garden);
		return KeyFactory.keyToString(gardenEntity.getKey());
	}

	private Entity storeGarden(Garden garden) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity gardenStore = new Entity("Garden_" + garden.getServiceInfo().id, "service_info");

		gardenStore.setProperty("social-number", garden.getSocialNumberOfPlaces());
		gardenStore.setProperty("non-social-number", garden.getNonSocialNumberOfPlaces());

		datastore.put(gardenStore);
		
		System.out.println("Stored number of places for garden:" + garden.getServiceInfo().name);
		
		return gardenStore;
	}
	
	private void storeSocialCandidates(Entity gardenEntity, Garden garden) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// Store the social candidates
		for (Candidate socialCandidate : garden.getSocialQueue()) {
			Entity candidate = new Entity("Garden_" + garden.getServiceInfo().id, socialCandidate.name, gardenEntity.getKey());

			candidate.setProperty("ratings", socialCandidate.ratings);
			candidate.setProperty("name", socialCandidate.name);
			candidate.setProperty("points", socialCandidate.points);
			candidate.setProperty("order", socialCandidate.order);
			candidate.setProperty("type", SOCIAL);

			datastore.put(candidate);
		}
		
		System.out.println("Stored social candidates for garden:" + garden.getServiceInfo().name);
	}
		
	private void storeNonSocialCandidates(Entity gardenEntity, Garden garden) {
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Store non-social candidates
		for (Candidate nonsocialCandidate : garden.getNonSocialQueue()) {
			Entity candidate = new Entity("Garden_" + garden.getServiceInfo().id, nonsocialCandidate.name, gardenEntity.getKey());

			candidate.setProperty("ratings", nonsocialCandidate.ratings);
			candidate.setProperty("name", nonsocialCandidate.name);
			candidate.setProperty("points", nonsocialCandidate.points);
			candidate.setProperty("order", nonsocialCandidate.order);
			candidate.setProperty("type", NON_SOCIAL);

			datastore.put(candidate);
		}
		
		System.out.println("Stored non-social candidates for garden:" + garden.getServiceInfo().name);

	}

    @Override
    public Garden readGardenInfo(String key) throws EntityNotFoundException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity gardenEntity = datastore.get(KeyFactory.stringToKey(key));
        Garden garden = new Garden();
        garden.setSocialNumberOfPlaces((Long) gardenEntity.getProperty("social-number"));
        garden.setNonSocialNumberOfPlace((Long) gardenEntity.getProperty("non-social-number"));
//        garden.setSocialQueue(readSocialCandidates(gardenEntity.getKind()));
        garden.setNonSocialQueue(readNonSocialCandidates(gardenEntity.getKind()));
        return garden;
    }
    
    @Override
    public Garden readGardenInfo(GardenServiceInfo serviceInfo) throws EntityNotFoundException {
        String key = KeyFactory.createKeyString("Garden_" + serviceInfo.id, "service_info");
        Garden garden = readGardenInfo(key);
        garden.setServiceInfo(serviceInfo);
        return garden;
    }
    
    private List<Candidate> readNonSocialCandidates(String kind) {
        List<Candidate> candidates = new ArrayList<Candidate>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Filter typeFilter = new FilterPredicate("type", FilterOperator.EQUAL,
                (Long)NON_SOCIAL);
        Query q = new Query(kind);
        PreparedQuery pq = datastore.prepare(q);

        for (Entity result : pq.asIterable()) {
            Candidate candidate = new Candidate();
            candidate.ratings = (Long) result.getProperty("ratings");
            candidate.name = (String) result.getProperty("name");
            candidate.points = (Float) result.getProperty("points");
            candidate.order = (Long) result.getProperty("order");
            candidates.add(candidate);
        }
        
        return candidates;
    }
//    
//    private List<Candidate> readSocialCandidates(String kind) {
//        List<Candidate> candidates = new ArrayList<Candidate>();
//        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//
//        Filter typeFilter = new FilterPredicate("type", FilterOperator.EQUAL,
//                "social");
//        Query q = new Query(kind).setFilter(typeFilter);
//        PreparedQuery pq = datastore.prepare(q);
//
//        for (Entity result : pq.asIterable()) {
//            Candidate candidate = new Candidate();
//            candidate.ratings = (Long) result.getProperty("ratings");
//            candidate.name = (String) result.getProperty("name");
//            candidate.points = (Float) result.getProperty("points");
//            candidate.order = (Long) result.getProperty("order");
//            candidates.add(candidate);
//        }
//        
//        return candidates;
//    }
}
