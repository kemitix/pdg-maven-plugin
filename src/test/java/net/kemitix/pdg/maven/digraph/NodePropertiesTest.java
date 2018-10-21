package net.kemitix.pdg.maven.digraph;

import lombok.val;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class NodePropertiesTest implements WithAssertions {

    private final DotFileFormat dotFileFormat = mock(DotFileFormat.class);

    @Test
    void shouldRender() {
        //given
        val nodeProperties = new NodeProperties(dotFileFormat);
        val expected = "rendered node properties";
        given(dotFileFormat.render(nodeProperties)).willReturn(expected);
        //then
        assertThat(nodeProperties.render()).isEqualTo(expected);
    }
}
