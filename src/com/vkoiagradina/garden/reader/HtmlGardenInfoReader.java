package com.vkoiagradina.garden.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vkoiagradina.model.Candidate;
import com.vkoiagradina.model.Garden;
import com.vkoiagradina.model.GardenServiceInfo;

public class HtmlGardenInfoReader implements GardenInfoReader {

	private static final String NON_SOCIAL_PREFIX = "Списък чакащи по общи критерии за";
	private static final String SOCIAL_PREFIX = "Списък чакащи по социални критерии за";

	@Override
	public Garden getGardenInfo(GardenServiceInfo gardenServiceInfo) {
		StringBuilder gardenHtml = readGardenHtml(gardenServiceInfo);
		Garden garden = new Garden(gardenServiceInfo);
		garden.setSocialNumberOfPlaces(parseNumber(gardenHtml, SOCIAL_PREFIX));
		garden.setSocialQueue(parseQueue(gardenHtml.substring(gardenHtml.indexOf(SOCIAL_PREFIX), gardenHtml.indexOf(NON_SOCIAL_PREFIX))));
		garden.setNonSocialNumberOfPlace(parseNumber(gardenHtml, NON_SOCIAL_PREFIX));
		garden.setNonSocialQueue(parseQueue(gardenHtml.substring(gardenHtml.indexOf(NON_SOCIAL_PREFIX))));
		System.out.println("Extracted information: " + garden);
		return garden;
	}

	private StringBuilder readGardenHtml(GardenServiceInfo gardenServiceInfo) {
		StringBuilder gardenHtml = new StringBuilder();
		try {
            URL url = new URL(gardenServiceInfo.URL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String line;
            boolean startBuffering = false;
            while ((line = reader.readLine()) != null) {
            	if (startBuffering) {
            		gardenHtml.append(line);
            	} else {
            		int gardenNameIndex = line.indexOf(gardenServiceInfo.name);
            		if (gardenNameIndex > 0) {
            			gardenHtml.append(line.substring(gardenNameIndex));
            			startBuffering = true;
            		}
            	}
            }
            
            reader.close();

        } catch (MalformedURLException e) {
        	System.err.println(gardenServiceInfo.name + " - error: " + e + "\n");
        } catch (IOException e) {
            System.err.println(gardenServiceInfo.name + " - error: " + e + "\n");
        }
        
        return gardenHtml;
	}

	private int parseNumber(StringBuilder gardenHtml, String numberPrefix) {
		int prefixIndex = gardenHtml.indexOf(numberPrefix);
		String suffix = "места";
		int suffixIndex = gardenHtml.indexOf(suffix, prefixIndex + numberPrefix.length());
		String number = gardenHtml.substring(prefixIndex + numberPrefix.length(), suffixIndex);
		return Integer.parseInt(number.trim());
	}
	
	private List<Candidate> parseQueue(String gardenHtml) {
		List<Candidate> candidates = new ArrayList<Candidate>();
		// Matching the following pattern:
		//	<tr><td>1. Борис  Викторов 
		// Горнаков</td><td>10+</td><td>3т. / (1-во желание)</td></tr>
		Pattern candidatePattern = Pattern.compile("<tr><td>(\\d+)\\.([^<]+)</td><td>([\\d.]+)\\+*</td><td>(\\d+)[^<]*</td></tr>", Pattern.UNICODE_CASE);
		Matcher gardenMatcher = candidatePattern.matcher(gardenHtml);
		
		while (gardenMatcher.find()) {
			Candidate candidate = new Candidate();
			candidate.ratings = Integer.parseInt(gardenMatcher.group(1));
			candidate.name = gardenMatcher.group(2).trim().replaceAll("\\s+", " ");
			candidate.points = Float.parseFloat(gardenMatcher.group(3));
			candidate.order = Integer.parseInt(gardenMatcher.group(4));
			candidates.add(candidate);
		}
		return candidates;
	}
}

