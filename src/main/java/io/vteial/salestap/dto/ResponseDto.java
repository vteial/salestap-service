package io.vteial.salestap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto {

    public static final int UNKNOWN = -1;

    public static final int SUCCESS = 0;

    public static final int ERROR = 1;

    public static final int WARNING = 2;

    private int type;

    private String message;

    private Object data;

}
