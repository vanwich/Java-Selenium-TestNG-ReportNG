/*
 * Copyright © 2019 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package help.config;

import com.vimalselvam.testng.SystemInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgu2 on 11/25/2019.
 */
public class MySystemInfo implements SystemInfo {

    public Map<String, String> getSystemInfo() {

        Map<String, String> systemInfo = new HashMap<String, String>();
        systemInfo.put("测试人员", "vanwich");

        return systemInfo;
    }

}
