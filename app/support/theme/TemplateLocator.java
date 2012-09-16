package support.theme;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import play.Logger;
import play.Play;
import play.exceptions.TemplateNotFoundException;
import play.templates.BaseTemplate;
import play.templates.Template;
import support.Configuration;
import support.TemplateCode;

public class TemplateLocator {

	private static TemplateLocator instance = null;

	private final String externalTemplatePath;
	private final ConcurrentHashMap<String, PathResult> templatePathCache;
	private final boolean isUseCache;

	private TemplateLocator(final boolean isUseCache) {
		externalTemplatePath = Configuration.getExternalTemplatePath();
		templatePathCache = new ConcurrentHashMap<String, PathResult>();
		this.isUseCache = isUseCache;
	}

	public String getExternalTemplatePath() {
		return externalTemplatePath;
	}

	public static TemplateLocator getInstance() {
		if (instance == null) {
			instance = new TemplateLocator(Play.mode.isProd());
		}
		return instance;
	}

	/* package local */static TemplateLocator getNewInstance(
			final boolean isUseCache) {
		instance = new TemplateLocator(isUseCache);
		return instance;
	}

	public void clearTemplateCache(final String customer, final String template) {
		final String keyPartOfCustomerTemplate = String.format("%s-%s",
				customer, template);
		for (final Entry<String, PathResult> entry : templatePathCache
				.entrySet()) {
			// Contains() - because key has 2 pairs of customer/template
			// example: bm-gp-generic-booster-laiphone-Page/main.html
			if (entry.getKey().contains(keyPartOfCustomerTemplate)) {
				templatePathCache.remove(entry.getKey());
			}
		}
	}

	public void resetTemplateCache() {
		templatePathCache.clear();
	}

	public Template findAndLoad(final List<TemplateCode> pairs,
			final String templatePath) {
		final String fullPath = find(pairs, templatePath);
		return ThemeTemplateLoader.loadExternalTemplate(fullPath);
	}

	public Template load(final TemplateCode pair,
			final String templateName) {
		final String fullPath = new PathResolver().generatePath(
				externalTemplatePath, pair, templateName);
		return ThemeTemplateLoader.loadExternalTemplate(fullPath);
	}

	public String find(final List<TemplateCode> pairs,
			final String templateName) {
		final String templatePath = getAbsoluteInThemePath(templateName);

		final String cacheKey = getKey(pairs, templatePath);

		if (isUseCache) {
			final PathResult cachedPathResult = templatePathCache.get(cacheKey);
			if (cachedPathResult != null) {
				Logger.debug("return file from cache %s", cachedPathResult);
				return cachedPathResult.getFullPath();
			}
		}

		final PathResult pathResult = new PathResolver().resolvePath(
				externalTemplatePath, pairs, templatePath);

		if (pathResult == null) {
			throw new TemplateNotFoundException(
					String.format(
							"Cannot find template '%s' for %s with externalTemplatePath '%s'",
							templatePath, pairs, externalTemplatePath));
		}

		if (isUseCache) {
			templatePathCache.put(cacheKey, pathResult);
		}

		return pathResult.getFullPath();
	}

	private String getKey(final List<TemplateCode> pairs,
			 final String templatePath) {
		final StringBuilder sb = new StringBuilder();
		for (final TemplateCode pair : pairs) {
			sb.append("-")
					.append(pair.getCode()).append("-");
		}
		sb.append("-").append(templatePath);
		return sb.toString();
	}

	private String getAbsoluteInThemePath(final String templateName) {
		if (!templateName.startsWith("./")) {
			return templateName;
		}

		Logger.debug("relative path '%s'", templateName);
		final String currentTemplateFullPath = BaseTemplate.currentTemplate
				.get().name;
		Logger.debug("externalTemplatePath '%s'", externalTemplatePath);
		Logger.debug("currentTemplateFullPath = %s", currentTemplateFullPath);

		final String absoluteInThemePath = currentTemplateFullPath.replaceAll(
				externalTemplatePath, "").replaceAll(
				"(customer-\\w+)?/(template-\\w+)?/", "");

		final String folderInTheme = absoluteInThemePath.substring(0,
				absoluteInThemePath.lastIndexOf("/"));
		// //////////////////////////////// remove '.' from start of string
		final String path = folderInTheme + templateName.substring(1);
		Logger.debug("absolute in theme path: %s", path);
		return path;
	}

}
