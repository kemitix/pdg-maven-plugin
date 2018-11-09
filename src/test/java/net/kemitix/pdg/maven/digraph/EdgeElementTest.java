package net.kemitix.pdg.maven.digraph;

import lombok.val;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EdgeElementTest implements WithAssertions {

    private final EdgeEndpoint head = mock(EdgeEndpoint.class);
    private final EdgeEndpoint tail = mock(EdgeEndpoint.class);
    private final DotFileFormat dotFileFormat = mock(DotFileFormat.class);
    private final EdgeEndpoint edgeEndpoint = mock(EdgeEndpoint.class);

    @Test
    void shouldRender() {
        //given
        val edgeElement = new EdgeElement(edgeEndpoint, edgeEndpoint, dotFileFormat);
        val expected = "rendered edge element";
        given(dotFileFormat.render(edgeElement)).willReturn(expected);
        //then
        assertThat(edgeElement.render()).isEqualTo(expected);
    }

    @Test
    void shouldSetAndReturnHeadAndTail() {
        //when
        val edgeElement = new EdgeElement(tail, head, dotFileFormat);
        //then
        assertThat(edgeElement.getTail()).isEqualTo(tail);
        assertThat(edgeElement.getHead()).isEqualTo(head);
    }
}
