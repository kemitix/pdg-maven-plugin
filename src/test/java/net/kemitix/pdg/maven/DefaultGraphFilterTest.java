package net.kemitix.pdg.maven;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kemitix.node.Nodes;
import org.apache.maven.plugin.logging.Log;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link DefaultGraphFilter}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class DefaultGraphFilterTest {

    private static final String UNSET = "";

    private static final String MATCHES = "kemitix";

    private static final String NON_MATCH = "garbage";

    private static final String PACKAGE_NAME = "net.kemitix";

    private static final boolean INCLUDED = true;

    private static final boolean EXCLUDED = false;

    @Mock
    private DigraphMojo mojo;

    @Mock
    private Log logger;

    @Mock
    private NodePathGenerator nodePathGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(mojo.getLog()).willReturn(logger);
    }

    @Test
    public void filterNodesShouldMatchTestMatrix() throws Exception {
        //given
        val packageData = PackageData.newInstance(PACKAGE_NAME);
        val packageNode = Nodes.unnamedRoot(packageData);
        val testPatterns = new ArrayList<TestPattern>();

        // simple options
        /// exclude
        testPatterns.add(new TestPattern(MATCHES, UNSET, EXCLUDED, "simple exclude, unset include"));
        testPatterns.add(new TestPattern(MATCHES, NON_MATCH, EXCLUDED, "simple exclude, non-matching include"));
        /// include
        testPatterns.add(new TestPattern(UNSET, MATCHES, INCLUDED, "simple include, unset exclude"));
        testPatterns.add(new TestPattern(NON_MATCH, MATCHES, INCLUDED, "simple include, non-matching exclude"));

        // dual options
        /// Default unless the user specifies something, so include all
        testPatterns.add(new TestPattern(UNSET, UNSET, INCLUDED, "Default unset all, so include all"));
        /// Explicit include pattern and it doesn't match, so exclude
        testPatterns.add(
                new TestPattern(NON_MATCH, NON_MATCH, EXCLUDED, "Explicit include and it doesn't match, so exclude"));
        /// Explicit exclude pattern, despite include pattern match, so exclude
        testPatterns.add(new TestPattern(MATCHES, MATCHES, EXCLUDED, "Explicit exclude, despite include, so exclude"));

        //then
        SoftAssertions.assertSoftly(softly -> testPatterns.forEach(testPattern -> {
            //given
            val exclude = testPattern.exclude;
            val include = testPattern.include;
            val graphFilter = new DefaultGraphFilter(exclude, include, nodePathGenerator);
            given(nodePathGenerator.getPath(eq(packageNode), any(), any())).willReturn(PACKAGE_NAME);
            //when
            val result = graphFilter.filterNodes(packageNode);
            //then
            softly.assertThat(result)
                  .as(testPattern.name)
                  .isEqualTo(testPattern.expected);
        }));
    }

    @Test
    public void isExcludedShouldBeFalseWhenNotSet() throws Exception {
        //given
        val graphFilter = new DefaultGraphFilter(UNSET, UNSET, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        //when
        val result = graphFilter.isExcluded(packageDataNode);
        //then
        assertThat(result).isFalse();
    }

    @Test
    public void isExcludedShouldBeTrueWhenMatch() throws Exception {
        //given
        val graphFilter = new DefaultGraphFilter(MATCHES, UNSET, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        given(nodePathGenerator.getPath(eq(packageDataNode), any(), any())).willReturn(PACKAGE_NAME);
        //when
        val result = graphFilter.isExcluded(packageDataNode);
        //then
        assertThat(result).isTrue();
    }

    @Test
    public void isExcludedShouldBeFalseWhenNoMatch() throws Exception {
        //given
        val graphFilter = new DefaultGraphFilter(NON_MATCH, UNSET, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        given(nodePathGenerator.getPath(eq(packageDataNode), any(), any())).willReturn(PACKAGE_NAME);
        //when
        val result = graphFilter.isExcluded(packageDataNode);
        //then
        assertThat(result).isFalse();
    }

    @Test
    public void isIncludedShouldBeTrueWhenNotSet() throws Exception {
        //given
        val graphFilter = new DefaultGraphFilter(UNSET, UNSET, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        //when
        val result = graphFilter.isIncluded(packageDataNode);
        //then
        assertThat(result).isTrue();
    }

    @Test
    public void isIncludedShouldBeTrueWhenMatch() throws Exception {
        //given
        val graphFilter = new DefaultGraphFilter(UNSET, MATCHES, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        given(nodePathGenerator.getPath(eq(packageDataNode), any(), any())).willReturn(PACKAGE_NAME);
        //when
        val result = graphFilter.isIncluded(packageDataNode);
        //then
        assertThat(result).isTrue();
    }

    @Test
    public void isIncludedShouldBeFalseWhenNoMatch() throws Exception {
        //given
        val graphFilter = new DefaultGraphFilter(UNSET, NON_MATCH, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        given(nodePathGenerator.getPath(eq(packageDataNode), any(), any())).willReturn(PACKAGE_NAME);
        //when
        val result = graphFilter.isIncluded(packageDataNode);
        //then
        assertThat(result).isFalse();
    }

    @RequiredArgsConstructor
    private class TestPattern {

        final String exclude;

        final String include;

        final boolean expected;

        final String name;
    }
}
