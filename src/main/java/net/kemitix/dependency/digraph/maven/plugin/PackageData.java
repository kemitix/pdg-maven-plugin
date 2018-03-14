/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import net.kemitix.node.Node;

import javax.annotation.concurrent.Immutable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Defines a package.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
public final class PackageData {

    @Getter
    private final String name;

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
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
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

    Set<Node<PackageData>> getUses() {
        return new HashSet<>(uses);
    }

    /**
     * Replace the set of packages used by this node.
     *
     * @param uses The new package used set
     */
    void setUses(final Set<Node<PackageData>> uses) {
        this.uses.clear();
        this.uses.addAll(uses);
    }

    /**
     * Notes that this package is a user of another package.
     *
     * @param packageDataNode the package that is used by this package
     */
    void uses(final Node<PackageData> packageDataNode) {
        uses.add(packageDataNode);
    }

}
