package net.kemitix.pdg.maven.digraph;

import lombok.val;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PropertyElementTest implements WithAssertions {

    private final DotFileFormat dotFileFormat = mock(DotFileFormat.class);

    @Test
    void shouldRender() {
        //given
        val propertyElement = new PropertyElement("name", "value", dotFileFormat);
        val expected = "rendered property element";
        given(dotFileFormat.render(propertyElement)).willReturn(expected);
        //then
        assertThat(propertyElement.render()).isEqualTo(expected);
    }

}
