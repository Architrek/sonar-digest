package com.architrek.projects.qa.sonar;


import com.architrek.projects.qa.sonar.rest.json.Component;
import com.architrek.projects.qa.sonar.rest.json.Issue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by AANG on 13/05/16/13:47.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * Architrek(c) Andreetto Consulting sonar-digest 2016 All rights reserved
 */
public class SonarQubeDigestUtilsTest {


    Issue issue;
    List<Component> components;

    @Before
    public void setUp() throws Exception {

        final String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

        final File targetDir = new File(relPath + "../../data");


        final File[] files = targetDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        });

        final ObjectMapper mapper = new ObjectMapper();
        for (File file : files) {
            if (file.getName().contains("components.json")) {
                components = mapper.readValue(file, new TypeReference<List<Component>>() {
                });
            }
            if (file.getName().contains("issue.json")) {
                issue = mapper.readValue(file, Issue.class);
            }
        }

    }

    @Test
    public void transformIssueInCSVLine() throws Exception {
        assertTrue("", SonarQubeDigestUtils.transformIssueInCSVLine(issue) != null);
    }


}