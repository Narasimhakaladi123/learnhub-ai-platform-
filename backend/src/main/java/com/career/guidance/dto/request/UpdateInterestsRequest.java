package com.career.guidance.dto.request;

import com.career.guidance.entity.enums.InterestArea;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class UpdateInterestsRequest {

    @NotEmpty(message = "At least one interest must be selected")
    private List<InterestArea> interests;

    public UpdateInterestsRequest() {
    }

    public UpdateInterestsRequest(List<InterestArea> interests) {
        this.interests = interests;
    }

    public List<InterestArea> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestArea> interests) {
        this.interests = interests;
    }
}
