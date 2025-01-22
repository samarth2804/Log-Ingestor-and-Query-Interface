package com.sam.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FilterDto {
    String level;
    String message;
    String resourceId;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
    String spanId;
    String commit;
    MetaDataDto metadata;

    @JsonIgnore
    @AssertTrue(message = "Either both or neither start and end time must be present and start must be before end")
    public boolean isBothStartAndEndPresent() {
        if(startTime == null && endTime == null) {
            return true;
        } else if(startTime == null || endTime == null) {
            return false;
        }
        return !startTime.isAfter(endTime);
    }
}


