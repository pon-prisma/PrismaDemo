/**
 * This package contains the configuration classes for several cross-cutting concerns of RESTful Web Services.
 * <br/>
 * The following configuration are contained:
 * <ul>
 * <li>Prisma responses enveloping, using {@link it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper}</li>
 * <li>Exception handling, and related transformation into {@link it.prisma.domain.dsl.prisma.prismaprotocol.Error}</li>
 * <li>Producers for {@link it.prisma.domain.dsl.prisma.prismaprotocol.restwsrequests.RestWSParamsContainer}</li>
 * </ul>
 * 
 * @author l.biava
 *
 */
package it.prisma.businesslayer.bizws.config;