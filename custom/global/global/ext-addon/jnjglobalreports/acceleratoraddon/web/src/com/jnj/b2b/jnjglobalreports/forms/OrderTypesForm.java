package com.jnj.b2b.jnjglobalreports.forms;

public class OrderTypesForm {
	
	/**
	 * Data class used to hold drop down select options and respective values. Holds the code identifier as well as the
	 * display name.
	 */
		protected final String code;
		protected final String name;

		public OrderTypesForm(final String code, final String name)
		{
			this.code = code;
			this.name = name;
		}

		public String getCode()
		{
			return code;
		}

		public String getName()
		{
			return name;
		}

}
