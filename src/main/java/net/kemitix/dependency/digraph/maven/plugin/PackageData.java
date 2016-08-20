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
        int result = 17;
        result = 31 * result + Objects.hashCode(this.name);
        return result;
    }

    /**
     * Checks whether two {@code PackageData} objects are "equal".
     *
     * <p>They are considered to be equal if they have the same name.</p>
     *
     * @param obj the other object to compare against
     *
     * @return {@code true} if this object is the same as the obj argument;
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PackageData)) {
            return false;
        }
        final PackageData other = (PackageData) obj;
        return Objects.equals(this.name, other.name);
    }

}
