package org.maple.client;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {
    private long id = 0;
    private final AtomicLong aid = new AtomicLong(1);
    private Object content;
    private String command;

    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public Object getContent() {
        return content;
    }
    public void setContent(Object content) {
        this.content = content;
    }
    public long getId() {
        return id;
    }
    public ClientRequest(){
        id = aid.incrementAndGet();
    }
}
