/*
 * Copyright (c) 2010, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.oracle.js.parser.ir;

import com.oracle.js.parser.ir.visitor.NodeVisitor;
import com.oracle.js.parser.ir.visitor.TranslatorNodeVisitor;
import java.util.Collections;
import java.util.List;

public class JsxElementNode extends Expression {

    private final String name;

    private final List<Expression> attributes;

    private final List<Expression> children;

    public JsxElementNode(String name, List<Expression> attributes, List<Expression> children, long token, int finish) {
        super(token, finish);
        this.name = name;
        this.attributes = attributes;
        this.children = children;
    }

    private JsxElementNode(JsxElementNode node, String name, List<Expression> attributes, List<Expression> children) {
        super(node);
        this.name = name;
        this.attributes = attributes;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    public JsxElementNode setAttributes(List<Expression> attributes) {
        if (this.attributes == attributes) {
            return this;
        }
        return new JsxElementNode(this, name, attributes, children);
    }

    public List<Expression> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public JsxElementNode setChildren(List<Expression> children) {
        if (this.children == children) {
            return this;
        }
        return new JsxElementNode(this, name, attributes, children);
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterJsxElementNode(this)) {
            return visitor.leaveJsxElementNode(
                    setAttributes(Node.accept(visitor, attributes)).
                    setChildren(Node.accept(visitor, children)));
        }

        return this;
    }

    @Override
    public <R> R accept(TranslatorNodeVisitor<? extends LexicalContext, R> visitor) {
        return visitor.enterJsxElementNode(this);
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        sb.append('<').append(name);
        for (Expression attr : attributes) {
            sb.append(' ');
            attr.toString(sb, printType);
        }
        if (children.isEmpty()) {
            sb.append("/>");
        } else {
            sb.append('>');
            for (Expression child : children) {
                child.toString(sb, printType);
            }
            sb.append("</").append(name).append('>');
        }
    }
}
