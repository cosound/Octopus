package com.chaos.octopus.integrationtests;

/**
 * Copyright (c) 2014 Chaos ApS. All rights reserved. See LICENSE.TXT for details.
 */
public class TestUtils
{
    public static void waitUntil(Check check) throws InterruptedException
    {
        for(int i = 0; i < 5000; i++)
        {
            if(check.isTrue()) return;

            Thread.sleep(1);
        }
    }
}
