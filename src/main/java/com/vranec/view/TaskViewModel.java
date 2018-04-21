package com.vranec.view;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TaskViewModel {
    String id;
    String description;
    String assignee;
    String status;
    Double workload;
}
