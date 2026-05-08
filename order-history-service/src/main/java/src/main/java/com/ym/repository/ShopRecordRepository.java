package src.main.java.com.ym.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.main.java.com.ym.entity.ShopRecord;

public interface ShopRecordRepository  extends MongoRepository<ShopRecord, String> {
}
