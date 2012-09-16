package support.theme;

import java.io.File;
import java.util.Map;

import play.Logger;
import play.exceptions.TemplateNotFoundException;
import play.libs.IO;
import play.templates.BaseTemplate;
import play.templates.GroovyTemplate;
import play.templates.GroovyTemplateCompiler;
import play.templates.TemplateLoader;

public class ThemeTemplateLoader extends TemplateLoader {

  
    public static Map<String, BaseTemplate> showCache() {
        for (final BaseTemplate bt : templates.values()) {
            Logger.info("%s", bt.name);
        }
        return templates;
    }

    /**
     * Load a template from a String
     * 
     * @param completePath
     *            A unique identifier for the template, used for retrieving a
     *            cached template
     * @return A Template
     */
    public static BaseTemplate loadExternalTemplate(final String completePath) {
        String source = null;
        final File completePathFile = new File(completePath);

        if (!templates.containsKey(completePath) || templates.get(completePath).compiledTemplate == null) {
            // read the source here...
            if (completePathFile.exists()) {
                source = IO.readContentAsString(completePathFile);
                final BaseTemplate template = new GroovyTemplate(completePath, source);
                if (template.loadFromCache()) {
                    templates.put(completePath, template);
                } else {
                    templates.put(completePath, new GroovyTemplateCompiler().compile(template));
                }
            }
        } else {

            if (// (Play.mode == Play.Mode.DEV) || // weird - play code always
                // compiles in DEV mode ?!?!
            (completePathFile.lastModified() > templates.get(completePath).timestamp)) {
                source = IO.readContentAsString(completePathFile);
                final BaseTemplate template = new GroovyTemplate(completePath, source);
                templates.put(completePath, new GroovyTemplateCompiler().compile(template));

            }
        }
        if (templates.get(completePath) == null) {
            throw new TemplateNotFoundException(completePath);
        }
        return templates.get(completePath);
    }

}
