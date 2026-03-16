# Information-Integration-System-Architecture

📌 Descrierea ProiectuluiAcest proiect implementează o Arhitectură de Baze de Date Federate (FDB) și o Arhitectură de Servicii de Date Integrate (DSA). Scopul este oferirea unui acces uniform la un set de surse de date autonome și eterogene.

🛠️ Gestiunea Datelor (Data Engineering Lifecycle)Pentru a simula un scenariu real de integrare, am descompus setul de date IBM HR Analytics din formatul original CSV în patru surse eterogene:SQL (PostgreSQL): Date de identitate și profil ale angajaților.SQL (Oracle): Date financiare și performanță.NoSQL (MongoDB): Feedback calitativ sub formă de documente JSON.Documente (Excel): Nomenclator de referință pentru departamente și locații.

🚀 Tehnologii UtilizateIntegrare Federată: Oracle Database (Database Links, Heterogeneous Services).Procesare: Python (pentru Data Splitting și Ingestion).Orchestrare (viitor): Java Spring Boot & Spring Cloud.
