package com.bdqn.utils;

import java.util.HashMap;

public class JsonResult extends HashMap<String, Object> {

    public JsonResult message(String message) {
        this.put("message", message);
        return this;
    }

    public JsonResult (boolean  success) {
        this.put("success", success);
    }


}
