package net.kemitix.pdg.maven;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kemitix.node.Nodes;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.apache.maven.plugin.logging.Log;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DefaultGraphFilterTest implements WithAssertions {

    private static final String UNSET = "";
    private static final String MATCHES = "kemitix";
    private static final String NON_MATCH = "garbage";
    private static final String PACKAGE_NAME = "net.kemitix";
    private static final boolean INCLUDED = true;
    private static final boolean EXCLUDED = false;

    private final DigraphConfiguration configuration = mock(DigraphConfiguration.class);
    private final Log logger = mock(Log.class);
    private final NodePathGenerator nodePathGenerator = mock(NodePathGenerator.class);

    @BeforeEach
    void setUp() {
        given(configuration.getLog()).willReturn(logger);
    }

    @Test
    void filterNodesShouldMatchTestMatrix() {
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
        assertSoftly(softly ->
                testPatterns.forEach(testPattern -> {
                    //given
                    given(configuration.getInclude()).willReturn(testPattern.include);
                    given(configuration.getExclude()).willReturn(testPattern.exclude);
                    val graphFilter = new DefaultGraphFilter(configuration, nodePathGenerator);
                    given(nodePathGenerator.getPath(eq(packageNode), any(), any())).willReturn(PACKAGE_NAME);
                    //when
                    val result = graphFilter.filterNodes(packageNode);
                    //then
                    softly.assertThat(result).as(testPattern.name).isEqualTo(testPattern.expected);
                }));
    }

    @Test
    void isExcludedShouldBeFalseWhenNotSet() {
        //given
        given(configuration.getInclude()).willReturn(UNSET);
        given(configuration.getExclude()).willReturn(UNSET);
        val graphFilter = new DefaultGraphFilter(configuration, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        //when
        val result = graphFilter.isExcluded(packageDataNode);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void isExcludedShouldBeTrueWhenMatch() {
        //given
        given(configuration.getInclude()).willReturn(UNSET);
        given(configuration.getExclude()).willReturn(MATCHES);
        val graphFilter = new DefaultGraphFilter(configuration, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        given(nodePathGenerator.getPath(eq(packageDataNode), any(), any())).willReturn(PACKAGE_NAME);
        //when
        val result = graphFilter.isExcluded(packageDataNode);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void isExcludedShouldBeFalseWhenNoMatch() {
        //given
        given(configuration.getInclude()).willReturn(NON_MATCH);
        given(configuration.getExclude()).willReturn(UNSET);
        val graphFilter = new DefaultGraphFilter(configuration, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        given(nodePathGenerator.getPath(eq(packageDataNode), any(), any())).willReturn(PACKAGE_NAME);
        //when
        val result = graphFilter.isExcluded(packageDataNode);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void isIncludedShouldBeTrueWhenNotSet() {
        //given
        given(configuration.getInclude()).willReturn(UNSET);
        given(configuration.getExclude()).willReturn(UNSET);
        val graphFilter = new DefaultGraphFilter(configuration, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        //when
        val result = graphFilter.isIncluded(packageDataNode);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void isIncludedShouldBeTrueWhenMatch() {
        //given
        given(configuration.getInclude()).willReturn(UNSET);
        given(configuration.getExclude()).willReturn(MATCHES);
        val graphFilter = new DefaultGraphFilter(configuration, nodePathGenerator);
        val packageDataNode = Nodes.unnamedRoot(PackageData.newInstance(PACKAGE_NAME));
        given(nodePathGenerator.getPath(eq(packageDataNode), any(), any())).willReturn(PACKAGE_NAME);
        //when
        val result = graphFilter.isIncluded(packageDataNode);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void isIncludedShouldBeFalseWhenNoMatch() {
        //given
        given(configuration.getInclude()).willReturn(NON_MATCH);
        given(configuration.getExclude()).willReturn(UNSET);
        val graphFilter = new DefaultGraphFilter(configuration, nodePathGenerator);
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
