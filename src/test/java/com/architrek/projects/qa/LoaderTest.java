package com.architrek.projects.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;

/**
 * Created by AANG on 10/05/16/16:19.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * (c) White Andreetto Consulting 2016 All rights reserved
 */
public class LoaderTest {

    @Test
    public void testLoading() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File ("/Users/anda/workspace/projects/sonar-digest/src/data/sonar.response.blocker.p1.json");
        BlockerSchema blockerSchema = objectMapper.readValue(jsonFile, BlockerSchema.class);
        System.out.println(blockerSchema.getIssues());

    }

}
