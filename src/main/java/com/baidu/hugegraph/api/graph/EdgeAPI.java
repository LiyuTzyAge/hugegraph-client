/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.baidu.hugegraph.api.graph;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;

import com.baidu.hugegraph.client.RestClient;
import com.baidu.hugegraph.client.RestResult;
import com.baidu.hugegraph.exception.NotAllCreatedException;
import com.baidu.hugegraph.structure.constant.Direction;
import com.baidu.hugegraph.structure.constant.HugeType;
import com.baidu.hugegraph.structure.graph.Edge;
import com.google.common.collect.ImmutableMap;

public class EdgeAPI extends GraphAPI {

    public EdgeAPI(RestClient client, String graph) {
        super(client, graph);
    }

    @Override
    protected String type() {
        return HugeType.EDGE.string();
    }

    public Edge create(Edge edge) {
        RestResult result = this.client.post(this.path(), edge);
        return result.readObject(Edge.class);
    }

    public List<String> create(List<Edge> edges, boolean checkVertex) {
        MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("Content-Encoding", BATCH_ENCODING);
        Map<String, Object> params = ImmutableMap.of("checkVertex",
                                                     checkVertex);
        RestResult result = this.client.post(this.batchPath(), edges,
                                             headers, params);
        List<String> ids = result.readList(String.class);
        if (edges.size() != ids.size()) {
            throw new NotAllCreatedException(
                      "Not all edges are successfully created, " +
                      "expect '%s', the actual is '%s'",
                      ids, edges.size(), ids.size());
        }
        return ids;
    }

    public Edge append(Edge edge) {
        String path = RestClient.buildPath(this.path(), edge.id());
        Map<String, Object> params = ImmutableMap.of("action", "append");
        RestResult result = this.client.put(path, edge, params);
        return result.readObject(Edge.class);
    }

    public Edge eliminate(Edge edge) {
        String path = RestClient.buildPath(this.path(), edge.id());
        Map<String, Object> params = ImmutableMap.of("action", "eliminate");
        RestResult result = this.client.put(path, edge, params);
        return result.readObject(Edge.class);
    }

    public Edge get(String id) {
        RestResult result = this.client.get(this.path(), id);
        return result.readObject(Edge.class);
    }

    public List<Edge> list(int limit) {
        return this.list(null, null, null, null, limit);
    }

    public List<Edge> list(String vertexId,
                           Direction direction,
                           String label,
                           Map<String, Object> properties,
                           int limit) {
        String props = GraphAPI.formatProperties(properties);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("vertex_id", vertexId);
        params.put("direction", direction);
        params.put("label", label);
        params.put("properties", props);
        params.put("limit", limit);
        RestResult result = this.client.get(this.path(), params);
        return result.readList(this.type(), Edge.class);
    }

    public void delete(String id) {
        this.client.delete(this.path(), id);
    }
}
