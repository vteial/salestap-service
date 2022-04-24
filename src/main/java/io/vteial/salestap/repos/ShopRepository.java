package io.vteial.salestap.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vteial.salestap.models.Shop;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShopRepository implements PanacheRepository<Shop>  {

    public Shop findByCode(String code) {
        return this.find("code", code).firstResult();
    }
}
