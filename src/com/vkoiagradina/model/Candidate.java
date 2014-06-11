package com.vkoiagradina.model;

public class Candidate {
	public long ratings;
	public String name;
	public float points;
	public long order;
	
	@Override
	public String toString() {
		return "Candidate [ratings=" + ratings + ", name=" + name + ", points="
				+ points + ", order=" + order + "]";
	}
}
