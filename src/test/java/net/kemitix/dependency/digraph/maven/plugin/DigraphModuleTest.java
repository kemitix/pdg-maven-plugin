package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link DigraphModule}.
 *
 * @author pcampbell
 */
public class DigraphModuleTest {

    @Inject
    private SourceDirectoryProvider sourceDirectoryProvider;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        Guice.createInjector(new DigraphModule()).injectMembers(this);
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
