package github.javaguide.performance.server;

import github.javaguide.annotation.RpcScan;
import github.javaguide.remoting.transport.netty.server.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RpcScan(basePackage = {"github.javaguide.performance.service"})
public class TestServer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TestServer.class);
        NettyRpcServer NettyRpcServer = (github.javaguide.remoting.transport.netty.server.NettyRpcServer) ctx.getBean("nettyRpcServer");
        NettyRpcServer.start();
    }
}
