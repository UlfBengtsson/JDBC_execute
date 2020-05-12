package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AppTest 
{
    @Test
    public void shouldAnswerWithTrue()
    {
        double x = 5.5;
        double y = 4.4;
        assertEquals(x,y, 1.1);
    }
}
