package com.architrek.projects.qa.sonar;

import com.architrek.projects.qa.sonar.rest.json.Component;
import com.architrek.projects.qa.sonar.rest.json.Issues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by AANG on 13/05/16/13:59.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * Architrek(c) Andreetto Consulting sonar-digest 2016 All rights reserved
 */
@org.springframework.stereotype.Component
public class SonarQubeClient {


    @Value("${SEVERITY}")
    private String SEVERITY;


    @Value("${COMPONENTS_URI}")
    private String COMPONENTS_URI;

    @Value("${ISSUES_URI}")
    private String ISSUES_URI;

    /**
     * Loads all Issues for a configured severity for the selected component
     *
     * @param component The Sonar Component to query about
     * @param page      The page we want to retrieve
     * @return All Issues belonging to the given component
     * @throws URISyntaxException IF the SonarQube URI is not correct
     */
    protected Issues getIssues(Component component, int page) throws URISyntaxException {

        final URI uri = new URI(String.format(ISSUES_URI, component.getKey(), SEVERITY, page));
        final RestTemplate anotherRestTemplate = new RestTemplate();
        return anotherRestTemplate.getForObject(uri, Issues.class);
    }


    /**
     * This method returns the list of all components configured in SonarQube instance
     *
     * @return the List of Components
     */
    protected List<Component> getComponents() {
        ResponseEntity<List<Component>> componentResponse = new RestTemplate().exchange(COMPONENTS_URI,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Component>>() {
                });
        return componentResponse.getBody();
    }

    public String getSEVERITY() {
        return SEVERITY;
    }

    public void setSEVERITY(String SEVERITY) {
        this.SEVERITY = SEVERITY;
    }

    public String getCOMPONENTS_URI() {
        return COMPONENTS_URI;
    }

    public void setCOMPONENTS_URI(String COMPONENTS_URI) {
        this.COMPONENTS_URI = COMPONENTS_URI;
    }

    public String getISSUES_URI() {
        return ISSUES_URI;
    }

    public void setISSUES_URI(String ISSUES_URI) {
        this.ISSUES_URI = ISSUES_URI;
    }

}
