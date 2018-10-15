package net.kemitix.pdg.maven;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.doReturn;
import static org.mockito.BDDMockito.then;

/**
 * Tests for {@link DirectoryListingProcessor}.
 *
 * @author pcampbell
 */
public class DirectoryListingProcessorTest {

    private DirectoryListingProcessor listingProcessor;

    @Mock
    private DigraphMojo mojo;

    @Mock
    private Log log;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        listingProcessor = new DirectoryListingProcessor(mojo);
        doReturn(log).when(mojo)
                     .getLog();
    }

    /**
     * Should log each directory provided.
     */
    @Test
    public void shouldLogEachDirectory() {
        //given
        final String alpha = "alpha";
        final String beta = "beta";
        final List<String> directories = new ArrayList<>();
        directories.add(alpha);
        directories.add(beta);
        //when
        listingProcessor.process(directories);
        //then
        then(log).should()
                 .info("* " + alpha);
        then(log).should()
                 .info("* " + beta);
    }
}
