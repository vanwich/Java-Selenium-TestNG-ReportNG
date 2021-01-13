package com.test;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import rest.service.policy.context.DXPQuickquoteAutoContext;

public class TestRest implements DXPQuickquoteAutoContext {

    @Test
    public void testRest(){
        Map<String, String> map = new HashMap<>();
        map.put("vehIdentificationNo", "01010400");
        context.getVehicles(map);
    }
}
