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

import lombok.NoArgsConstructor;
import net.kemitix.node.Node;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for sorting {@link PackageData} {@link Node}s.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@NoArgsConstructor
@Immutable
@SuppressWarnings("serial")
class NodePackageDataComparator implements Comparator<Node<PackageData>>, Serializable {

    @Override
    public int compare(
            final Node<PackageData> o1,
            final Node<PackageData> o2
    ) {
        return o1.getData()
                 .getName()
                 .compareTo(o2.getData()
                              .getName());
    }
}
