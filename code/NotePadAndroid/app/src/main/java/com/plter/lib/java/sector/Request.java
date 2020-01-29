package com.plter.lib.java.sector;

import com.plter.lib.java.event.Event;

public class Request extends Event {

    public Request(String name, Object data) {
        init(name, data);
    }

}
