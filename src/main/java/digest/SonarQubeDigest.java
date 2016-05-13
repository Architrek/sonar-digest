package digest;

import com.architrek.projects.samples.rest.sonar.Component;
import com.architrek.projects.samples.rest.sonar.Issue;
import com.architrek.projects.samples.rest.sonar.Issues;
import com.architrek.projects.samples.rest.sonar.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AANG on 12/05/16/12:54.
 * <p>
 * Perfection is unachievable, I'm all right with Excellence
 * <p>
 * (c) White Andreetto Consulting 2016 All rights reserved
 */
@SpringBootApplication
public class SonarQubeDigest implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SonarQubeDigest.class);

    @Value("${SEVERITY}")
    private String SEVERITY;

    @Value("${OUTPUT_LOCATION}")
    private String OUTPUT_LOCATION;

    @Value("${COMPONENTS_URI}")
    private String COMPONENTS_URI;

    @Value("${ISSUES_URI}")
    private String ISSUES_URI;


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

        List<Component> components = getComponents();


        for (Component component : components) {

            final List<Issue> allPages = new ArrayList<>();


            // we start with page one Issues
            int currentPage = 1;
            final Issues issuesPageOne = getIssues(component, currentPage);
            allPages.addAll(issuesPageOne.getIssues());


            // let's count how many pages are there
            Paging paging = issuesPageOne.getPaging();
            int pages = paging.getTotal() / paging.getPageSize() + 1;

            if (pages > 1) {

                while (pages > currentPage) {
                    currentPage++;
                    allPages.addAll(getIssues(component, currentPage).getIssues());
                }

            }

            try {
                writeItDown(component, allPages);
            } catch (SonarQubeDigestException e) {
                log.error("Execution terminated abnormally, check the stack trace for info");
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes the results of this Query to the configured output location
     *
     * @param component the detail message. The detail message is saved for
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


    /**
     * Loads all Issues for a configured severity for the selected component
     *
     * @param component The Sonar COmponent to query about
     * @param page      The page we want to retrieve
     * @return All Issues belonging to the given component
     * @throws URISyntaxException IF the SonarQube URI is not correct
     */
    private Issues getIssues(Component component, int page) throws URISyntaxException {

        final URI uri = new URI(String.format(ISSUES_URI, component.getKey(), SEVERITY, page));
        final RestTemplate anotherRestTemplate = new RestTemplate();
        return anotherRestTemplate.getForObject(uri, Issues.class);
    }


    /**
     * This method returns the list of all components configured in SonarQube instance
     *
     * @return
     */
    private List<Component> getComponents() {
        ResponseEntity<List<Component>> componentResponse = new RestTemplate().exchange(COMPONENTS_URI,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Component>>() {
                });
        return componentResponse.getBody();
    }

}
