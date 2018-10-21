package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class PackageDataTest implements WithAssertions {

    private final String name = "name";
    private final PackageData data = PackageData.newInstance(name);

    @Test
    void shouldGetName() {
        assertThat(data.getName()).isEqualTo(name);
    }

    @Test
    void shouldCreateUniqueHashCode() {
        //given
        final String name2 = "name2";
        val data2 = PackageData.newInstance(name2);
        //then
        assertThat(data.hashCode()).isNotEqualTo(data2.hashCode());
    }

    /**
     * Two objects with the same name are equal.
     */
    @Test
    void shouldBeEqualWhenSameName() {
        //given
        val data2 = PackageData.newInstance(name);
        //then
        assertThat(data).isEqualTo(data2)
                        .isNotSameAs(data2);
    }

    /**
     * When an object is in a Set, then another object with the same name is
     * considered to be in the Set.
     */
    @Test
    void shouldBeInSetIfNameMatches() {
        //given
        val data2 = PackageData.newInstance(name);
        final Set<PackageData> set = new HashSet<>();
        //when
        set.add(data);
        //then
        assertThat(set).contains(data2);
    }

    @Test
    void equalsShouldBeFalseWhenOtherIsNull() {
        //given
        val data = PackageData.newInstance("name");
        //then
        assertThat(data).isNotNull();
    }

    @Test
    void equalsShouldBeFalseWhenOtherIsNotPackageData() {
        //given
        val data = PackageData.newInstance("name");
        //then
        assertThat(data).isNotEqualTo("not PackageData");
    }
}
