package io.giovannymassuia.disecomm.ordermanagement.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.Objects;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String sku;

    @Min(0)
    private Integer onHand;

    @UpdateTimestamp(source = SourceType.DB)
    private Timestamp updatedAt;

    public Inventory() {
    }

    public Inventory(String sku, Integer onHand) {
        this.sku = sku;
        this.onHand = onHand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getOnHand() {
        return onHand;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Inventory inventory = (Inventory) o;
        return Objects.equals(sku, inventory.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sku);
    }
}
