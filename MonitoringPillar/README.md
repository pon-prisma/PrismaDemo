MODIFICHE AL PROGETTO
-----------------------
E' presente un WS REST all'indirizzo /rest-task/REST/rest-task.

Il WS istanzia un processo jBPM asincrono che funziona tramite l'AsyncWorkItemHandler di jBPM, fornendo a tale strumento il nome del comando da eseguire (in questo processo il ConsumeRestWSCommand).
Viene utilizzato in automatico l'AsyncWIH se si mette come nome del task 'tsk:name="async"'.
Si sta provando un CustomAsyncWIH che dovrebbe modificare il tipo di esecuzione (sincrona o asincrona) a seconda di un flag DisableAsync passato come parametro all'istanziazione del processo. In tal caso non verrebbe chiamato l'executor ma eseguito subito il comando ( ! NON ANCORA FUNZIONANTE !). Per usare questo handler predefinito bisogna utilizzare il nome del task 'tsk:name="customAsync"'

I componenti principali sono:
- WS Rest che avvia il processo
- CustomApplicationScopedProducer che configura l'ambiente Runtime di jBPM
- RestTaskWorkItemHandlersProducer che (iniettato nel CustomApplicationScopedProducer) si occupa di caricare ogni volta il mapping dei WIH con il processo caricato. Serve utilizzarlo per via dell'esecuzione con l'ExecutorService, dato che quando riprende l'esecuzione di un task successivo perderebbe il mapping con i WIH.
- RestProcessBean che si occupa di istanziare un runtime jBPM ed il processo com.sample.rest-task con i relativi parametri (chiamato dal Rest WS)
- rest-task.bpmn che contiene la definizione BPMN del processo. In particolare un task con nome 'customAsync' e parametro CommandClass per specificare l'utilizzo del comando ConsumeRestWSCommand con il mapping dei relativi parametri (Url, Metodo Rest). C'è poi un task di callback per sperimentare le funzionalità di callback implementabili, per ora non ancora sfruttato a dovere.
- ConsumeRestWSCommand che è il comando che consuma il servizio Rest (per ora sfruttando il RestWIH predefinito ingannandolo con un trucchetto - che non dovrebbe introdurre alcun bug).

! LIBRERIE e JBOSS AS
------------------------
RestEasy v.3.0.7:
	- copiare librerie nella cartella 'modules' dell'AS (http://sourceforge.net/projects/resteasy/files/Resteasy%20JAX-RS/3.0.7.Final/resteasy-jaxrs-3.0.7.Final-all.zip/download - dentro allo zip c'è un altro zip 'resteasy-jboss-modules' che contiene tali librerie).
	- aggiornare le librerie situate in 'modules/org/apache/httpcomponents/main' alla versione 4.2.1 (le librerie sono httpcore, httpmime e httpcore - ricordarsi di modificare il file 'module.xml' con i dati delle nuove versioni).
		

! FIX ECLIPSE
----------------
-	Per errori dell'editor disabilitare i vari errori nelle categorie errors/warnings (es Properties>JPA)


! DEPLOY SUL SERVER
----------------------

- Creare un DB Mysql con nome 'jbpmRestDB' accessibile dall'utente jbpm/jbpm

- La datasource è già configurata nel file WEB-INF/jbpm-ds.xml, è sufficiente posizionare il driver mysql-connector-java.jar (testato fino alla versione 5.1.24) all'interno della cartella deployments dell'AS.

Effettuare il deployment del war su JBoss AS 7.1.1