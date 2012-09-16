package support.theme;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import play.Logger;

import support.TemplateCode;

public class PathResolver {

	public PathResolver() {
	}


	/**
	 * Looking for path only in themes of template pairs
	 * 
	 * @param externalPath
	 * @param pairs
	 * @param fileName
	 * @return
	 */
	public PathResult resolvePath(final String externalPath,
			final List<TemplateCode> pairs,
			final String fileName) {
		return resolvePath(externalPath, pairs,  false, fileName);
	}

	/**
	 * Recursively looking for path: at first in first pair of
	 * CustomerTemplateCodePair and themes If not find yet, do same for second
	 * pair of CustomerTemplateCodePair.
	 * 
	 * @param externalPath
	 * @param pairs
	 *            of customerCode and templateCode
	 * @param doResolveWithoutThemeAsWell
	 *            if true also looking for path in template root if search in
	 *            themes is fails
	 * @param fileName
	 *            absolute from template/theme path, for example
	 *            /static/style/iphone.css or /Page/main.html
	 * @return PathResult or null if could not find
	 */
	public PathResult resolvePath(final String externalPath,
			final List<TemplateCode> pairs,final boolean doResolveWithoutThemeAsWell, final String fileName) {

	
		Logger.debug("resolvePair: %s, path: %s", pairs, 
				fileName);

		for (final TemplateCode pair : pairs) {
				 PathResult pathResult = checkPath(externalPath, pair,
						fileName);
				if (pathResult != null) {
					return pathResult;
			}
			if (doResolveWithoutThemeAsWell) {
				pathResult = checkPath(externalPath, pair,
						fileName);
				if (pathResult != null) {
					return pathResult;
				}
			}
		}
		return null;
	}

	private PathResult checkPath(final String externalPath,
			final TemplateCode pair, 
			final String fileName) {
		final String generatedPath = generatePath(externalPath, pair,
				fileName);

		final boolean result = pathExists(generatedPath);
		if (result) {
			return new PathResult(generatedPath, externalPath,
					 pair.getCode(), fileName);
		}
		return null;
	}

	public String generatePath(final String externalPath,
			final TemplateCode pair, 
			final String resourcePath) {
		return String.format("%stemplate-%s/%s",
				externalPath, pair.getCode(), 
				resourcePath);
	}

	

	private boolean pathExists(final String path) {
		final File file = new File(path);
		Logger.debug("checkPath is %s", file.getPath());
		return file.exists();
	}

}
