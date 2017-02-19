package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.val;
import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link NodeProperties}.
 *
 * @author pcampbell
 */
public class NodePropertiesTest {

    @Mock
    private DotFileFormat dotFileFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldRender() {
        //given
        val nodeProperties = new NodeProperties(dotFileFormat);
        val expected = "rendered node properties";
        given(dotFileFormat.render(nodeProperties)).willReturn(expected);
        //then
        assertThat(nodeProperties.render()).isEqualTo(expected);
    }
}
