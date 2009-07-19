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

package org.apache.commons.jexl.util.introspection;

import java.util.Iterator;

/**
 * 'Federated' introspection/reflection interface to allow the introspection
 * behavior in Velocity to be customized.
 * 
 * @since 1.0
 * @author <a href="mailto:geirm@apache.org">Geir Magusson Jr.</a>
 * @version $Id$
 */
public interface Uberspect {
    /**
     * Initializer - will be called before use.
     */
    void init();

    /** Gets underlying introspector.
     * @return the introspector
     */
    Introspector getIntrospector();
    
    /**
     * To support iteratives - #foreach().
     * @param info template info.
     * @param obj to get the iterator for.
     * @return an iterator over obj.
     */
    Iterator<?> getIterator(Object obj, Info info);

    /**
     * Returns a general method, corresponding to $foo.bar( $woogie ).
     * @param obj the object
     * @param method the method name
     * @param args method arguments
     * @param info template info
     * @return a {@link VelMethod}.
     */
    VelMethod getMethod(Object obj, String method, Object[] args, Info info);

    /**
     * Property getter - returns VelPropertyGet appropos for #set($foo =
     * $bar.woogie).
     * @param obj the object to get the property from.
     * @param identifier property name
     * @param info template info
     * @return a {@link VelPropertyGet}.
     */
    VelPropertyGet getPropertyGet(Object obj, String identifier, Info info);

    /**
     * Property setter - returns VelPropertySet appropos for #set($foo.bar =
     * "geir").
     * @param obj the object to get the property from.
     * @param identifier property name
     * @param arg value to set.
     * @param info template info
     * @return a {@link VelPropertySet}.
     */
    VelPropertySet getPropertySet(Object obj, String identifier, Object arg, Info info);
}