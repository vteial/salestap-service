package io.vteial.salestap.dtos;

import io.vteial.salestap.models.Shop;
import io.vteial.salestap.models.User;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
public class SetUpDto {

    public static String STATE_KEY = "setUpState";

    public static String STATE_NEW = "New";

    public static String STATE_IN_PROGRESS = "In Progress";

    public static String STATE_COMPLETED = "Completed";

    public static String STEP_REGISTER_OWNER = "register-owner";

    public static String STEP_CREATE_SHOP = "create-shop";

    public static String STEP_SUMMARY = "summary";

    private String state;

    private Map<String, Boolean> steps;

    private boolean termsAndConditions;

    private User owner;

    private Shop shop;

    public void initSteps() {
        steps = new HashMap<>();
        steps.put(STEP_REGISTER_OWNER, false);
        steps.put(STEP_CREATE_SHOP, false);
        steps.put(STEP_SUMMARY, false);
    }

}
