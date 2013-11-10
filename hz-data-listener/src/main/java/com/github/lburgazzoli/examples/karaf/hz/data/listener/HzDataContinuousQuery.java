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
package com.github.lburgazzoli.examples.karaf.hz.data.listener;

import com.github.lburgazzoli.examples.karaf.hz.IHazelcastInstanceProvider;
import com.github.lburgazzoli.examples.karaf.hz.data.HzData;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.SqlPredicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class HzDataContinuousQuery implements EntryListener<String,HzData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HzDataContinuousQuery.class);

    private final IHazelcastInstanceProvider m_instanceProvider;
    private String m_queryKey;
    private IMap<String,HzData> m_map;

    /**
     * c-tor
     */
    public HzDataContinuousQuery(IHazelcastInstanceProvider instanceProvider) {
        m_instanceProvider = instanceProvider;
        m_queryKey = null;
    }

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public void init() {
        m_map      = m_instanceProvider.getInstance().getMap("hztest:map");
        m_queryKey = m_map.addEntryListener(this,new SqlPredicate("payload like 'x%'"),null,true);
    }

    /**
     *
     */
    public void destroy() {
        if((m_map != null) && StringUtils.isNotBlank(m_queryKey)) {
            m_map.removeEntryListener(m_queryKey);
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public void entryAdded(EntryEvent<String, HzData> event) {
        LOGGER.info("EntryAdded <{}> = <{}>",event.getKey(),event.getValue());
    }

    @Override
    public void entryRemoved(EntryEvent<String, HzData> event) {
        LOGGER.info("EntryRemoved <{}> = <{}>",event.getKey(),event.getValue());
    }

    @Override
    public void entryUpdated(EntryEvent<String, HzData> event) {
        LOGGER.info("EntryUpdated <{}> = <{}>",event.getKey(),event.getValue());
    }

    @Override
    public void entryEvicted(EntryEvent<String, HzData> event) {
        LOGGER.info("EntryEvicted <{}> = <{}>",event.getKey(),event.getValue());
    }
}
