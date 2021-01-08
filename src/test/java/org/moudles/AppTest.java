package org.moudles;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    public static final int INT = 60;
    private static long aaa = (currentTimeMillis() + 5 * INT) / 1000;

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
        System.out.println("当前时间为：" + currentTimeMillis());

    }
}
