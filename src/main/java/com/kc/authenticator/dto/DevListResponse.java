package com.kc.authenticator.dto;

import com.kc.authenticator.model.Dev;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DevListResponse {
    public Boolean isSuccess = true;
    public Integer count = 0;
    public String message = "";
    public List<Dev> developers;

    public DevListResponse() {
        this.message = "No info of the developers";
        this.isSuccess = true;
    }

    public DevListResponse(List<Dev> developers) {
        this.developers = developers;
        this.count = developers.size();
    }

    public DevListResponse(List<Dev> developers, String message) {
        this.developers = developers;
        this.message = message;
        this.isSuccess = true;
        this.count = developers.size();
    }

    public DevListResponse(List<Dev> developers, String message, Boolean isSuccess) {
        this.developers = developers;
        this.message = message;
        this.isSuccess = isSuccess;
        this.count = developers.size();
    }
}
