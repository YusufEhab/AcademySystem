package com.iacademy.AcademySystem.Service;

import com.iacademy.AcademySystem.model.PageResult;

import java.util.List;
import java.util.Map;

public interface CrudService<Dto, ID> {

    ID create(Dto entity);

    Dto getById(ID id);

    void update(ID id, Dto newEntity);

    void delete(ID id);

    List<Dto> getAll();


    PageResult<Dto> search(Map<String, String> params);

}
