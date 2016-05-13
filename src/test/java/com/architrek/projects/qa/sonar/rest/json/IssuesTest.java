package com.architrek.projects.qa.sonar.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by AANG on 13/05/16/16:03.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * Architrek(c) Andreetto Consulting sonar-digest 2016 All rights reserved
 */
public class IssuesTest {

    Issues issues;

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

            if (file.getName().contains("issues.json")) {
                issues = mapper.readValue(file, Issues.class);
            }
        }
    }

    @Test
    public void getPaging() throws Exception {
        Paging paging = issues.getPaging();
        assertTrue(paging.getPageSize().equals(100));
    }

    @Test
    public void getComponents() throws Exception {
        List<Component> component = issues.getComponents();
        assertFalse(component.isEmpty());

    }

}