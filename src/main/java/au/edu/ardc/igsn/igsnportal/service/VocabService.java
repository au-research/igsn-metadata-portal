package au.edu.ardc.igsn.igsnportal.service;

import au.edu.ardc.igsn.igsnportal.util.Helpers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VocabService {

	Logger log = LoggerFactory.getLogger(VocabService.class);
	private Map<String, String> lookupTable;

	@PostConstruct
	public void init() {
		log.trace("Initializing VocabService");
		lookupTable = new HashMap<>();
		loadLocalARDCv1Vocab();
	}

	/**
	 * Get the current lookup table Should already be initialized
	 * @return the current lookup table
	 */
	public Map<String, String> getLookupTable() {
		return lookupTable;
	}

	public boolean resolvable(String uri) {
		return lookupTable.containsKey(uri);
	}

	/**
	 * Get human readable terms for vocabulary uri
	 * @param uri the uri string of the vocab
	 * @return the human readable form of the vocab uri. returns raw uri if not found
	 */
	public String resolve(String uri) {
		log.debug("Resolving {}", uri);

		// attempt resolve using local look up table first
		if (!lookupTable.isEmpty() && lookupTable.containsKey(uri)) {
			log.debug("Term {} is found in local lookup table, returning value {}", uri, lookupTable.get(uri));
			return lookupTable.get(uri);
		}

		log.debug("No local term found for {} returning raw", uri);
		return uri;
	}

	/**
	 * Loading the local ardcv1.json file into the local lookup table the ardcv1.json file
	 * can be found in the classpath and contains all the label and value for each
	 * vocabulary term
	 */
	public void loadLocalARDCv1Vocab() {
		log.debug("Loading local ARDCv1 vocabulary");
		try {
			log.debug("Attempting to load ardcv1.json on classpath");
			String json = Helpers.readFileOnClassPath("ardcv1.json");
			ObjectMapper mapper = new ObjectMapper();
			Map<String, List<Map<String, String>>> data = mapper.readValue(json,
					new TypeReference<Map<String, List<Map<String, String>>>>() {
					});
			data.forEach((key, value) -> value.forEach(kv -> {
				if (lookupTable.get(kv.get("value")) != null) {
					log.warn("Value {} already existed in lookup table, overwriting", kv.get("value"));
				}
				lookupTable.put(kv.get("value"), kv.get("label"));
			}));
			log.info("Loading ardcv1 vocabulary completed, look-up table size: {}", lookupTable.size());
		}
		catch (IOException e) {
			log.error("Error encountered while loading local ARDCv1 vocabularies {}", e.getMessage());
			e.printStackTrace();
		}
	}

}
