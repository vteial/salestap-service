package io.vteial.salestap.services.impl;

import io.vteial.salestap.models.Shop;
import io.vteial.salestap.repos.ShopRepository;
import io.vteial.salestap.services.ShopService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;

@Slf4j
@ApplicationScoped
public class DefaultShopService implements ShopService {

    @Inject
    ShopRepository shopRepository;

    @Override
    public Shop findById(Long id) {
        return shopRepository.findById(id);
    }

    @Override
    public Shop findByCode(String code) {
        return shopRepository.findByCode(code);
    }

    @Override
    public Shop create(Shop item) {
        item.prePersist(null, new Date());
        item.correctData();
        shopRepository.persist(item);
        return item;
    }

    @Override
    public Shop update(Shop item) {
        Shop eitem = shopRepository.findById(item.getId());
        // ToDo: copy relevant fields from item to eitem;
        return eitem;
    }

//    @Override
//    public void delete(Long id) {
//        userRepository.deleteById(id);
//    }
}
