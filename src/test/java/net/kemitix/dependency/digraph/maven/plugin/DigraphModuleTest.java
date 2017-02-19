package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link DigraphModule}.
 *
 * @author pcampbell
 */
public class DigraphModuleTest {

    @Inject
    private SourceDirectoryProvider sourceDirectoryProvider;

    @Mock
    private DigraphMojo digraphMojo;

    @Mock
    private GraphFilter graphFilter;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Guice.createInjector(new DigraphModule(digraphMojo, graphFilter))
             .injectMembers(this);
        /// default behaviour without any filters is to include
        given(graphFilter.filterNodes(any())).willReturn(true);
    }

    /**
     * Test that a {@link SourceDirectoryProvider} implementation is found an
     * injected.
     */
    @Test
    public void shouldHaveInjectedSourceDirectoryProvider() {
        assertNotNull(sourceDirectoryProvider);
    }

}
