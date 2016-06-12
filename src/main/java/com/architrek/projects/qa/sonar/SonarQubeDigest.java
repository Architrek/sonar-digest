package com.architrek.projects.qa.sonar;

import com.architrek.projects.qa.sonar.rest.json.Component;
import com.architrek.projects.qa.sonar.rest.json.Issue;
import com.architrek.projects.qa.sonar.rest.json.Issues;
import com.architrek.projects.qa.sonar.rest.json.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by AANG on 12/05/16/12:54.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * (c) Architrek(c) Andreetto Consulting sonar-digest 2016 All rights reserved
 */
@SpringBootApplication
public class SonarQubeDigest implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SonarQubeDigest.class);
    @Autowired
    SonarQubeClient sonarQubeClient;
    @Value("${OUTPUT_LOCATION}")
    private String OUTPUT_LOCATION;

    /**
     * Makes this Application runnable
     */
    public static void main(String args[]) {
        log.info("Boostrapping...");
        ConfigurableApplicationContext run = SpringApplication.run(SonarQubeDigest.class);
        log.info("Shutting down...");
        run.close();
    }

    /**
     * Runner Method for this App
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        final long nanoTime = System.nanoTime();
        log.info("Starting execution");

        List<Component> components = sonarQubeClient.getComponents();


        for (Component component : components) {

            // we start with page one Issues, load it from SQ
            int currentPage = 1;
            Issues issues = sonarQubeClient.getIssues(component, currentPage);

            // write down page one in any case
            SonarQubeDigestUtils.appendToFile(OUTPUT_LOCATION, component, issues);
            currentPage++;


            // let's count how many pages are there
            Paging paging = issues.getPaging();
            int pages = paging.getTotal() / paging.getPageSize() + 1;

            // load and append issues page by page
            while (currentPage < pages) {
                issues = sonarQubeClient.getIssues(component, currentPage);
                SonarQubeDigestUtils.appendToFile(OUTPUT_LOCATION, component, issues);
                currentPage++;
            }


        }

        log.info("Execution completed in {} 1/1000\"", TimeUnit.MILLISECONDS.convert(System.nanoTime() - nanoTime, TimeUnit.NANOSECONDS));
    }


}
