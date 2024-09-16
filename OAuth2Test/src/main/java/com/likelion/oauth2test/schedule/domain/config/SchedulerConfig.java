package com.likelion.oauth2test.schedule.domain.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Configuration;

import com.likelion.oauth2test.schedule.domain.MyJob;
import com.likelion.oauth2test.schedule.domain.MyJobListener;

import jakarta.annotation.PostConstruct;

/**
 * 스케줄러의 설정을 관리합니다.
 *
 * @author : lee
 * @fileName : ScheduleConfig
 * @since : 2/28/24
 */
@Configuration
public class SchedulerConfig {

	private Scheduler scheduler;

	public SchedulerConfig(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 스케줄러의 실제 처리 과정을 담당합니다.
	 */
	@PostConstruct
	private void jobProgress() throws SchedulerException {
		cronScheduler();
	}

	/**
	 * SimpleScheduler 구성 메서드
	 *
	 * @throws SchedulerException
	 */
	private void simpleScheduler() throws SchedulerException {
		// [STEP1] Job 생성
		JobDetail job = JobBuilder
			.newJob(MyJob.class)                                   // Job 구현 클래스
			.withIdentity("myJob", "myGroup")     // Job 이름, 그룹 지정
			.withDescription("FCM 처리를 위한 조회 Job")   // Job 설명
			.build();

		// [STEP2] Trigger 생성
		Trigger trigger = TriggerBuilder
			.newTrigger()
			.withIdentity("myTrigger", "myGroup")         // Trigger 이름, 그룹 지정
			.withDescription("FCM 처리를 위한 조회 Trigger")     // Trigger 설명
			.startNow()
			.withSchedule(
				SimpleScheduleBuilder
					.simpleSchedule()
					.withIntervalInSeconds(5)
					.repeatForever())
			.build();

		// [STEP3] 스케줄러 생성 및 Job, Trigger 등록
		scheduler = new StdSchedulerFactory().getScheduler();
		MyJobListener myJobListener = new MyJobListener();
		scheduler.getListenerManager().addJobListener(myJobListener);
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
	}

	/**
	 * CronScheduler 구성 메서드
	 */
	private void cronScheduler() throws SchedulerException {

		// [STEP1] Job 생성
		JobDetail job = JobBuilder
			.newJob(MyJob.class)                                   // Job 구현 클래스
			.withIdentity("myJob", "myGroup")     // Job 이름, 그룹 지정
			.withDescription("FCM 처리를 위한 조회 Job")   // Job 설명
			.build();

		CronTrigger cronTrigger = TriggerBuilder
			.newTrigger()
			.withIdentity("fcmSendTrigger", "fcmGroup")         // Trigger 이름, 그룹 지정
			.withDescription("FCM 처리를 위한 조회 Trigger")     // Trigger 설명
			.startNow()
			.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")).build();

		// [STEP3] 스케줄러 생성 및 Job, Trigger 등록
		scheduler = new StdSchedulerFactory().getScheduler();
		MyJobListener myJobListener = new MyJobListener();
		scheduler.getListenerManager().addJobListener(myJobListener);
		scheduler.start();
		scheduler.scheduleJob(job, cronTrigger);
	}

}

