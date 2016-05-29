package com.architrek.projects.qa.sonar;

import com.architrek.projects.qa.sonar.rest.json.Component;
import com.architrek.projects.qa.sonar.rest.json.Issues;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by AANG on 13/05/16/13:27.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * Architrek(c) Andreetto Consulting sonar-digest 2016 All rights reserved
 */
public class SonarQubeClientTest {

    SonarQubeClient sonarQubeClient;
    private List<Component> components;

    @Before
    public void setUp() throws Exception {
        sonarQubeClient = new SonarQubeClient();
        sonarQubeClient.setCOMPONENTS_URI("http://localhost:9000/api/resources");
        sonarQubeClient.setISSUES_URI("http://localhost:9000/api/issues/search?componentRoots=%s&statuses=OPEN,REOPENED,CONFIRMED&severities=%s&p=%d");
        sonarQubeClient.setSEVERITY("BLOCKER,CRITICAL");

        components = sonarQubeClient.getComponents();
    }

    @Test
    public void testReadComponents() throws Exception {
        assertTrue("Expecting some components", components.size() > 0);
    }

    @Test
    public void testReadIssues() throws Exception {

        final Issues issues = sonarQubeClient.getIssues(components.get(1), 1);
        assertTrue("Expecting some issue", issues.getIssues().size() > 0);
    }


    @Test
    public void testRegExp() throws Exception{
        String test = "\"value\" is already a string, there's no need to call \"toString()\" on it.";
        //test = test.replaceAll("(?<!, ), (?!,)", " ");
        test = test.replaceAll("[,]$", ";");
        System.out.println(test);

    }


}