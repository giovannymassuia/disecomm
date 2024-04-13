package io.giovannymassuia.disecomm.productcatalog.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record ProductAnalytics(String id, int count) {

    public ProductAnalytics withCount(int i) {
        return new ProductAnalytics(id, i);
    }
}
