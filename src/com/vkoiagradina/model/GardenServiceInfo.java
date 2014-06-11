package com.vkoiagradina.model;

public class GardenServiceInfo {
	public int id;
	public String name;
	public int year;
	public String URL;
	
	public GardenServiceInfo(int id, String name, int year, String URL) {
		this.id = id;
		this.name = name;
		this.year = year;
		this.URL = URL;
	}

	@Override
	public String toString() {
		return "GardenServiceInfo [id=" + id + ", name=" + name + ", year="
				+ year + ", URL=" + URL + "]";
	}
}