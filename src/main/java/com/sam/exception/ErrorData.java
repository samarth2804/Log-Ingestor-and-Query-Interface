package com.sam.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorData {
    private ErrorCode code;
    private String message;
}
