package com.vranec.view;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TaskViewModel {
    String key;
    String description;
    String assignee;
    Double workload;
}
