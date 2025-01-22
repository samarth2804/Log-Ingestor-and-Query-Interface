package com.sam.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogDto {
    @NotNull(message = "Level cannot be null") String level;
    @NotNull(message = "Message cannot be null") String message;
    @NotNull(message = "Resource ID cannot be null") String resourceId;
    @NotNull(message = "Timestamp cannot be null") ZonedDateTime timestamp;
    @NotNull(message = "Span ID cannot be null") String spanId;
    @NotNull(message = "Commit cannot be null") String commit;
    @NotNull(message = "Metadata cannot be null") MetaDataDto metadata;
}
