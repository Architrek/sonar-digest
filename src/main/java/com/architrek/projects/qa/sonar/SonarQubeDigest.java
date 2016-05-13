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
import java.util.Date;
import java.util.List;

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

        log.info("Starting execution");

        List<Component> components = sonarQubeClient.getComponents();


        for (Component component : components) {

            final List<Issue> allPages = new ArrayList<>();


            // we start with page one Issues
            int currentPage = 1;
            final Issues issuesPageOne = sonarQubeClient.getIssues(component, currentPage);
            allPages.addAll(issuesPageOne.getIssues());


            // let's count how many pages are there
            Paging paging = issuesPageOne.getPaging();
            int pages = paging.getTotal() / paging.getPageSize() + 1;

            if (pages > 1) {

                while (pages > currentPage) {
                    currentPage++;
                    allPages.addAll(sonarQubeClient.getIssues(component, currentPage).getIssues());
                }

            }

            try {
                writeItDown(component, allPages);
            } catch (SonarQubeDigestException e) {
                log.error("Execution terminated abnormally, check the stack trace for info", e);
            }
        }
    }

    /**
     * Writes the results of this Query to the configured output location
     *
     * @param component the Sonar Component
     * @param allPages  All Issues collected for this Component
     * @throws SonarQubeDigestException in case something didn't work
     */
    private void writeItDown(Component component, List<Issue> allPages) throws SonarQubeDigestException {

        try {

            final SimpleDateFormat fromFmt = new SimpleDateFormat("dd-MM-yyyy");


            final FileWriter fileW = new FileWriter(String.format(OUTPUT_LOCATION, fromFmt.format(new Date()), component.getKey()));
            final BufferedWriter bufferedWriter = new BufferedWriter(fileW);

            // add column headers
            bufferedWriter.write("SEVERITY,PROJECT,MODULE,COMPONENT,MESSAGE,LINE,AUTHOR,DEBT,TAGS");
            bufferedWriter.newLine();


            for (Issue issue : allPages) {


                bufferedWriter.write(SonarQubeDigestUtils.transformIssueInCSVLine(issue));
                bufferedWriter.newLine();

            }


            bufferedWriter.close();
            fileW.close();

        } catch (IOException e) {
            throw new SonarQubeDigestException("Something really bad happend whilst writing down results", e);
        }


    }


}
