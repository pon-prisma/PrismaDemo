La persistenza può essere configurata in due modi:

1) Utilizzando le configurazioni di default delle JPA:
		
		Creando un apposito EJB che produce le Datasources (vedi xxxDataSourcesProvider.java).
		NOTA: I nomi JNDI sono stati modificati per adattarsi alla configurazione di JBoss, è possibile ripristinarli se non si utilizza la seconda configurazione (da "java:app/" a "java:jboss/datasources/").
		NOTA2: Tali Datasources sono già esistenti, è sufficiente decommentarne AL MASSIMO una per le specifiche configurazioni.

2) Utilizzando le facilitazioni di JBoss AS 7:
		
		Creando un file xml in 'webapp\WEB-INF\' contenente le configurazioni per le datasources che verranno instanziate dall'AS.
		Inoltre è necessario posizionare il driver mysql-connector-java.jar (testato fino alla versione 5.1.29) all'interno della cartella deployments dell'AS.
		NOTA: Tale file è già presente nella cartella ('xa-orchestrator-ds.xml').

!! ATTENZIONE !!
E' preferibile utilizzare il secondo modo, in quanto permette di configurare, senza ricompilare il progetto, i dati di connessione ai DB.