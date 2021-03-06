/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.store.hdfs.fs;

import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryScope;
import org.apache.hadoop.fs.permission.AclEntryType;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FsPermissionHelper {

    public static final FsPermission permission770
            = new FsPermission(FsAction.ALL, FsAction.ALL, FsAction.NONE);

    private FsPermissionHelper() {
    }

    public static List<AclEntry> getDefaultAclsForTechnicalUsers(List<String> users, FsAction fsAction) {
        List<AclEntry> acls = users.stream().map(
                name -> getAcl(AclEntryScope.DEFAULT, fsAction, AclEntryType.USER, name)
        ).collect(toList());
        acls.add(getAcl(AclEntryScope.DEFAULT, FsAction.ALL, AclEntryType.GROUP));
        acls.add(getAcl(AclEntryScope.DEFAULT, FsAction.ALL, AclEntryType.MASK));
        return acls;
    }

    public static List<AclEntry> getAclsForTechnicalUsers(List<String> users, FsAction fsAction) {
        List<AclEntry> acls = users.stream().map(
                name -> getAcl(AclEntryScope.ACCESS, fsAction, AclEntryType.USER, name)
        ).collect(toList());
        acls.add(getAcl(AclEntryScope.ACCESS, FsAction.ALL, AclEntryType.GROUP));
        acls.add(getAcl(AclEntryScope.ACCESS, FsAction.ALL, AclEntryType.MASK));
        return acls;
    }

    private static AclEntry getAcl(AclEntryScope entryScope, FsAction action, AclEntryType entryType, String name) {
        return getAclBuilder(entryScope, action, entryType)
                .setName(name)
                .build();
    }

    private static AclEntry getAcl(AclEntryScope entryScope, FsAction action, AclEntryType entryType) {
        return getAclBuilder(entryScope, action, entryType)
                .build();
    }

    private static AclEntry.Builder getAclBuilder(AclEntryScope entryScope, FsAction action, AclEntryType entryType) {
        return new AclEntry.Builder()
                .setScope(entryScope)
                .setPermission(action)
                .setType(entryType);
    }
}
