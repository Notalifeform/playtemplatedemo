package support;


import play.Logger;
import play.Play;

public class Configuration {

   
    private static String externalTemplatePath;

    /**
     * Return the template  path of our templates.
     * 
     * @return
     */
    public static String getExternalTemplatePath() {
        if (externalTemplatePath == null) {
            // the default root is used for running our front-end tests
            Configuration.externalTemplatePath = Play.configuration.getProperty("external.template.path",
                    Play.applicationPath + "/data/test-views/");
            Logger.info("setting external template path to %s", externalTemplatePath);
        }
        return externalTemplatePath;
    }

}
