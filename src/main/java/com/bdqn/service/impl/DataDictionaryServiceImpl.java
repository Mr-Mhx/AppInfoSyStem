package com.bdqn.service.impl;

import com.bdqn.entity.DataDictionary;
import com.bdqn.mapper.DataDictionaryMapper;
import com.bdqn.service.DataDictionaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DataDictionaryServiceImpl implements DataDictionaryService {

    @Resource
    private DataDictionaryMapper dataDictionaryMapper;

    /**
     * 01
     * 根据 typeCode 和 valueId 查询单个对象
     * @param typecode
     * @param valueid
     * @return
     */
    @Override
    public DataDictionary queryByCodeAndId(String typecode,Long valueid) {
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypecode(typecode);
        dataDictionary.setValueid(valueid);
        return dataDictionaryMapper.selectOne(dataDictionary);
    }

    /**
     * 02
     * 根据 typeCode 查询
     * @param typecode
     * @return
     */
    public List<DataDictionary> queryByCode(String typecode) {
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setTypecode(typecode);
        return   dataDictionaryMapper.select(dataDictionary);
    }
}
