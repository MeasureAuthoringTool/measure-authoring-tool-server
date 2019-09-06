package mat.measure;

import java.util.ArrayList;
import java.util.List;

public class MeasureUtil {
    private MeasureUtil(){}
    private static MeasureUtil instance;
    public static MeasureUtil getInstance() {
        if(instance == null) {
            instance = new MeasureUtil();
        }
        return instance;
    }
    

    
    public boolean isCQLMeasure(String releaseVersion) {
        if(releaseVersion == null) {
            return false;
        }
        String str[] = releaseVersion.replace("v", "").split("\\.");
        int versionInt = Integer.parseInt(str[0]);
        if(versionInt<5){
            return false;
        }
        return true;
    }

    public String getTextSansOid(String text) {
        if (text == null) {
            return text;
        }
        int d = text.lastIndexOf('-');
        int c = text.lastIndexOf(':');
        if ((d > 0) && (d > c)) {
            return text.substring(0, d);
        } else {
            return text;
        }
    }

    public List<String> getAllowedPopulationsInPackage(){

        List<String> allowedPopulationsInPackage = new ArrayList<String>();

        allowedPopulationsInPackage.add("initialPopulation");
        allowedPopulationsInPackage.add("stratification");
        allowedPopulationsInPackage.add("measurePopulation");
        allowedPopulationsInPackage.add("measurePopulationExclusions");
        allowedPopulationsInPackage.add("measureObservation");
        allowedPopulationsInPackage.add("denominator");
        allowedPopulationsInPackage.add("denominatorExclusions");
        allowedPopulationsInPackage.add("denominatorExceptions");
        allowedPopulationsInPackage.add("numerator");
        allowedPopulationsInPackage.add("numeratorExclusions");
        return allowedPopulationsInPackage;
    }
}
