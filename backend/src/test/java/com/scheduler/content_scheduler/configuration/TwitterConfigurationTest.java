package com.scheduler.content_scheduler.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class TwitterConfigurationTest {

    @Autowired
    private Twitter twitter;

    @Test
    void testTwitterInstance() {
        try {
            // Verify credentials to ensure the Twitter instance is functional
            twitter.verifyCredentials();
            System.out.println("Twitter credentials verified successfully.");

            // Optionally, post a test tweet (comment out after testing)
            Status status = twitter.updateStatus("Test tweet from TwitterConfigurationTest!");
            System.out.println("Successfully posted status: " + status.getText());
        } catch (TwitterException e) {
            e.printStackTrace();
            fail("Twitter instance failed: " + e.getMessage());
        }
    }
}
