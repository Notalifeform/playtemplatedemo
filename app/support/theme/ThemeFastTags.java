package support.theme;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.exceptions.TagInternalException;
import play.exceptions.TemplateExecutionException;
import play.exceptions.TemplateNotFoundException;
import play.mvc.Scope.RenderArgs;
import play.templates.BaseTemplate;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import play.templates.Template;
import support.TemplateCode;


@FastTags.Namespace("theme")
public class ThemeFastTags extends FastTags{

   
    public static void _themeInclude(final Map<?, ?> args, final Closure body, final PrintWriter out,
            final ExecutableTemplate template, final int fromLine) {
        Logger.debug("_themeInclude: args: %s, fromLine: %s", args, fromLine);

        Template t = null;
        try {
            t = getTemplate(args, template);
        } catch (final TemplateNotFoundException e) {
            String includeName = "<unknown>";
            if (args.get("arg") != null) {
                includeName = args.get("arg").toString();
            }
            throw new TemplateExecutionException(template.template, fromLine, "included file '" + includeName + "'not found", e);
        }
        if (t != null) {
            final Map<String, Object> newArgs = new HashMap<String, Object>();
            newArgs.putAll(template.getBinding().getVariables());
            newArgs.put("_isInclude", true);
            t.render(newArgs);
        }
    }

    public static void _themeExtends(final Map<?, ?> args, final Closure body, final PrintWriter out,
            final ExecutableTemplate template, final int fromLine) {
        Logger.debug("_themeExtends: args: %s, fromLine: %s", args, fromLine);

        Template t = null;
        try {
            t = getTemplate(args, template);
        } catch (final TemplateNotFoundException e) {
            String extendName = "<unknown>";
            if (args.get("arg") != null) {
                extendName = args.get("arg").toString();
            }
            throw new TemplateExecutionException(template.template, fromLine, "extended file '" + extendName + "'not found", e);
        }
        if (t != null && t instanceof BaseTemplate) {
            BaseTemplate.layout.set((BaseTemplate) t);
        }
    }

    private static Template getTemplate(final Map<?, ?> args, final ExecutableTemplate template) {
        if (!args.containsKey("arg") || args.get("arg") == null) {
            return null;
        }
       
        List<TemplateCode> pairs = determineCustomerTemplateCodePairs(template); 
          final String path = args.get("arg").toString();
        Logger.debug("_theme: Pairs are '%s'. Name is '%s'",  pairs, path);

        return TemplateLocator.getInstance().findAndLoad(pairs, path);
    }

  
   
    private static  List<TemplateCode> determineCustomerTemplateCodePairs(final ExecutableTemplate template) {   
    	List<TemplateCode> pairs = new ArrayList<TemplateCode>();
    	return determineVariable(template, "templateDefs", pairs.getClass());
    }

    
    
    protected static <T> T determineVariable(final ExecutableTemplate template, final String varName, final Class<T> clazz) {
        try {
            final Object obj = template.getBinding().getVariable(varName);
            if (obj != null) {
                return (T) obj;
            }
        } catch (final Exception e) {
            Logger.error(e, "unable to grab " + varName);
        }
        return null;
    }

}
