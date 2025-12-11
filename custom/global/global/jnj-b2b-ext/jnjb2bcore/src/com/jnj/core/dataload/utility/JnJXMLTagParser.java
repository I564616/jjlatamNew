/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dataload.utility;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



/**
 * This is a utility class responsible for parsing / reading the XML tags.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnJXMLTagParser
{
	/**
	 * Method : multipleChildrenTagReader Description : this method is responsible for fetching the Nodes that go by the
	 * 'tagName' specified, that lie within the 'parentElement' supplied
	 * 
	 * @param parentElement
	 *           - The parent element that will hold a tag having 'tagName'
	 * @param tagName
	 *           - The name of the tag that will lie within the 'parentElement'
	 * @return nodeList - List of all nodes having the tagName specified
	 */
	public static NodeList multipleChildrenTagReader(final Element parentElement, final String tagName)
	{
		// Returning the List of all nodes having the tagName specified, that lie within the parentElement
		return parentElement.getElementsByTagName(tagName);
	}

	/**
	 * Method : singleChildTagValueReader Description : this method is responsible for fetching the contained value of
	 * the particular 'tagName' which lies within the supplied 'parentElement'.
	 * 
	 * @param parentElement
	 *           - The parent element that will hold a tag having 'tagName'
	 * @param tagName
	 *           - The name of the tag that will lie within the 'parentElement'
	 * @return childTagValue - This is the text that lies within the tagName element so found.
	 */
	public static String singleChildTagValueReader(final Element parentElement, final String tagName)
	{
		String childTagValue = "";

		// List of all nodes having the specified tag name 
		final NodeList nodeList = parentElement.getElementsByTagName(tagName);

		if (null != nodeList)
		{
			// Assuming single node exists for the tagName specified
			final Element nodeListFirstChild = (Element) nodeList.item(0);
			if (null != nodeListFirstChild)
			{
				// Fetching child node of the single found tag as per tagName
				final NodeList child = nodeListFirstChild.getChildNodes();
				if (null != child)
				{
					// Fetching the value present in the node and trimming it
					childTagValue = child.item(0).getNodeValue().trim();
				}
			}
		}
		// Returning the text value
		return childTagValue;
	}
}
