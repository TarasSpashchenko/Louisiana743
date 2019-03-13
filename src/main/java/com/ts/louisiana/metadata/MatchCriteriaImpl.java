package com.ts.louisiana.metadata;

import com.ts.louisiana.metadata.api.MatchCriteria;

public class MatchCriteriaImpl implements MatchCriteria {
    private String criteriaDetails;

    public void setCriteriaDetails(String criteriaDetails) {
        this.criteriaDetails = criteriaDetails;
    }

    @Override
    public String getCriteriaDetails() {
        return criteriaDetails;
    }
}
