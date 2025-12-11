/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.versioning.constants;

/**
 * Global class for all Jnjlaversioning constants. You can add global constants for your extension into this class.
 */
public final class JnjlaversioningConstants extends GeneratedJnjlaversioningConstants {
	public static final String EXTENSIONNAME = "jnjlaversioning";

    public static final String VERSION = "VERSION";
    public static final String VERSION_LABEL = "Which version should be executed?";

    public static final String PATH = "/jnjlaversioning/import/versions/%s/";
	public static final String PATH_PATTERN = "jnjlaversioning/import/versions/%s/*.impex";

	public static final String SYNCHRONIZE_CONTENT_CATALOGS = "SYNCHRONIZE_CONTENT_CATALOGS";
	public static final String SYNCHRONIZE_CONTENT_CATALOGS_LABEL = "Synchronize Content Catalogs?";

	private JnjlaversioningConstants() {
	}
}
