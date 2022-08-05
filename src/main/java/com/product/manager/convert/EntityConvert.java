package com.product.manager.convert;

import java.util.List;

public interface EntityConvert<E, D> {
    E convertDTOToEntity(D dto);
    List<E> convertDTOsToEntities(List<D> dtos);
    D convertEntityToDTO(E entity);
    List<D> convertEntitiesToDTOs(List<E> entities);
}
