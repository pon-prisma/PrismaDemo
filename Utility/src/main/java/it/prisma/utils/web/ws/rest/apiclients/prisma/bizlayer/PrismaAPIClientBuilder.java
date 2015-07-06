//package it.prisma.utils.web.ws.rest.apiclients.prisma.bizlayer;
//
//public abstract class PrismaAPIClientBuilder<R, T extends IPrismaAPIClientBuilder<R, T>> implements IPrismaAPIClientBuilder<R, T> {
//
//    Object config;
//    String endpoint;
//    String token;    
//    
//    @SuppressWarnings("unchecked")
//    @Override
//    public T withConfig(Config config) {
//        this.config = config;
//        return (T) this;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public T credentials(String user, String password) {
//        this.user = user;
//        this.password = password;
//        return (T) this;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public T endpoint(String endpoint) {
//        this.endpoint = endpoint;
//        return (T) this;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public T perspective(Facing perspective) {
//        this.perspective = perspective;
//        return (T) this;
//    }
//    
//    @SuppressWarnings("unchecked")
//    @Override
//    public T useNonStrictSSLClient(boolean useNonStrictSSL) {
//        if (config == null)
//            config = Config.newConfig().withSSLVerificationDisabled();
//        return (T) this;
//    }
//    
//    public static class ClientV1 extends PrismaAPIClientBuilder<OSClient, IOSClientBuilder.V2> implements IOSClientBuilder.V2 {
//
//        String tenantName;
//        String tenantId;
//        
//        @Override
//        public ClientV2 tenantName(String tenantName) {
//            this.tenantName = tenantName;
//            return this;
//        }
//
//        @Override
//        public ClientV2 tenantId(String tenantId) {
//            this.tenantId = tenantId;
//            return this;
//        }
//        
//        @Override
//        public OSClient authenticate() throws AuthenticationException {
//            return OSAuthenticator.invoke(new Credentials(user, password, tenantName, tenantId), endpoint, perspective, config);
//        }
//    }
//    
//}
