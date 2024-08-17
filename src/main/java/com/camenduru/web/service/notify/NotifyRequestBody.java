package com.camenduru.web.service.notify;

public class NotifyRequestBody {

    private String jobId;
    private String result;
    private String status;

    public String getJobId() {
        return jobId;
    }

    public void setJobid(String jobId) {
        this.jobId = jobId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
