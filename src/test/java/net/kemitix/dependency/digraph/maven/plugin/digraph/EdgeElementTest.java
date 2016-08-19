package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

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

    @Mock
    private DotFileFormat dotFileFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSetAndReturnHeadAndTail() {
        //when
        val edgeElement = new EdgeElement(tail, head, dotFileFormat);
        //then
        Assert.assertThat(edgeElement.getTail(), is(tail));
        Assert.assertThat(edgeElement.getHead(), is(head));
    }

}
