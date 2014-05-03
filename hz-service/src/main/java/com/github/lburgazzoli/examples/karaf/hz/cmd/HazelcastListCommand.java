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

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.Partition;
import org.apache.commons.lang3.StringUtils;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.table.ShellTable;

/**
 *
 */
@Command(scope = "hz", name = "list", description = "Lists")
public class HazelcastListCommand extends AbstractHazelcastCommand {

    @Argument(
        index       = 0,
        name        = "arg",
        description = "The command argument",
        required    = true,
        multiValued = false)
    String arg = null;

    // *************************************************************************
    //
    // *************************************************************************

    @Override
    public void execute() throws Exception {
        if(StringUtils.isNotBlank(arg)) {
            if(StringUtils.equalsIgnoreCase(HazelcastCommandConstants.LIST_MEMBERS,arg)) {
                doExecuteMemberList(getService().getInstance());
            } else if(StringUtils.equalsIgnoreCase(HazelcastCommandConstants.LIST_OBJECTS,arg)) {
                doExecuteDistributedObjectList(getService().getInstance());
            } else if(StringUtils.equalsIgnoreCase(HazelcastCommandConstants.LIST_PARTITIONS,arg)) {
                doExecutePartitionList(getService().getInstance());
            }
        }
    }

    // *************************************************************************
    //
    // *************************************************************************

    /**
     *
     * @param instance
     * @throws Exception
     */
    public void doExecuteMemberList(HazelcastInstance instance) throws Exception {

        ShellTable table = new ShellTable();
        table.column("ID");
        table.column("Host");
        table.column("Port");
        table.column("Local");;

        for(Member m : instance.getCluster().getMembers()) {
            table.addRow().addContent(
                m.getUuid(),
                m.getSocketAddress().getHostName(),
                m.getSocketAddress().getPort(),
                m.localMember() ? "*" : StringUtils.EMPTY
            );
        }

        table.print(System.out);
    }

    /**
     *
     * @param instance
     * @throws Exception
     */
    public void doExecuteDistributedObjectList(HazelcastInstance instance) throws Exception {
        ShellTable table = new ShellTable();
        table.column("Name");
        table.column("Class");

        for(DistributedObject o : instance.getDistributedObjects()) {
            table.addRow().addContent(
                o.getName(),
                o.getClass()
            );
        }

        table.print(System.out);
    }

    /**
     *
     * @param instance
     * @throws Exception
     */
    public void doExecutePartitionList(HazelcastInstance instance) throws Exception {
        ShellTable table = new ShellTable();
        table.column("ID");
        table.column("Member");

        for(Partition p : instance.getPartitionService().getPartitions()) {
            table.addRow().addContent(
                p.getPartitionId(),
                p.getOwner().getUuid()
            );
        }

        table.print(System.out);
    }
}
