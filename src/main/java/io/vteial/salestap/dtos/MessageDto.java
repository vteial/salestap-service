package io.vteial.salestap.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
public class MessageDto implements Serializable {

    String userId;

    String message;

    Date time;
}
