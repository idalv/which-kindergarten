package com.vkoiagradina.garden;

import java.util.concurrent.ThreadFactory;

import com.google.appengine.api.ThreadManager;
import com.vkoiagradina.api.Kindergardens;
import com.vkoiagradina.garden.reader.GardenInfoReader;
import com.vkoiagradina.garden.reader.HtmlGardenInfoReader;
import com.vkoiagradina.garden.store.GardenInfoAppEngineDatastore;
import com.vkoiagradina.garden.store.GardenInfoStore;
import com.vkoiagradina.model.Garden;
import com.vkoiagradina.model.GardenServiceInfo;

public class GardenSynchronizer {

	public static void main(String[] args) {
		try {
			new GardenSynchronizer().synchronize();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void synchronize() throws InterruptedException {        
            // sync each garden in a separate thread
        	ThreadFactory threadFactory = ThreadManager.currentRequestThreadFactory();
        	for (GardenServiceInfo gardenServiceInfo : Kindergardens.gardensToSync) {
            	GardenSynchronization synchronizer = new GardenSynchronization(gardenServiceInfo);
            	Thread synchronizerThread = threadFactory.newThread(synchronizer);
            	synchronizerThread.start();
            	synchronizerThread.join();
			}
    }

    /**
     * A thread that syncrhonizes a kindergarden.
     */
    private static class GardenSynchronization implements Runnable {

        private final GardenServiceInfo gardenServiceInfo;

        public GardenSynchronization(GardenServiceInfo gardenInfo) {
            this.gardenServiceInfo = gardenInfo;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {
        	System.out.println(gardenServiceInfo.name + " - about to sync from " + gardenServiceInfo.URL + "\n");
            GardenInfoReader gardenReader = new HtmlGardenInfoReader();
            Garden syncedGarden = gardenReader.getGardenInfo(gardenServiceInfo);
            GardenInfoStore gardenStore = new GardenInfoAppEngineDatastore();
            System.out.println(gardenStore.storeGardenInfo(syncedGarden));
        }
    }

}
