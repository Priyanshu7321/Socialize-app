package com.example.socialize;

public class chat_view_items_class {
    String msg;
    String condition;
    public chat_view_items_class(){

    }
    public chat_view_items_class(String msg,String condition){
        this.msg=msg;
        this.condition=condition;
    }

    public String getMsg() {
        return msg;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
