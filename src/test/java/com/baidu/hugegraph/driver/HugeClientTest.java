package com.baidu.hugegraph.driver;

import com.baidu.hugegraph.client.UserAccessContext;
import com.baidu.hugegraph.structure.gremlin.Result;
import com.baidu.hugegraph.structure.gremlin.ResultSet;
import org.junit.Test;

import java.util.Iterator;

/**
 *  @author: liyu04
 *  @date: 2020/8/19
 *  @version: V1.0
 *
 * @Description:
 */
public class HugeClientTest
{

    @Test
    public void main()
    {
        HugeClient client = UserAccessContext.hugeClient("http://10.95.54" +
                        ".196:38080", "gtest1",
                "work", "j9NWkTXXQxwaoLz" +
                        "+x1T853WHKX5DlOss27tkHXNY1JS+OMvtGY4cQIzea6ePrdbZ6Lm9Ql0" +
                        "/nWcnvj93AzakuI0ny1zMdGslWOQ4XKkfVQ8YLWisOQUxS46xN" +
                        "/wQRhQ1qnpLMAa4uhBRQM/pkJ14qiubFMPjxvVozGHpXpRBiJY=");
        ResultSet rs =
                client.gremlin().gremlin("gtest1 .traversal().V()").execute();
        Iterator<Result> iterator = rs.iterator();
        while (iterator.hasNext()) {
            Result next = iterator.next();
            System.out.println(next.getString());
        }
    }
}