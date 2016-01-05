package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Tests for {@link DotFileReportGenerator}.
 *
 * @author pcampbell
 */
public class DotFileReportGeneratorTest {

    /**
     * Class under test.
     */
    @InjectMocks
    private DotFileReportGenerator reportGenerator;

    @Mock
    private DependencyData dependencyData;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test that the report is created as expected.
     */
    @Test
    public void shouldGenerateReport() {
        //given
        List<String> users = new ArrayList<>();
        List<String> used = new ArrayList<>();
        String user = "test.nested";
        String dependency = "test.other";
        users.add(user);
        used.add(dependency);
        doReturn(users).when(dependencyData).getUserPackages();
        doReturn(used).when(dependencyData).getUsedPackages(user);
        //when
        String report = reportGenerator.generate();
        //then
        assertThat(report,
                is("digraph {\n\t\"test.nested\" -> \"test.other\";\n}"));
    }

}
