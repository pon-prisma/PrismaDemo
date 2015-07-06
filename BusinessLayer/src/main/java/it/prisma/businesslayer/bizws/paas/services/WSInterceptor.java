//package it.prisma.businesslayer.bizws.paas.services;
//
//import it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSPaginationParams;
//
//import javax.interceptor.AroundInvoke;
//import javax.interceptor.InvocationContext;
//
//public class WSInterceptor {
//
//	@AroundInvoke
//	public Object intercept(InvocationContext context) throws Exception {
//
//		System.out
//				.println("SimpleInterceptor - Logging BEFORE calling method :"
//						+ context.getMethod().getName());
//
//		Object[] np = new Object[context.getParameters().length+1];
//		int i=0;
//		for(i=0;i<np.length-1;i++)
//		{
//			np[i]=context.getParameters()[i];
//		}
//		np[i]=new RestWSPaginationParams(1L, 100L);
//		
//		for(Object param:context.getParameters())
//		{
//			if(param instanceof RestWSPaginationParams)
//				System.out.println("Param:" +param);
//		}
//		
//		Object result = context.proceed();
//
//		System.out.println("SimpleInterceptor - Logging AFTER calling method :"
//				+ context.getMethod().getName());
//
//		return result;
//	}
//
//}