Configurazioni dell'ambiente
====


1. Importazione delle CA Affidabili nel truststore di sistema (della JVM in uso):
----

Per ogni connessione che fa uso di certificati non rilasciati da CA pubblicamente attendibili, è necessario utilizzare il seguente comando per aggiungere il relativo certificato a quelli affidabili:

<pre>keytool -import -file <i>&lt;cacert.crt&gt;</i> -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts</pre>

**NOTE:**

- La password di default è `changeit`
- Se la variabile d'ambiente JAVA_HOME non è settata, introdurre il percorso manualmente (di solito tale installazione si trova in `/usr/lib/jvm`).

2. Configurazione delle proprietà dell'ambiente ed hostname
----
E' necessario che sia presente un'apposita cartella dentro a `var-configs-profiles` che contiene i file dell'ambiente.
Inoltre, è necessario configurare il server nel quale effettuare il deploy con un hostname FQDN che contiene il nome del profilo.

Esempio:
	- INFN server:
		- hostname: `xxx.**infn**.yyy`
		- properties folder: `var-configs-profiles/**infn**`

3. LIBRERIE e JBOSS AS
----

E' necessario aggiungere/sostituire le seguenti librerie alla versione base di JBoss AS 7.1.1.Final.

- **RestEasy v.3.0.7:**

    - copiare librerie nella cartella `modules` dell'AS (http://sourceforge.net/projects/resteasy/files/Resteasy%20JAX-RS/3.0.7.Final/resteasy-jaxrs-3.0.7.Final-all.zip/download - dentro allo zip c'è un altro zip 'resteasy-jboss-modules' che contiene tali librerie).
	- aggiornare le librerie situate in `modules/org/apache/httpcomponents/main` alla versione 4.2.1 (le librerie sono httpcore, httpmime e httpcore - ricordarsi di modificare il file 'module.xml' con i dati delle nuove versioni).

