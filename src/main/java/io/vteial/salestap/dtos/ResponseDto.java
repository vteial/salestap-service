package io.vteial.salestap.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto {

    public static final int UNKNOWN = -1;

    public static final int SUCCESS = 0;

    public static final int ERROR = 1;

    public static final int WARNING = 2;

    public static final int INFO = 3;

    private int type;

    private String message;

    private Object data;

}
