package com.architrek.projects.qa.sonar;


import com.architrek.projects.qa.sonar.rest.json.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by AANG on 13/05/16/11:40.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * Architrek(c) Andreetto Consulting sonar-digest 2016 All rights reserved
 */
public class SonarQubeDigestUtils {

    private static final Logger log = LoggerFactory.getLogger(SonarQubeDigestUtils.class);

    public static String transformIssueInCSVLine(Issue issue) {

        try {
            String message = issue.getMessage();
            message = message.replaceAll("(?<!, ), (?!,)", " "); // remove all commas from message
            String newLine = issue.getSeverity() + "," +
                    issue.getProject() + "," +
                    issue.getSubProject() + "," +
                    (issue.getComponent()!=null?issue.getComponent().replaceAll(issue.getSubProject() + ":", ""):"") + "," +
                    message + "," +
                    (issue.getLine() != null ? issue.getLine() : 0) + "," +
                    issue.getAuthor() + "," +
                    (issue.getDebt() != null ? issue.getDebt().replaceAll("[^0-9]", "") : "0") + ",";
            final List<String> tags = issue.getTags();
            for (String tag : tags) {
                newLine = newLine + tag + ";";
            }
            newLine = newLine.replaceAll("[;]$", "");
            return newLine;
        } catch (Exception e) {
            log.error("Something went wrong with this issue: " + issue.toString());
            return "";
        }
    }

}
