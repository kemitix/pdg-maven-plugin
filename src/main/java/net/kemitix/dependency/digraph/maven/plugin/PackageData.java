package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.kemitix.node.Node;

/**
 * Defines a package.
 *
 * @author pcampbell
 */
public final class PackageData {

    @Getter
    private final String name;

    @Getter
    private final Set<Node<PackageData>> uses = new HashSet<>();

    private PackageData(final String name) {
        this.name = name;
    }

    /**
     * Static factory.
     *
     * @param name the name of the package
     *
     * @return new instance of PackageData
     */
    static PackageData newInstance(final String name) {
        return new PackageData(name);
    }

    @Override
    @SuppressWarnings("magicnumber")
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PackageData other = (PackageData) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
