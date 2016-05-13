package com.architrek.projects.qa.sonar;


import com.architrek.projects.qa.sonar.rest.json.Issue;

import java.util.List;

/**
 * Created by AANG on 13/05/16/11:40.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * Architrek(c) Andreetto Consulting sampleRest 2016 All rights reserved
 */
public class SonarQubeDigestUtils {


    public static String transformIssueInCSVLine(Issue issue) {
        String newLine = issue.getSeverity() + "," +
                issue.getProject() + "," +
                issue.getSubProject() + "," +
                issue.getComponent().replaceAll(issue.getSubProject() + ":", "") + "," +
                issue.getMessage() + "," +
                issue.getLine() + "," +
                issue.getAuthor() + "," +
                issue.getDebt().replaceAll("[^0-9]", "") + ",";
        final List<String> tags = issue.getTags();
        for (String tag : tags) {
            newLine = newLine + tag + ";";
        }
        newLine = newLine.replaceAll("[;]$", "");
        return newLine;
    }

}
