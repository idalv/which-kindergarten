package com.vkoiagradina.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class VKoiaGradinaApplication extends Application {

	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(Kindergardens.class);
		return s;
	}
}
