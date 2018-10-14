package net.kemitix.pdg.maven.digraph;

import lombok.val;
import net.kemitix.pdg.maven.DotFileFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link PropertyElement}.
 *
 * @author pcampbell
 */
public class PropertyElementTest {

    @Mock
    private DotFileFormat dotFileFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldRender() {
        //given
        val propertyElement = new PropertyElement("name", "value", dotFileFormat);
        val expected = "rendered property element";
        given(dotFileFormat.render(propertyElement)).willReturn(expected);
        //then
        assertThat(propertyElement.render()).isEqualTo(expected);
    }

}
