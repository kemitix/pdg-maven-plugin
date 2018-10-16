package net.kemitix.pdg.maven.scan;

import lombok.val;
import net.kemitix.pdg.maven.scan.DigraphFactory;
import net.kemitix.pdg.maven.scan.NodeTreeDependencyData;
import org.junit.Test;

import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DigraphFactory}.
 *
 * @author pcampbell
 */
public class DigraphFactoryTest {

    @Test
    public void shouldBeValidUtilityClass() throws Exception {
        assertUtilityClassWellDefined(DigraphFactory.class);
    }

    @Test
    public void shouldNewDependencyData() throws Exception {
        //given
        val basePackage = "net.kemitix";
        //when
        val result = DigraphFactory.newDependencyData(basePackage);
        //then
        assertThat(result).isInstanceOf(NodeTreeDependencyData.class);
    }
}
