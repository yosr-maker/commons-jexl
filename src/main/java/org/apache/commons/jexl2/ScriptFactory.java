/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jexl2;

import java.io.File;
import java.net.URL;
import org.apache.commons.jexl2.parser.JexlNode;
/**
 * <p>
 * Creates {@link Script}s.  To create a JEXL Script, pass
 * valid JEXL syntax to the static createScript() method:
 * </p>
 *
 * <pre>
 * String jexl = "y = x * 12 + 44; y = y * 4;";
 * Script script = ScriptFactory.createScript( jexl );
 * </pre>
 *
 * <p>
 * When an {@link Script} is created, the JEXL syntax is
 * parsed and verified.
 * </p>
 *
 * <p>
 * This is a convenience class; using an instance of a {@link JexlEngine}
 * that serves the same purpose with more control is recommended.
 * </p>
 * @since 1.1
 * @version $Id$
 * @deprecated Create a JexlEngine and use the createScript method on that instead.
 */
@Deprecated
public final class ScriptFactory {
    /**
     * Lazy JexlEngine singleton; since this class is deprecated, let's not create the shared
     * engine if it's not gonna be used.
     */
    private static volatile JexlEngine jexl10 = null;
    /**
     * Default cache size.
     */
    private static final int CACHE_SIZE = 256;
    
    /**
     * An interpreter made compatible with v1.1 behavior (at least Jelly's expectations).
     */
    private static class LegacyInterpreter extends Interpreter {
        /**
         * Creates an instance.
         * @param jexl the jexl engine
         * @param aContext the jexl context
         */
        public LegacyInterpreter(JexlEngine jexl, JexlContext aContext) {
            super(jexl, aContext);
        }
        /**{@inheritDoc}*/
        @Override
        public Object interpret(JexlNode node) {
            try {
                return node.jjtAccept(this, null);
            } catch (JexlException xjexl) {
                Throwable e = xjexl.getCause();
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                if (e instanceof IllegalStateException) {
                    throw (IllegalStateException) e;
                }
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        /**{@inheritDoc}*/
        @Override
        protected Object invocationFailed(JexlException xjexl) {
            throw xjexl;
        }
        /**{@inheritDoc}*/
        @Override
        protected Object unknownVariable(JexlException xjexl) {
            return null;
        }
    }
    
    /**
     * An engine that uses a LegacyInterpreter.
     */
    private static class LegacyEngine extends JexlEngine {
        /**{@inheritDoc}*/
        @Override
        protected Interpreter createInterpreter(JexlContext context) {
            if (context == null) {
                context = EMPTY_CONTEXT;
            }
            return new ScriptFactory.LegacyInterpreter(this, context);
        }
    }
    
    /**
     * Retrieves the static shared JexlEngine instance.
     * @return the static "legacy" shared instance
     */
    // CSOFF: DoubleCheckedLocking
    static JexlEngine getInstance() {
        // Java5 memory model allows double-lock pattern
        if (jexl10 == null) {
            synchronized(ScriptFactory.class){
                if (jexl10 == null) {
                    JexlEngine jexl = new LegacyEngine();
                    jexl.setCache(CACHE_SIZE);
                    jexl.setSilent(false);
                    jexl10 = jexl;
                }
            }
        }
        return jexl10;
    }
    // CSON: DoubleCheckedLocking

    /**
     * Private constructor, ensure no instance.
     */
    private ScriptFactory() {}

    /**
     * Creates a Script from a String containing valid JEXL syntax.
     * This method parses the script which validates the syntax.
     *
     * @param scriptText A String containing valid JEXL syntax
     * @return A {@link Script} which can be executed with a
     *      {@link JexlContext}.
     * @throws Exception An exception can be thrown if there is a
     *      problem parsing the script.
     * @deprecated Create a JexlEngine and use the createScript method on that instead.
     */
    @Deprecated
    public static Script createScript(String scriptText) throws Exception {
        return getInstance().createScript(scriptText);
    }

    /**
     * Creates a Script from a {@link File} containing valid JEXL syntax.
     * This method parses the script and validates the syntax.
     *
     * @param scriptFile A {@link File} containing valid JEXL syntax.
     *      Must not be null. Must be a readable file.
     * @return A {@link Script} which can be executed with a
     *      {@link JexlContext}.
     * @throws Exception An exception can be thrown if there is a problem
     *      parsing the script.
     * @deprecated Create a JexlEngine and use the createScript method on that instead.
     */
    @Deprecated
    public static Script createScript(File scriptFile) throws Exception {
        return getInstance().createScript(scriptFile);
    }

    /**
     * Creates a Script from a {@link URL} containing valid JEXL syntax.
     * This method parses the script and validates the syntax.
     *
     * @param scriptUrl A {@link URL} containing valid JEXL syntax.
     *      Must not be null. Must be a readable file.
     * @return A {@link Script} which can be executed with a
     *      {@link JexlContext}.
     * @throws Exception An exception can be thrown if there is a problem
     *      parsing the script.
     * @deprecated Create a JexlEngine and use the createScript method on that instead.
     */
    @Deprecated
    public static Script createScript(URL scriptUrl) throws Exception {
        return getInstance().createScript(scriptUrl);
    }

}