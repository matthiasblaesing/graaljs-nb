/*
 * Copyright (c) 2022, Matthias Bläsing. All rights reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oracle.js.parser;

import com.oracle.js.parser.ErrorManager.PrintWriterErrorManager;
import com.oracle.js.parser.ir.FunctionNode;
import com.oracle.js.parser.ir.IdentNode;
import com.oracle.js.parser.ir.LexicalContext;
import com.oracle.js.parser.ir.LiteralNode;
import com.oracle.js.parser.ir.Node;
import com.oracle.js.parser.ir.visitor.NodeVisitor;

public class ManualTest {

    public static void main(String[] args) {
        //Source source = Source.sourceFor("dummy.js", "function hallo() {return 'Welt';}");
        //Source source = Source.sourceFor("dummy.js", "class hallo { constructor() {} dummy() {return 'Welt'}  #height = 0; #internal() {}}");
        Source source = Source.sourceFor("dummy.js", "async function hallo() {}; async function hallo2() {let a = await hallo(); return a;}");
        ScriptEnvironment.Builder builder = ScriptEnvironment.builder();
        Parser parser = new Parser(
                builder.emptyStatements(true).ecmacriptEdition(7).jsx(true).build(),
                source,
                new PrintWriterErrorManager());
        FunctionNode fn = parser.parse();
        DumpingVisitor dv = new DumpingVisitor(new LexicalContext());
        fn.accept(dv);
    }

    static class DumpingVisitor extends NodeVisitor {
        private static final int INDENT_PER_LEVEL = 2;
        private int indent = 0;

        public DumpingVisitor(LexicalContext lc) {
            super(lc);
        }

        @Override
        protected Node leaveDefault(Node node) {
            indent -= INDENT_PER_LEVEL;
            return super.leaveDefault(node);
        }

        @Override
        protected boolean enterDefault(Node node) {
            if(node instanceof IdentNode) {
                System.out.println(indent() + node.getClass().getName() + " [" + ((IdentNode) node).getName() + "]");
            } else if(node instanceof LiteralNode) {
                System.out.println(indent() + node.getClass().getName() + " [" + ((LiteralNode) node).getValue() + "]");
            } else if(node instanceof FunctionNode) {
                System.out.println(indent() + node.getClass().getName() + " [" + (((FunctionNode) node).isAsync() ? "async" : "") + "]");
            } else {
                System.out.println(indent() + node.getClass().getName());
            }
            indent += INDENT_PER_LEVEL;
            return super.enterDefault(node);
        }

        private String indent() {
            StringBuilder sb = new StringBuilder(indent);
            for(int i = 0; i < indent; i++) {
                sb.append(" ");
            }
            return sb.toString();
        }
    }
}
