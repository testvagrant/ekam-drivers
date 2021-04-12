package com.testvagrant.optimus.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class TestFeedDeviceFilter {
    private List<String> include = new ArrayList<>();
    private List<String> exclude = new ArrayList<>();
    private String operator = TestFeedDeviceFilterOperators.EQ.getOperator();

    public boolean isEmpty() {
        return this.getInclude().isEmpty() && this.getExclude().isEmpty();
    }
}
