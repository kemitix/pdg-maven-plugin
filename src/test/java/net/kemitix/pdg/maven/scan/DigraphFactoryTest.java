package net.kemitix.pdg.maven.scan;

import lombok.val;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;

class DigraphFactoryTest implements WithAssertions {

    @Test
    void shouldBeValidUtilityClass() throws Exception {
        assertUtilityClassWellDefined(DigraphFactory.class);
    }

    @Test
    void shouldNewDependencyData() throws Exception {
        //given
        val basePackage = "net.kemitix";
        //when
        val result = DigraphFactory.newDependencyData(basePackage);
        //then
        assertThat(result).isInstanceOf(NodeTreeDependencyData.class);
    }
}
