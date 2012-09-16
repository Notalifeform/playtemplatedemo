package controllers;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.Play;
import play.data.validation.Validation;
import play.exceptions.JavaExecutionException;
import play.exceptions.PlayException;
import play.exceptions.TemplateException;
import play.exceptions.TemplateNotFoundException;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Scope;
import play.mvc.With;
import play.mvc.Scope.RenderArgs;
import play.mvc.results.RenderTemplate;
import play.templates.Template;
import support.TemplateCode;
import support.theme.TemplateLocator;

public class Page extends Controller {

	@Before
	public static void initTheme() {
			
		List<TemplateCode> templateDefs = new ArrayList<TemplateCode>();
		// define templates, from most important (site1) to fall back (base)
	 	templateDefs.add(new TemplateCode("site1"));
	    templateDefs.add(new TemplateCode("base"));

		 RenderArgs.current().put("templateDefs", templateDefs);
		
	    }

	public static void showStatic(String pageCode) {
		Logger.info("showStatic: pageCode=%s",  pageCode);
		if (pageCode == null) {
			pageCode = "index";
		}


		renderTemplate( pageCode + ".html");
	}


	private static void renderTemplate(final String templatePath) {

		List<TemplateCode> pairs = new ArrayList<TemplateCode>();
		pairs = renderArgs.get("templateDefs", pairs.getClass());

		Template template = TemplateLocator.getInstance().findAndLoad(pairs, templatePath);

		initRenderArgsForRenderTemplate();
		Logger.debug("render template %s", template.getName());
		throw new RenderTemplate(template, renderArgs.data);
	}

	
	private static void initRenderArgsForRenderTemplate() {
		renderArgs.put("session", Scope.Session.current());
		renderArgs.put("request", Http.Request.current());
		renderArgs.put("flash", Scope.Flash.current());
		renderArgs.put("params", Scope.Params.current());
		renderArgs.put("errors", Validation.errors());
	}

	
}