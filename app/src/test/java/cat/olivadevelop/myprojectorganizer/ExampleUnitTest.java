package cat.olivadevelop.myprojectorganizer;

import org.junit.Test;

import java.util.UUID;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void generateID() throws Exception {
        String test = UUID.randomUUID().toString().replace("-", "");
        System.out.print("UUID: " + test + "; " + test.length());
    }
}