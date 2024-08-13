package github.javaguide.performance.service.impl;

import github.javaguide.annotation.RpcService;
import github.javaguide.performance.service.TestService;

@RpcService(group = "test1", version = "version1")
public class TestServiceImpl implements TestService {
    @Override
    public String hello(String testStr) {
        return testStr;
    }
}
