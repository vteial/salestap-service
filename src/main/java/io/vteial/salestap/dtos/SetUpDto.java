package io.vteial.salestap.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SetUpDto {

    public static String STATE_KEY = "setUpState";

    public static String STATE_NEW = "New";

    public static String STATE_IN_PROGRESS = "In Progress";

    public static String STATE_COMPLETED = "Completed";

    String state;

}
