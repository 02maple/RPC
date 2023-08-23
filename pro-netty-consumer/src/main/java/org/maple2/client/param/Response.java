package org.maple2.client.param;

public class Response {
    private Long id;
    private Object result;
    private String status = "00000";// 00000 表示成功  其他表示失败
    private String massage;//返回失败的原因
    public Long getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMassage() {
        return massage;
    }
    public void setMassage(String massage) {
        this.massage = massage;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
}
