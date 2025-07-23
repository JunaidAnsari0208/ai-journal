package com.junaid.ai_journal.scheduler;

import com.junaid.ai_journal.repository.JournalEntryRepository;
import com.junaid.ai_journal.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WeeklyReportScheduler {
    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportScheduler.class);

    private final JournalService journalService;
    private final JournalEntryRepository journalEntryRepository;

    @Scheduled(cron = "0 0 20 * * SUN")
    public void generateWeeklyReportForAllUsers(){
        logger.info("----Starting Weekly Report Generation----");

        // Get a list of all unique userIds from database

        List<Long> userIds = journalEntryRepository.findDistinctUserIds();
        logger.info("Found {} unique users to process...", userIds.size());

        // Loop through each userId
        for(Long userId: userIds){
            try{
                logger.info("---Generating report for userId: {}", userId);

                //Call the service for report generation

                journalService.generateReport(userId);

                logger.info("---Report Generated Successfully!---");
            }
            catch (Exception e){
                logger.info("Failed to generate a report: "+ e);
            }
        }

        logger.info("----Finished Weekly Report Generation----");
    }
}
