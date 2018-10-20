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

package net.kemitix.pdg.maven;

import lombok.NonNull;
import lombok.val;
import net.kemitix.node.Node;

import java.util.Objects;

/**
 * Default implementation of {@link GraphFilter}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
class DefaultGraphFilter implements GraphFilter {

    public static final String DELIMITER = ".";

    private final String exclude;
    private final String include;
    private final NodePathGenerator nodePathGenerator;

    /**
     * Constructor.
     *
     * @param exclude           The exclusion
     * @param include           The inclusion
     * @param nodePathGenerator The Node Path Generator
     */
    @SuppressWarnings("avoidinlineconditionals")
    DefaultGraphFilter(final String exclude, final String include, final NodePathGenerator nodePathGenerator) {
        this.exclude = exclude == null ? "" : exclude;
        this.include = include == null ? "" : include;
        this.nodePathGenerator = nodePathGenerator;
    }

    @Override
    @SuppressWarnings("npathcomplexity")
    public boolean filterNodes(final Node<PackageData> packageDataNode) {
        boolean result = true;
        val packageName = getPackageName(packageDataNode);
        if (!include.isEmpty()) {
            result = packageName.contains(include);
        }
        if (result && !exclude.isEmpty()) {
            result = !packageName.contains(exclude);
        }
        return result;
    }

    @Override
    public boolean isExcluded(@NonNull final Node<PackageData> packageDataNode) {
        Objects.requireNonNull(exclude);
        return !exclude.isEmpty() && getPackageName(packageDataNode).contains(exclude);
    }

    @Override
    public boolean isIncluded(@NonNull final Node<PackageData> packageDataNode) {
        Objects.requireNonNull(include);
        return include.isEmpty() || getPackageName(packageDataNode).contains(include);
    }

    private String getPackageName(final Node<PackageData> packageDataNode) {
        Objects.requireNonNull(nodePathGenerator, "NodePathGenerator not set in GraphFilter");
        return nodePathGenerator.getPath(packageDataNode, null, DELIMITER);
    }
}
