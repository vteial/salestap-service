package io.vteial.salestap.services;

import io.vteial.salestap.models.Shop;

public interface ShopService {

    Shop findById(Long id);

    Shop findByCode(String code);

    Shop create(Shop shop);

    Shop update(Shop shop);

//    void delete(Long shopId);
}
