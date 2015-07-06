//package it.prisma.dal.utils;
//
//import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
//import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
//import org.hibernate.cfg.reveng.TableIdentifier;
//
//public class CustomHibernateStrategy extends
//		DelegatingReverseEngineeringStrategy {
//
//	public CustomHibernateStrategy(ReverseEngineeringStrategy delegate) {
//		super(delegate);
//		//throw new IllegalArgumentException(delegate.toString());
//	}
//
//	@Override
//	public String tableToClassName(TableIdentifier tableIdentifier) {
//		String name = super.tableToClassName(tableIdentifier);
//		try {
//			name=name.substring(0,name.lastIndexOf(".")+1);
//			name+=tableIdentifier.getName();
//			return name;
//		} catch (Exception e) {
//		}
//		return name;
//	}
//
//	@Override
//	public String columnToPropertyName(TableIdentifier table, String column) {
//		if (column.endsWith("ID"))
//			return "Id";
//		else
//			return column;
//	}
//
//}