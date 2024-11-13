package io.giovannymassuia.disecomm.productcatalog.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAnalyticsRepository extends MongoRepository<ProductAnalytics, String> {

}
