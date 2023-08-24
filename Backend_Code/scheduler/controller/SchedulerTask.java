package com.scheduler.controller;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.entity.Template;
import com.scheduler.helper.ImportFile;

import com.scheduler.repository.TemplateRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SchedulerTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerTask.class);

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private ImportFile importFile;

	@Scheduled(cron = "0 * * * * *")
	public void performTaskUsingCron() {
		LOGGER.info("Searching for task to download");

		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		String formattedCurrentDateTime = formatDateTime(currentDateTime, "yyyy-MM-dd HH:mm");
		LOGGER.info("Current IST time: {}", formattedCurrentDateTime);

		Flux<Template> tasks = templateRepository.findByJobStatus("S");

		tasks.filter(task -> task.getRunTime() != null).filter(this::isCurrentDateTimeEqualsTaskRunTime)
				.flatMap(task -> executeTask(task).then(Mono.just(task))).flatMap(templateRepository::save).subscribe();
	}

	private boolean isCurrentDateTimeEqualsTaskRunTime(Template task) {
		ZonedDateTime taskRunTime = task.getRunTime().atZone(ZoneId.of("Asia/Kolkata")).minusHours(5).minusMinutes(30);
		String formattedTaskRunTime = formatDateTime(taskRunTime.toLocalDateTime(), "yyyy-MM-dd HH:mm");
		String formattedCurrentDateTime = formatDateTime(LocalDateTime.now(ZoneId.of("Asia/Kolkata")),
				"yyyy-MM-dd HH:mm");
		LOGGER.info("Task run time: {}", formattedTaskRunTime);
		LOGGER.info("Current date time equals task run time: {}",
				formattedCurrentDateTime.equals(formattedTaskRunTime));
//        return formattedCurrentDateTime.equals(formattedTaskRunTime); //or greater
		return formattedCurrentDateTime.compareTo(formattedTaskRunTime) >= 0;

	}

	private Mono<Void> executeTask(Template task) {

		String dataFile = task.getFileName();

		try {
			importFile.download(dataFile);
		} catch (IOException e) {
			LOGGER.error("Error occurred while downloading file: {}", e.getMessage());
			// Decide whether to continue or abort the task execution based on the
			// requirements
		}

		task.setJobStatus("D");
		return Mono.empty();
	}

	private String formatDateTime(LocalDateTime dateTime, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return formatter.format(dateTime);
	}
}
