package io.vteial.workbench.controllers;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import io.quarkus.panache.common.Sort;
import io.vteial.workbench.data.ProductRepository;

import io.vteial.workbench.models.Product;

@Slf4j
@Path("api/products")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class ProductController {

    @Inject
    ProductRepository productRepository;

    @GET
    public List<Product> list() {
        log.debug("Product Count : {}", productRepository.count());
        return productRepository.listAll(Sort.by("name"));
    }

    @POST
    @Transactional
    public Response add(Product item) {
        if (item.getId() != null) {
            throw new WebApplicationException("Id should not set on request.", 422);
        }
        productRepository.persist(item);
        return Response.ok(item).status(201).build();
    }

    @GET
    @Path("{id}")
    public Product findById(@PathParam("id") Long id) {
        Product item = productRepository.findById(id);
        if( item == null) {
            throw new WebApplicationException("Product with id of " + id + " does not exists", 404);
        }
        return item;
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response set(@PathParam("id") Long id, Product item) {
        Product eitem = productRepository.findById(id);
        if( eitem == null) {
            throw new WebApplicationException("Product with id of " + id + " does not exists", 404);
        }
        eitem.setCode(item.getCode());
        eitem.setCode(item.getCode());
        eitem.setName(item.getName());
        eitem.setDesc(item.getDesc());
        eitem.setUnit(item.getUnit());
        eitem.setRate(item.getRate());
        return Response.ok(eitem).status(204).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Long id) {
        Product eitem = productRepository.findById(id);
        if( eitem == null) {
            throw new WebApplicationException("Product with id of " + id + " does not exists", 404);
        }
        productRepository.delete(eitem);
        return Response.status(204).build();
    }
}