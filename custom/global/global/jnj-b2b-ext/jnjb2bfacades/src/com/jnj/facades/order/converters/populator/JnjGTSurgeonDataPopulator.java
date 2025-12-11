/**
 *
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.jnj.facades.data.JnjGTSurgeonData;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * Populator class to convert and populates data bean <code>JnjGTSurgeryInfoData</code> with the values form the source
 * instance <code>JnjGTSurgeonModel</code>.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class JnjGTSurgeonDataPopulator implements Populator<JnjGTSurgeonModel, JnjGTSurgeonData>
{
	/**
	 * Sets values from source instance into the target instance.
	 */
	@Override
	public void populate(final JnjGTSurgeonModel source, final JnjGTSurgeonData target) throws ConversionException
	{
		if (source != null && target != null)
		{
			target.setCode(source.getSurgeonId());
			target.setCaseDate(source.getCaseDate());
			target.setCaseNumber(source.getCaseNumber());
			target.setInterbody(source.getInterBody());
			target.setLevelsInstrumented(source.getLevelsInstrumented());
			target.setOrthobiologics(source.getOrthoBio());
			target.setPathology(source.getPathology());
			target.setSpecialty(source.getSpeciality());
			target.setSurgicalApproach(source.getApproach());
			target.setZone(source.getZone());
			target.setHospitalId(source.getHospitalId());
			
			target.setFirstName(source.getFirstName());
			target.setMiddleName(source.getMiddleName());
			target.setLastName(source.getLastName());

			target.setCity(source.getCity());
			target.setState(source.getState());
			target.setCountry(source.getCountry());
			target.setPostalCode(source.getPostalCode());

			target.setDisplayName(JnjGTCoreUtil.formatValuesByCommaSeparated(source.getFirstName(), source.getMiddleName(),
					source.getLastName()));
			target.setDisplayAddress(JnjGTCoreUtil.formatValuesByCommaSeparated(source.getCity(), source.getState(),
					source.getCountry(), source.getPostalCode()));
		}
	}
}
