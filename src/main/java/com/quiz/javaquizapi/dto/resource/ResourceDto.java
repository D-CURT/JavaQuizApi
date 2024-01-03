package com.quiz.javaquizapi.dto.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.dto.MeDto;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Me(Resource.class)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResourceDto extends MeDto {
    private String resourceId;
    private ResourceStatus status;
    private String folderCode;
    private String createdByCode;
    private String approvedByCode;

    @Override
    public ResourceDto nullify() {
        super.nullify();
        return setResourceId(null);
    }
}
