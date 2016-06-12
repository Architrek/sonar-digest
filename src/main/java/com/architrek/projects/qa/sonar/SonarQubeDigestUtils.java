package com.architrek.projects.qa.sonar;


import com.architrek.projects.qa.sonar.rest.json.Component;
import com.architrek.projects.qa.sonar.rest.json.Issue;
import com.architrek.projects.qa.sonar.rest.json.Issues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by AANG on 13/05/16/11:40.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * Architrek(c) Andreetto Consulting sonar-digest 2016 All rights reserved
 */
public class SonarQubeDigestUtils {

    private static final Logger log = LoggerFactory.getLogger(SonarQubeDigestUtils.class);

    private SonarQubeDigestUtils() {
    }

    /**
     * Takes one issue and properly formats it to a csv compliant line
     * This methid removes all characters form the issue which can confuse the csv reader
     * @param issue the issue to be transformed
     * @return the issue transformed in a csv line
     * @throws SonarQubeDigestException if anything wrong happens during the transformation
     */
    static String transformIssueInCSVLine(Issue issue) throws SonarQubeDigestException {

        try {
            String message = issue.getMessage();
            message = message.replaceAll("(?<!, ), (?!,)", " "); // remove all commas from message

            final StringBuilder newLine = new StringBuilder(250);

            newLine.append(issue.getSeverity()).append("," )
            .append(issue.getProject()).append("," )
                    .append(issue.getSubProject()).append("," )
                    .append(issue.getComponent() != null ? issue.getComponent().replaceAll(issue.getSubProject() + ":", "") : "").append("," )
                    .append(message).append("," )
                    .append(issue.getLine() != null ? String.valueOf(issue.getLine()) : "0").append("," )
                    .append(issue.getAuthor()).append("," )
                    .append(issue.getDebt() != null ? issue.getDebt().replaceAll("[^0-9]", "") : "0").append("," );

            final List<String> tags = issue.getTags();
            for (String tag : tags) {
                newLine.append(tag).append(";");
            }
            return newLine.toString().replaceAll("[;]$", "");

        } catch (Exception e) {
            final String msg = "Something went wrong with this issue: " + issue.toString();
            log.error(msg);
            throw new SonarQubeDigestException(msg, e);
        }
    }

    /**
     *
     * @param location where to write the digest file, configured in the application.properties file
     * @param component the current componentID
     * @param issues the current page received from SQ
     * @throws SonarQubeDigestException if anything goes wrong
     */
    static void appendToFile(String location, Component component, Issues issues) throws SonarQubeDigestException {


        // FileWriter is instantiated with the "append" flag set to true is a file already exist otherwise false.

        final File digestFile = new File(String.format(location,
                new SimpleDateFormat("dd-MM-yyyy").format(new Date()),
                component.getKey()));

        final boolean fileExist = digestFile.exists();


        writeItDown(digestFile, issues.getIssues(), fileExist);


    }




    /**
     * Writes (daily append mode) the given list of @Issue to the configured output location
     *
     * @param digestFile The file where to write the results
     * @param page       the current page collected for this Component
     * @param fileExist    If the digestFile exist or not, controls the writer append mode
     * @throws SonarQubeDigestException if anything goes wrong

     */
     static void writeItDown(File digestFile, List<Issue> page, boolean fileExist) throws SonarQubeDigestException {

        try {


            // if the digest file exists, the writer is in append mode
            final FileWriter fileW = new FileWriter(digestFile, fileExist);

            final BufferedWriter bufferedWriter = new BufferedWriter(fileW);

            if (!fileExist) {
                // new file, add column headers
                bufferedWriter.write("SEVERITY,PROJECT,MODULE,COMPONENT,MESSAGE,LINE,AUTHOR,DEBT,TAGS");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

            for (Issue issue : page) {

                bufferedWriter.write(transformIssueInCSVLine(issue));
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }


            bufferedWriter.close();
            fileW.close();

        } catch (IOException e) {
            throw new SonarQubeDigestException("Something really bad happened whilst writing down results", e);
        }


    }


}
