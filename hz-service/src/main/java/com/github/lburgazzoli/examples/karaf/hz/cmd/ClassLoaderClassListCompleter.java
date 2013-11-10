/**
 *
 * Copyright 2013 lb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.examples.karaf.hz.cmd;

import com.github.lburgazzoli.karaf.common.CombinedClassLoader;
import com.github.lburgazzoli.karaf.common.cmd.AbstractServiceCompleter;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.osgi.framework.Bundle;

import java.util.List;

/**
 *
 */
public class ClassLoaderClassListCompleter extends AbstractServiceCompleter<CombinedClassLoader> {
    @Override
    public int complete(String buffer, int cursor, List<String> candidates) {
        StringsCompleter delegate = new StringsCompleter();

        for(Bundle b : getService().getBundles()) {
            delegate.getStrings().add(b.getSymbolicName());
        }

        return delegate.complete(buffer, cursor, candidates);
    }
}
