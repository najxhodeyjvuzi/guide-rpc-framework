package github.javaguide.registry.nacos.util;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import github.javaguide.enums.RpcConfigEnum;
import github.javaguide.utils.PropertiesFileUtil;

import java.util.Properties;

public class NamingUtils {

    public static final String NACOS_REGISTER_GROUP_PATH = "/nacos/rpc";

    private static final String DEFAULT_NACOS_ADDRESS = "127.0.0.1:8848";

    private static NamingService nacosNamingService;

    private NamingUtils() {
    }

    public static NamingService getNacosNamingService() {
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String nacosAddress = properties != null && properties.getProperty(RpcConfigEnum.NACOS_ADDRESS.getPropertyValue()) != null ? properties.getProperty(RpcConfigEnum.NACOS_ADDRESS.getPropertyValue()) : DEFAULT_NACOS_ADDRESS;



        if (nacosNamingService != null && nacosNamingService.getServerStatus().equals("UP")) {
            return nacosNamingService;
        }

        try {
            nacosNamingService = NacosFactory.createNamingService(nacosAddress);
        } catch (NacosException e) {
            e.printStackTrace();
        }


        return null;
    }


}
