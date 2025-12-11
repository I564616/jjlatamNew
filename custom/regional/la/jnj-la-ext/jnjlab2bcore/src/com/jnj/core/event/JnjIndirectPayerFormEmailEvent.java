package com.jnj.core.event;

import com.jnj.core.dto.JnjFormDTO;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class JnjIndirectPayerFormEmailEvent extends AbstractCommerceUserEvent {

    private transient JnjFormDTO jnjFormDTO;

	public JnjIndirectPayerFormEmailEvent()
	{
		//empty constructor
	}

    public JnjIndirectPayerFormEmailEvent(final JnjFormDTO dto)
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
