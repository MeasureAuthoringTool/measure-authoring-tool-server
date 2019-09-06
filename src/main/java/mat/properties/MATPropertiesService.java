package mat.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The Class MATPropertiesService.
 */
public class MATPropertiesService {
	
	
	private static MATPropertiesService instance;

	/** The current release version. */
	private  static String currentReleaseVersion;
	
	/** The qmd version. */
	private  static String qmdVersion;
	
	
	
	public static MATPropertiesService get(){

		if(instance == null) {
			instance = new MATPropertiesService();

			Properties properties = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream stream = loader.getResourceAsStream("MAT.properties");
			try {
				properties.load(stream);
				instance.setCurrentReleaseVersion(properties.get("mat.measure.current.release.version").toString());
				instance.setQmdVersion(properties.get("mat.measure.current.qdm.version").toString());
			} catch (IOException e) {
				e.printStackTrace();
			}


		}

		return instance;
	}
	
	

	/**
	 * Gets the current release version.
	 *
	 * @return the current release version
	 */
	public String getCurrentReleaseVersion() {
		return currentReleaseVersion;
	}

	/**
	 * Sets the current release version.
	 *
	 * @param releaseVersion the new current release version
	 */
	public void setCurrentReleaseVersion(String releaseVersion) {
		
		currentReleaseVersion = releaseVersion;
		System.out.println("**************************CurrentRelase Version:  "+currentReleaseVersion);
	}

	/**
	 * Gets the qmd version.
	 *
	 * @return the qmd version
	 */
	public String getQmdVersion() {
		return qmdVersion;
	}

	/**
	 * Sets the qmd version.
	 *
	 * @param qdm_version the new qmd version
	 */
	public void setQmdVersion(String qdm_version) {
		qmdVersion = qdm_version;
	}
	
}
