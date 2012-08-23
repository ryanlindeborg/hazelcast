/*
 * Copyright (c) 2008-2012, Hazel Bilisim Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.cluster;

import com.hazelcast.impl.Node;
import com.hazelcast.impl.spi.AbstractOperation;
import com.hazelcast.impl.spi.NonBlockingOperation;
import com.hazelcast.impl.spi.NonMemberOperation;
import com.hazelcast.impl.spi.Response;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @mdogan 8/2/12
 */
public class JoinCheck extends AbstractOperation implements NonBlockingOperation, NonMemberOperation {

    private JoinInfo joinInfo;

    public JoinCheck() {
    }

    public JoinCheck(final JoinInfo joinInfo) {
        this.joinInfo = joinInfo;
    }

    public void run() {
        System.out.println("RUNNING CHECK !!!!! -> " + joinInfo);
        Node node = getNodeService().getNode();
        boolean ok = false;
        if (joinInfo != null && node.joined() && node.isActive()) {
            try {
                ok = node.validateJoinRequest(joinInfo);
            } catch (Exception ignored) {
            }
        }
        if (ok) {
            getResponseHandler().sendResponse(new Response(node.createJoinInfo()));
        } else {
            getResponseHandler().sendResponse(null);
        }
    }

    @Override
    public void readInternal(final DataInput in) throws IOException {
        super.readInternal(in);
        System.out.println("READING =============");
        joinInfo = new JoinInfo();
        joinInfo.readData(in);
    }

    @Override
    public void writeInternal(final DataOutput out) throws IOException {
        super.writeInternal(out);
        System.out.println("WRITING =============");
        joinInfo.writeData(out);
    }
}

