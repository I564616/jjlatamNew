package com.pac.aera.job.service;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gt.pac.aera.constants.Jnjgtb2bpacConstants.PAC_AERA_DAILY_EMAIL_CRON_JOBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created 21:02 18 December 2019.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultJnjDeliveryDateUpdateServiceTest {
	@Mock
	private CronJobService cronJobService;
	@Mock
	private ConfigurationService configurationService;

	@InjectMocks
	private JnjDeliveryDateUpdateService jnjDeliveryDateUpdateService = new DefaultJnjDeliveryDateUpdateService();

	@Nonnull
	private List<CronJobModel> mockEmailCronJobs(@Nullable final String... cronJobCodes) {

		final List<CronJobModel> cronJobModels = new ArrayList<>();
		if (null != cronJobCodes) {
			for (String cronJobCode : cronJobCodes) {
				final CronJobModel cronJobModel = Mockito.mock(CronJobModel.class);
				when(cronJobModel.getCode()).thenReturn(cronJobCode);

				cronJobModels.add(cronJobModel);
			}
		}

		final Configuration configuration = Mockito.mock(Configuration.class);

		when(this.configurationService.getConfiguration()).thenReturn(configuration);

		final String join = cronJobModels.stream().map(CronJobModel::getCode).collect(Collectors.joining(","));
		when(configuration.getString(PAC_AERA_DAILY_EMAIL_CRON_JOBS, StringUtils.EMPTY)).thenReturn(join);

		for (CronJobModel cronJobModel : cronJobModels) {
			when(this.cronJobService.getCronJob(cronJobModel.getCode())).thenReturn(cronJobModel);
		}

		return cronJobModels;
	}

	@Test
	public void runEmailCronJob_OneJob() {
		final List<CronJobModel> cronJobModels = this.mockEmailCronJobs("1");

		this.jnjDeliveryDateUpdateService.runEmailCronJob();

		verify(this.cronJobService, times(1)).performCronJob(any(CronJobModel.class));

		for (CronJobModel cronJobModel : cronJobModels) {
			verify(this.cronJobService).performCronJob(cronJobModel);
		}
	}

	@Test
	public void runEmailCronJob_multipleJobs() {
		final List<CronJobModel> cronJobModels = this.mockEmailCronJobs("1", "2", "3");

		this.jnjDeliveryDateUpdateService.runEmailCronJob();

		verify(this.cronJobService, times(3)).performCronJob(any(CronJobModel.class));

		for (CronJobModel cronJobModel : cronJobModels) {
			verify(this.cronJobService).performCronJob(cronJobModel);
		}
	}

	@Test
	public void runEmailCronJob_JobNotFound() {
		final List<CronJobModel> cronJobModels = this.mockEmailCronJobs("1", "2", "3");

		final Configuration configuration = Mockito.mock(Configuration.class);
		when(this.configurationService.getConfiguration()).thenReturn(configuration);

		when(configuration.getString(PAC_AERA_DAILY_EMAIL_CRON_JOBS, StringUtils.EMPTY)).thenReturn("");

		this.jnjDeliveryDateUpdateService.runEmailCronJob();

		verify(this.cronJobService, never()).performCronJob(any(CronJobModel.class));
	}

	@Test
	public void runEmailCronJob_JobSearchError() {
		final List<CronJobModel> cronJobModels = this.mockEmailCronJobs("1", "2", "3");

		when(this.cronJobService.getCronJob(anyString())).thenThrow(new RuntimeException("Test error!"));

		this.jnjDeliveryDateUpdateService.runEmailCronJob();

		verify(this.cronJobService, never()).performCronJob(any(CronJobModel.class));
	}

	@Test
	public void runEmailCronJob_JobPerformError() {
		final List<CronJobModel> cronJobModels = this.mockEmailCronJobs("1", "2", "3");

		doThrow(new RuntimeException("Test error!")).when(this.cronJobService).performCronJob(any(CronJobModel.class));

		this.jnjDeliveryDateUpdateService.runEmailCronJob();

		verify(this.cronJobService, times(3)).performCronJob(any(CronJobModel.class));

		for (CronJobModel cronJobModel : cronJobModels) {
			verify(this.cronJobService).performCronJob(cronJobModel);
		}
	}

	@Test
	public void runEmailCronJob_BlankJobCode() {
		final List<CronJobModel> cronJobModels = this.mockEmailCronJobs("1", "2", "3", " ");

		this.jnjDeliveryDateUpdateService.runEmailCronJob();

		verify(this.cronJobService, times(3)).performCronJob(any(CronJobModel.class));

		for (CronJobModel cronJobModel : cronJobModels) {
			if (StringUtils.isBlank(cronJobModel.getCode())) {
				continue;
			}

			verify(this.cronJobService).performCronJob(cronJobModel);
		}
	}
}
