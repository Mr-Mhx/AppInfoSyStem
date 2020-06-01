package com.bdqn.service;

import com.bdqn.entity.DataDictionary;

import java.util.List;

public interface DataDictionaryService {

    public DataDictionary queryByCodeAndId(String typecode, Long valueid);
    public List<DataDictionary> queryByCode(String typecode);
}
