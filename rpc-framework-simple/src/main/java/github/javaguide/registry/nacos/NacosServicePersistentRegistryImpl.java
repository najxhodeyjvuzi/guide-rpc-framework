package github.javaguide.registry.nacos;

import github.javaguide.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Nacos service registry implementation for persistent registry.
 *
 * @author najxhodeyjvuzi
 * @version 1.0
 * @since 2024/08/12
 */
@Slf4j
public class NacosServicePersistentRegistryImpl implements ServiceRegistry {

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
//        String servicePath = rpcServiceName + inetSocketAddress.toString();
        return;
    }


}
