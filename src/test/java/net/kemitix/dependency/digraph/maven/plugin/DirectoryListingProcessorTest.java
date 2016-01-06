package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DirectoryListingProcessor}.
 *
 * @author pcampbell
 */
public class DirectoryListingProcessorTest {

    /**
     * Class under test.
     */
    @InjectMocks
    private DirectoryListingProcessor listingProcessor;

    /**
     * Mock mojo.
     */
    @Mock
    private DigraphMojo mojo;

    /**
     * Mock Log.
     */
    @Mock
    private Log log;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(log).when(mojo).getLog();
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
        verify(log, times(1)).info("* " + alpha);
        verify(log, times(1)).info("* " + beta);
    }

}
