package au.edu.ardc.igsn.igsnportal;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestHelper {

	public static String readFile(String path) throws IOException {
		File file = new File(path);
		return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
	}

}
