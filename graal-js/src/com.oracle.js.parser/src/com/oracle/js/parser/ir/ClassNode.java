/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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

import java.util.Collections;
import java.util.List;

import com.oracle.js.parser.ir.visitor.NodeVisitor;
import com.oracle.js.parser.ir.visitor.TranslatorNodeVisitor;

/**
 * IR representation for class definitions.
 */
public class ClassNode extends Expression {
    private final IdentNode ident;
    private final Expression classHeritage;
    private final PropertyNode constructor;
    private final List<PropertyNode> classElements;
    private final List<Expression> decorators;
    private final int line;

    /**
     * Constructor.
     *
     * @param line line number
     * @param token token
     * @param finish finish
     */
    public ClassNode(final int line, final long token, final int finish, final IdentNode ident, final Expression classHeritage, final PropertyNode constructor,
                    final List<PropertyNode> classElements, final List<Expression> decorators) {
        super(token, finish);
        this.line = line;
        this.ident = ident;
        this.classHeritage = classHeritage;
        this.constructor = constructor;
        this.classElements = classElements;
        this.decorators = decorators;
    }

    private ClassNode(final ClassNode classNode, final IdentNode ident, final Expression classHeritage, final PropertyNode constructor,
                    final List<PropertyNode> classElements, final List<Expression> decorators) {
        super(classNode);
        this.line = classNode.getLineNumber();
        this.ident = ident;
        this.classHeritage = classHeritage;
        this.constructor = constructor;
        this.classElements = classElements;
        this.decorators = decorators;
    }

    /**
     * Class identifier. Optional.
     */
    public IdentNode getIdent() {
        return ident;
    }

    private ClassNode setIdent(final IdentNode ident) {
        if (this.ident == ident) {
            return this;
        }
        return new ClassNode(this, ident, classHeritage, constructor, classElements, decorators);
    }

    /**
     * The expression of the {@code extends} clause. Optional.
     */
    public Expression getClassHeritage() {
        return classHeritage;
    }

    private ClassNode setClassHeritage(final Expression classHeritage) {
        if (this.classHeritage == classHeritage) {
            return this;
        }
        return new ClassNode(this, ident, classHeritage, constructor, classElements, decorators);
    }

    /**
     * Get the constructor method definition.
     */
    public PropertyNode getConstructor() {
        return constructor;
    }

    private ClassNode setConstructor(final PropertyNode constructor) {
        if (this.constructor == constructor) {
            return this;
        }
        return new ClassNode(this, ident, classHeritage, constructor, classElements, decorators);
    }

    /**
     * Get method definitions except the constructor.
     */
    public List<PropertyNode> getClassElements() {
        return Collections.unmodifiableList(classElements);
    }

    private ClassNode setClassElements(final List<PropertyNode> classElements) {
        if (this.classElements == classElements) {
            return this;
        }
        return new ClassNode(this, ident, classHeritage, constructor, classElements, decorators);
    }

    /**
     * Get decorators.
     */
    public List<Expression> getDecorators() {
        return Collections.unmodifiableList(decorators);
    }

    private ClassNode setDecorators(final List<Expression> decorators) {
        if (this.decorators == decorators) {
            return this;
        }
        return new ClassNode(this, ident, classHeritage, constructor, classElements, decorators);
    }

    /**
     * Returns the line number.
     */
    public int getLineNumber() {
        return line;
    }

    @Override
    public Node accept(final NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterClassNode(this)) {
            IdentNode newIdent = ident == null ? null : (IdentNode) ident.accept(visitor);
            Expression newClassHeritage = classHeritage == null ? null : (Expression) classHeritage.accept(visitor);
            PropertyNode newConstructor = constructor == null ? null : (PropertyNode) constructor.accept(visitor);
            return visitor.leaveClassNode(setIdent(newIdent).setClassHeritage(newClassHeritage).setConstructor(newConstructor).setClassElements(Node.accept(visitor, classElements)).setDecorators(Node.accept(visitor, decorators)));
        }

        return this;
    }

    @Override
    public <R> R accept(TranslatorNodeVisitor<? extends LexicalContext, R> visitor) {
        return visitor.enterClassNode(this);
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        sb.append("class");
        if (ident != null) {
            sb.append(' ');
            ident.toString(sb, printType);
        }
        if (classHeritage != null) {
            sb.append(" extends");
            classHeritage.toString(sb, printType);
        }
        sb.append(" {");
        if (constructor != null) {
            constructor.toString(sb, printType);
        }
        for (PropertyNode classElement : classElements) {
            sb.append(" ");
            classElement.toString(sb, printType);
        }
        sb.append("}");
    }
}
