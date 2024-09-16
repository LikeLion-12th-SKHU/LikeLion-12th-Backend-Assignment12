package com.likelion.oauth2test.schedule.domain;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 실제 데이터 처리를 위한 Job을 구성합니다.
 *
 * @author : soo
 * @fileName : MyJob
 * @since : 8/16/24
 */
public class MyJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		System.out.println("실제 수행하는 Job 입니다.");
	}
}
