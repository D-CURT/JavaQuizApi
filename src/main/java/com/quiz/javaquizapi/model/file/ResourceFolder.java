package com.quiz.javaquizapi.model.file;

import com.quiz.javaquizapi.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "resource_folders")
public class ResourceFolder extends BaseEntity {
    public static final String ROOT_PATH = StringUtils.EMPTY;

    @NotNull
    private String name;

    @NotNull
    private String path;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ResourceFolder parent;

    public ResourceFolder setName(String name) {
        this.name = name;
        return setPath(name);
    }

    public ResourceFolder setPath(String path) {
        this.path = parent == null || StringUtils.isBlank(parent.getPath())
                ? path
                : String.join(".", parent.getPath(), path);
        return this;
    }

    public ResourceFolder setParent(ResourceFolder parent) {
        this.parent = parent;
        return setName(name);
    }
}
