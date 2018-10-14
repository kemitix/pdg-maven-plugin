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

import lombok.val;
import net.kemitix.node.Node;

import javax.annotation.concurrent.Immutable;

/**
 * Default implementation of the Node ID Generator.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DefaultNodePathGenerator implements NodePathGenerator {

    @Override
    public String getPath(
            final Node<PackageData> node, final Node<PackageData> base, final String delimiter
                         ) {
        val path = new StringBuilder();
        node.findData()
            .map(PackageData::getName)
            .ifPresent(name -> node.findParent()
                                   .ifPresent(parent -> {
                                       if (!parent.equals(base)) {
                                           path.append(getPath(parent, base, delimiter))
                                               .append(delimiter);
                                       }
                                       path.append(name);
                                   }));
        return path.toString();
    }

}
