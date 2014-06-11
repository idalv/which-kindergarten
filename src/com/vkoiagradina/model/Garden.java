package com.vkoiagradina.model;

import java.util.ArrayList;
import java.util.List;

public class Garden {
	private GardenServiceInfo serviceInfo;
	private long socialNumberOfPlaces;
	private long nonSocialNumberOfPlace;
	private List<Candidate> socialQueue = new ArrayList<Candidate>();
	private List<Candidate> nonSocialQueue = new ArrayList<Candidate>();
	
    public Garden() {
    }
	   
	public Garden(GardenServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public long getSocialNumberOfPlaces() {
		return socialNumberOfPlaces;
	}

	public void setSocialNumberOfPlaces(long socialNumberOfPlaces) {
		this.socialNumberOfPlaces = socialNumberOfPlaces;
	}

	public long getNonSocialNumberOfPlaces() {
		return nonSocialNumberOfPlace;
	}

	public void setNonSocialNumberOfPlace(long nonSocialNumberOfPlace) {
		this.nonSocialNumberOfPlace = nonSocialNumberOfPlace;
	}

	public List<Candidate> getSocialQueue() {
		return socialQueue;
	}

	public void setSocialQueue(List<Candidate> socialQueue) {
		this.socialQueue = socialQueue;
	}

	public List<Candidate> getNonSocialQueue() {
		return nonSocialQueue;
	}

	public void setNonSocialQueue(List<Candidate> nonSocialQueue) {
		this.nonSocialQueue = nonSocialQueue;
	}

	public GardenServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(GardenServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    @Override
	public String toString() {
		return "Garden [serviceInfo=" + serviceInfo + ", socialNumberOfPlaces="
				+ socialNumberOfPlaces + ", nonSocialNumberOfPlace="
				+ nonSocialNumberOfPlace + ", socialQueueSize=" + socialQueue.size()
				+ ", nonSocialQueueSize=" + nonSocialQueue.size() + "]";
	}
}
