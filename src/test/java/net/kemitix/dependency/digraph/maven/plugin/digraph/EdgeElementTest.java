package net.kemitix.dependency.digraph.maven.plugin.digraph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for {@link EdgeElement}.
 *
 * @author pcampbell
 */
public class EdgeElementTest {

    @Mock
    private EdgeEndpoint head;

    @Mock
    private EdgeEndpoint tail;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSetAndReturnHeadAndTail() {
        //when
        final EdgeElement edgeElement = new EdgeElement(tail, head);
        //then
        Assert.assertThat(edgeElement.getTail(), is(tail));
        Assert.assertThat(edgeElement.getHead(), is(head));
    }

}
