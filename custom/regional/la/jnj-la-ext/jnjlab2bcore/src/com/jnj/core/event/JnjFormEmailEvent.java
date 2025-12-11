package com.jnj.core.event;

import com.jnj.core.dto.JnjFormDTO;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class JnjFormEmailEvent extends AbstractCommerceUserEvent {

    private transient JnjFormDTO jnjFormDTO;

	public JnjFormEmailEvent()
	{
		//empty constructor
	}

    public JnjFormEmailEvent(final JnjFormDTO dto)
    {
        this.setJnjFormDTO(dto);
    }

    public JnjFormDTO getJnjFormDTO()
    {
        return jnjFormDTO;
    }

    public void setJnjFormDTO(final JnjFormDTO jnjFormDTO)
    {
        this.jnjFormDTO = jnjFormDTO;
    }

}
