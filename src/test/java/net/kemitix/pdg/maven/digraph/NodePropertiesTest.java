package net.kemitix.pdg.maven.digraph;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
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
