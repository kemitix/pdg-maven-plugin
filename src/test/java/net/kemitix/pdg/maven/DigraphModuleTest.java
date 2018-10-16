package net.kemitix.pdg.maven;

import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DigraphModule}.
 *
 * @author pcampbell
 */
public class DigraphModuleTest {

    @Inject
    private DotFileFormatFactory dotFileFormatFactory;

    private final DigraphMojo digraphMojo = mock(DigraphMojo.class);
    private final GraphFilter graphFilter = mock(GraphFilter.class);

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        Guice.createInjector(new DigraphModule(digraphMojo, graphFilter))
             .injectMembers(this);
        /// default behaviour without any filters is to include
        given(graphFilter.filterNodes(any())).willReturn(true);
    }

    /**
     * Test that an implementation is found and injected.
     */
    @Test
    public void shouldHaveInjectedfield() {
        assertNotNull(dotFileFormatFactory);
    }

}
