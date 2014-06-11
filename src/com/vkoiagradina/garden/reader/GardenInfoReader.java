package com.vkoiagradina.garden.reader;

import com.vkoiagradina.model.Garden;
import com.vkoiagradina.model.GardenServiceInfo;

public interface GardenInfoReader {
	Garden getGardenInfo(GardenServiceInfo serviceInfo);
}
