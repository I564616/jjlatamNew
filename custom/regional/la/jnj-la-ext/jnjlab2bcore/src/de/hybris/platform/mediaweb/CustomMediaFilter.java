/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.mediaweb;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.exceptions.MediaInvalidLocationException;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.media.url.impl.LocalMediaWebURLStrategy;
import de.hybris.platform.servicelayer.media.impl.DefaultMediaService;
import de.hybris.platform.util.MediaUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 */
public class CustomMediaFilter extends MediaFilter
{
	private static final Logger LOG = Logger.getLogger(CustomMediaFilter.class);

	private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

	private static final String FORCE_DOWNLOAD_DIALOG_FILE_EXTENSIONS = "media.force.download.dialog.fileextensions";
	private static final Pattern FOLDER_PATTERN = Pattern
			.compile("(([\\w\\-]+?/)??)(([\\w\\-]+?/)??)((h\\w{2}/)*([0-9]+)(/[\\w\\-.]+)?(\\.\\w+?))", Pattern.CASE_INSENSITIVE);
	/**
	 * advanced.properties key for setting the allowed extensions when using load from jar (loading with the class
	 * loader)
	 */
	private static final String ALLOWED_EXT_FOR_CL = "media.allowed.extensions.for.ClassLoader";
	private static final String HEADER_ETAG = "ETag";
	private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
	private static final Splitter CTX_SPLITTER = Splitter.on(LocalMediaWebURLStrategy.CONTEXT_PARAM_DELIM);

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse)
		{
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			final HttpServletResponse httpResponse = new SecureResponseWrapper((HttpServletResponse) response);

			try
			{
				doFilterMedia(httpRequest, httpResponse, getMediaContext(httpRequest));
			}
			catch (final IllegalArgumentException e)
			{
				sendBadRequestResponseStatus(httpResponse, e);
			}
		}
		else
		{
			chain.doFilter(request, response);
		}
	}

	private void addContentDisposition(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse,
			final String resourcePath)
	{

		if (isAddContentDisposition(httpRequest, resourcePath))
		{
			httpResponse.addHeader("Content-Disposition", " attachment; filename=" + getRealFileNameFromResource(resourcePath));
		}
	}


	private boolean isAddContentDisposition(final HttpServletRequest httpRequest, final String resourcePath)
	{
		final boolean addContentDisposition = Boolean
				.parseBoolean(httpRequest.getParameter(LocalMediaWebURLStrategy.ATTACHEMENT_PARAM));

		if (!addContentDisposition)
		{
			final Set<String> extensions = getAllowedExtensions(FORCE_DOWNLOAD_DIALOG_FILE_EXTENSIONS);
			if (extensions != null && !extensions.isEmpty())
			{
				final String lowerCaseResource = resourcePath.toLowerCase();
				for (final String ext : extensions)
				{
					if (lowerCaseResource.endsWith(ext))
					{
						return true;
					}
				}
			}
		}

		return addContentDisposition;
	}


	private Set<String> getAllowedExtensions(final String configParameter)
	{
		final Set<String> extensions;

		final String str = getConfig().getParameter(configParameter);
		if (StringUtils.isBlank(str))
		{
			extensions = Collections.emptySet();
		}
		else
		{
			final Set<String> set = new LinkedHashSet<String>();
			for (final StringTokenizer tok = new StringTokenizer(str, ",;"); tok.hasMoreTokens();)
			{
				final String strElement = tok.nextToken().toLowerCase().trim();
				if (strElement.length() > 0)
				{
					set.add(strElement);
				}
			}
			extensions = Collections.unmodifiableSet(set);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(configParameter + ": Supported media extensions: " + extensions.toString());
		}

		return extensions;
	}

	private void processStandardResponse(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse,
			final Iterable<String> mediaContext) throws IOException, ServletException
	{
		final String resourcePath = getResourcePath(httpRequest);

		readConfiguredHeaderParamsAndWriteToResponse(httpResponse);
		addContentDisposition(httpRequest, httpResponse, resourcePath);
		addContentType(httpResponse, mediaContext, resourcePath);
		loadFromMediaStorage(httpResponse, mediaContext);
	}

	private static String getResourcePath(final HttpServletRequest httpRequest)
	{
		String resourcePath = httpRequest.getServletPath();
		if (GenericValidator.isBlankOrNull(resourcePath))
		{
			//ok, it's maybe Websphere, because on websphere the getServletPath() is always null or ''
			final String reqURI = httpRequest.getRequestURI();
			final String ctxPath = httpRequest.getContextPath();
			resourcePath = reqURI.replace(ctxPath, "");
		}

		return resourcePath;
	}

	/**
	 * Reads the defined http header parameters from the properties into the map and writes to <code>httpResponse</code>.
	 * And also register a config change listener to get the properties changes during runtime.
	 *
	 * @throws UnsupportedEncodingException
	 */
	private void readConfiguredHeaderParamsAndWriteToResponse(final HttpServletResponse httpResponse)
			throws UnsupportedEncodingException
	{
		final MediaHeadersRegistry mediaHeadersRegistry = getMediaManager().getMediaHeadersRegistry();

		if (mediaHeadersRegistry != null)
		{
			final Map<String, String> headerParams = mediaHeadersRegistry.getHeaders();
			for (final Map.Entry<String, String> me : headerParams.entrySet())
			{
				httpResponse.setHeader(me.getKey(), me.getValue());
			}
		}
	}

	private void loadFromMediaStorage(final HttpServletResponse httpResponse, final Iterable<String> mediaContext)
			throws IOException
	{
		InputStream inputStream = null;
		try
		{
			final String folderQualifier = getContextPart(ContextPart.FOLDER, mediaContext);
			final String size = getContextPart(ContextPart.SIZE, mediaContext);
			final String location = getContextPart(ContextPart.LOCATION, mediaContext);
			final String locationHash = getContextPart(ContextPart.LOCATION_HASH, mediaContext);

			if (!LocalMediaWebURLStrategy.NO_CTX_PART_MARKER.equals(locationHash))
			{
				verifyHashForLocation(folderQualifier, location, locationHash);
			}

			if (size != null)
			{
				httpResponse.setContentLength(Integer.parseInt(size));
			}

			inputStream = getMediaAsStream(folderQualifier, location);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Loading resource [location: " + location + "] from media storage.");
			}
			IOUtils.copy(inputStream, httpResponse.getOutputStream());
		}
		catch (final MediaInvalidLocationException e)
		{
			sendForbiddenResponseStatus(httpResponse, e);
		}
		catch (final MediaNotFoundException e)
		{
			sendResourceNotFoundResponseStatus(httpResponse, e);
		}
		catch (final Exception e)
		{
			sendBadRequestResponseStatus(httpResponse, e);
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
			unsetCurrentTenant();
		}
	}

	private enum ContextPart
	{
		TENANT(0), FOLDER(1), SIZE(2), MIME(3), LOCATION(4), LOCATION_HASH(5);

		private final int partNumber;

		ContextPart(final int partNumber)
		{
			this.partNumber = partNumber;
		}

		public int getPartNumber()
		{
			return partNumber;
		}
	}


	private static String getContextPart(final ContextPart contextPart, final Iterable<String> mediaContext)
	{
		return Iterables.get(mediaContext, contextPart.getPartNumber());
	}

	private void verifyHashForLocation(final String folderQualifier, final String location, final String storedHash)
	{
		getMediaManager().verifyMediaHashForLocation(folderQualifier, location, storedHash);
	}

	private InputStream getMediaAsStream(final String folderQualifier, final String location)
	{
		return getMediaManager().getMediaAsStream(folderQualifier, location);
	}

	@Override
	protected MediaManager getMediaManager()
	{
		return MediaManager.getInstance();
	}


	private static void sendForbiddenResponseStatus(final HttpServletResponse httpResponse, final Exception exception)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Access forbidden for given media", exception);
		}

		httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	private static void sendBadRequestResponseStatus(final HttpServletResponse httpResponse, final Exception exception)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("The request sent by the client was syntactically incorrect", exception);
		}

		httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	private static void sendResourceNotFoundResponseStatus(final HttpServletResponse httpResponse, final Exception exception)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Requested resource not found", exception);
		}

		httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

	private void doFilterMedia(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse,
			final Iterable<String> mediaContext) throws IOException, ServletException
	{
		setCurretTenantByID(mediaContext);
		if (getMediaManager().isSecuredFolder(getContextPart(ContextPart.FOLDER, mediaContext)))
		{
			//in case someone tries to guess the url using the non secured one, but the media is still in secured folder.
			httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		else
		{
			final String resourcePath = getResourcePath(httpRequest);

			if (isResourceFromClassLoader(resourcePath))
			{
				loadFromClassLoader(httpResponse, resourcePath);
			}
			else
			{
				final String responseETag = generateETag(getContextPart(ContextPart.LOCATION, mediaContext));

				if (responseETag == null)
				{
					processStandardResponse(httpRequest, httpResponse, mediaContext);
				}
				else
				{
					httpResponse.setHeader(HEADER_ETAG, responseETag);
					final String requestETag = httpRequest.getHeader(HEADER_IF_NONE_MATCH);

					if (responseETag.equals(requestETag))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
						}

						readConfiguredHeaderParamsAndWriteToResponse(httpResponse);
						httpResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					}
					else
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("ETag [" + responseETag + "] is not equal to If-None-Match, sending standard response");
						}

						processStandardResponse(httpRequest, httpResponse, mediaContext);
					}
				}
			}
		}
	}

	private Iterable<String> getMediaContext(final HttpServletRequest httpRequest)
	{
		final String encodedMediaCtx = getLocalMediaWebUrlContextParam(httpRequest);
		if (isLegacyPrettyUrlSupport())
		{
			return createLegacyLocalMediaWebUrlContext(httpRequest);
		}
		else
		{
			return createLocalMediawebUrlContext(encodedMediaCtx);
		}
	}

	private boolean isResourceFromClassLoader(final String resourcePath)
	{
		return resourcePath != null && resourcePath.contains("/" + DefaultMediaService.FROM_JAR);
	}

	private static String getRealFileNameFromResource(final String resourcePath)
	{
		final int index = resourcePath.lastIndexOf('/');
		return (index >= 0) ? resourcePath.substring(index + 1) : resourcePath;
	}

	private void loadFromClassLoader(final HttpServletResponse httpResponse, final String resourcePath) throws IOException
	{
		final String resourceName = resourcePath
				.substring(resourcePath.indexOf("/" + DefaultMediaService.FROM_JAR) + (1 + DefaultMediaService.FROM_JAR.length()));

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Trying to load resource '" + resourceName + "' from classloader.");
		}

		if (isDeniedByExtensionForClassloader(resourceName))
		{
			httpResponse.setContentType(CONTENT_TYPE_TEXT_PLAIN);
			httpResponse.getOutputStream()
					.println("not allowed to load media '" + resourceName + "' from classloader. Check parameter " + ALLOWED_EXT_FOR_CL
							+ " in advanced.properties to change the file extensions (e.g. *.gif) that are allowed to download.");
		}
		else
		{
			InputStream inputStream = null;
			try
			{
				inputStream = getResourceAsStream(resourceName);
				if (inputStream == null)
				{
					httpResponse.setContentType(CONTENT_TYPE_TEXT_PLAIN);
					httpResponse.getOutputStream().println("file '" + resourceName + "' not found!");
				}
				else
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Loading resource '" + resourceName + "' from classloader.");
					}
					IOUtils.copy(inputStream, httpResponse.getOutputStream());
				}
			}
			finally
			{
				IOUtils.closeQuietly(inputStream);
			}
		}
	}

	private static String generateETag(final String location)
	{
		final HashFunction md5 = Hashing.md5();
		return md5.hashUnencodedChars(location).toString();
	}

	private String getLocalMediaWebUrlContextParam(final HttpServletRequest httpRequest)
	{
		return httpRequest.getParameter(LocalMediaWebURLStrategy.CONTEXT_PARAM);
	}

	private Iterable<String> createLegacyLocalMediaWebUrlContext(final HttpServletRequest httpRequest)
	{
		String resourcePath = getLegacyResourcePath(httpRequest);
		if (resourcePath.startsWith("/"))
		{
			resourcePath = resourcePath.substring(1);
		}

		return splitLegacyPath(resourcePath);
	}

	private static Iterable<String> createLocalMediawebUrlContext(final String encodedMediaCtx)
	{
		Preconditions.checkArgument(!GenericValidator.isBlankOrNull(encodedMediaCtx), "incorrect media context in request");
		final Iterable<String> mediaContext = CTX_SPLITTER.split(decodeBase64(encodedMediaCtx));
		Preconditions.checkArgument(Iterables.size(mediaContext) == 6, "incorrect media context in request");
		return mediaContext;
	}


	/**
	 * Returns <code>true</code> if the extension of given resource name is <b>NOT</b> defined in the advanced.properties
	 * with the key "media.allowed.extensions.for.ClassLoader". This does only applies when getting the resources with
	 * the class loader.
	 *
	 * @return <code>false</code> if no extension is configured (every extension is allowed) or the extension of given
	 *         resource is configured, otherwise <code>true</code>
	 */
	private boolean isDeniedByExtensionForClassloader(final String resourceName)
	{
		final Set<String> extensions = getAllowedExtensions(ALLOWED_EXT_FOR_CL);
		if (extensions == null || extensions.isEmpty())
		{
			return false;
		}

		final String check = resourceName.toLowerCase();
		for (final String ext : extensions)
		{
			if (check.endsWith(ext))
			{
				return false;
			}
		}
		return true;
	}

	private static String getLegacyResourcePath(final HttpServletRequest httpRequest)
	{
		String resourcePath = httpRequest.getServletPath();
		if (resourcePath == null || resourcePath.trim().isEmpty())
		{
			//ok, it's maybe Websphere, because on websphere the getServletPath() is always null or ''
			final String reqURI = httpRequest.getRequestURI();
			final String ctxPath = httpRequest.getContextPath();
			resourcePath = reqURI.replace(ctxPath, "");
		}

		return resourcePath;
	}

	protected Iterable<String> splitLegacyPath(final String path)
	{
		final String tenant = getTenantNameFromLegacyUrl(path);
		final String pathExp = getPathFromLegacyUrl(path);
		final Matcher matcher = FOLDER_PATTERN.matcher(pathExp);

		if (matcher.matches())
		{
			final String folderQualifier = determineFolderQualifierFromPartOfPath(matcher.group(1));
			final String folderPath = determineFolderPathFromPartOfPath(matcher.group(3));
			final String locationWithRealFileName = matcher.group(5);
			final String realFileName = matcher.group(8);
			final String location = realFileName != null ? locationWithRealFileName.replace(realFileName, "")
					: locationWithRealFileName;

			final List<String> result = new ArrayList<>(6);
			result.add(tenant);
			result.add(MediaUtil.removeTrailingFileSepIfNeeded(folderQualifier));
			result.add(null);
			result.add(LocalMediaWebURLStrategy.NO_CTX_PART_MARKER);
			result.add(MediaUtil.addTrailingFileSepIfNeeded(folderPath) + location);
			result.add(LocalMediaWebURLStrategy.NO_CTX_PART_MARKER);

			return result;
		}
		else
		{
			throw new IllegalArgumentException("invalid legacy media path '" + path + "'");
		}
	}

	private static String decodeBase64(final String value)
	{
		String decodedValue = "";
		if (StringUtils.isNotBlank(value))
		{
			try
			{
				decodedValue = new String(new Base64(-1, null, true).decode(value));
			}
			catch (final Exception e)
			{
				throw new RuntimeException("Cannot decode base32 coded string: " + value, e);
			}
		}
		return decodedValue;
	}

	private static String getTenantNameFromLegacyUrl(final String url)
	{
		Preconditions.checkArgument(url.startsWith("sys_"), "invalid legacy media path '" + url + "'");

		final int slashAfterTenant = url.indexOf('/');
		final String tenantExpr = url.substring(0, slashAfterTenant);
		return tenantExpr.substring("sys_".length());
	}

	private static String getPathFromLegacyUrl(final String url)
	{
		return url.substring(url.indexOf('/') + 1);
	}

	private String determineFolderQualifierFromPartOfPath(final String part)
	{
		return StringUtils.isEmpty(part) ? MediaManager.ROOT_FOLDER_QUALIFIER : part;
	}

	private String determineFolderPathFromPartOfPath(final String part)
	{
		return MediaManager.ROOT_FOLDER_QUALIFIER.equals(MediaUtil.removeTrailingFileSepIfNeeded(part)) ? StringUtils.EMPTY : part;
	}

}
