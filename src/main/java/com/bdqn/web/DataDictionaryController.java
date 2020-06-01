package com.bdqn.web;

import com.bdqn.entity.DataDictionary;
import com.bdqn.service.DataDictionaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/dictionaryCon")
public class DataDictionaryController {

    @Resource
    private DataDictionaryService dataDictionaryService;


    @GetMapping("/datadictionarylist")
    public List<DataDictionary> datadictionarylist(String typecode) {

        return dataDictionaryService.queryByCode(typecode);
    }
}
