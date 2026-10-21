# TP 4 — Hibernate et persistance des données

Projet indépendant hibernate1, Java 17, javax.persistence, Hibernate 5.6.15.Final, MySQL Connector 8.0.33, JUnit 5.9.2. Aucun Spring.

## Lancement

Ouvrir pom.xml dans IntelliJ IDEA et sélectionner JDK 17 (ms-17). Exécuter test.MainTest avec le dossier du projet comme répertoire de travail. Une configuration MainTest est fournie.

MySQL local doit être démarré sur 3306. La base db_tp_hibernate est créée à la connexion ; Hibernate crée/met à jour salles et machines. Le mot de passe local se trouve dans hibernate-local.properties, ignoré par Git, ou peut être fourni via TP_MYSQL_PASSWORD. hibernate.cfg.xml contient les autres réglages.

Depuis la racine :
~~~powershell
.\mvn17.ps1 clean test
.\mvn17.ps1 compile exec:java
.\mvn17.ps1 package
~~~
Le script utilise le Java 17 et le Maven IntelliJ installés sur cette machine. Les équivalents sont mvn clean test, mvn compile exec:java et mvn package avec JAVA_HOME et PATH configurés.

## Les onze étapes

1. pom.xml : versions demandées, compiler Java 17, Surefire et moteur JUnit.
2. dao.IDao<T> : create, delete, update, getById, getAll, signatures exactes.
3. entities.Salle et entities.Machine : annotations JPA, identifiants int, cascade ALL et collection LAZY côté Salle, ManyToOne côté Machine.
4. src/main/resources/hibernate.cfg.xml : connexion MySQL, MySQL8Dialect, update et mappings.
5. util.HibernateUtil : SessionFactory unique et fermeture explicite.
6. dao.SalleDao et dao.MachineDao : implémentations de IDao via AbstractDao, qui mutualise le code transactionnel.
7. services.SalleService et services.MachineService : implémentent IDao et délèguent aux DAO.
8. test.MainTest : salle S1 et machine M-101 / Dell, lecture des données. Les relances séquentielles réutilisent ces données.
9. SalleServiceTest et MachineServiceTest : 17 tests CRUD, relations, cascade, erreurs et recherche.
10. MachineDao.findByMarque : HQL typé et paramétré, exposé par MachineService.
11. Transactions : rollback dans le bloc où la session est encore ouverte ; fermeture ensuite. Les écritures renvoient false en cas d'erreur comme le contrat fourni, avec journalisation.

## Adaptations techniques

- Le moteur junit-jupiter-engine complète junit-jupiter-api pour que Maven exécute réellement les tests.
- Le connecteur demandé mysql:mysql-connector-java:8.0.33 peut afficher un avertissement Maven de relocation vers com.mysql:mysql-connector-j : la version reste 8.0.33.
- createDatabaseIfNotExist=true permet la création de la base dédiée. allowPublicKeyRetrieval=true est utilisé pour l'authentification MySQL 8 sur localhost avec la connexion sans SSL fournie par l'énoncé ; cette configuration vise le TP local.
- Le rollback du code fourni était situé après la fermeture automatique de session. Il est déplacé à l'intérieur du try-with-resources.
- La liste Salle.machines reste LAZY. getAvecMachines utilise un LEFT JOIN FETCH explicite pour lire la collection après fermeture de session, sans changer le mapping.
- Les tests utilisent exclusivement db_tp_hibernate_test, avec create-drop. Une extension JUnit configure aussi cette isolation lors d'un lancement direct dans IntelliJ, et ferme la factory en fin de suite.
- Les tests provoquent volontairement une référence de plus de 255 caractères pour vérifier le rollback. Les messages SQL associés sont attendus.
- Pas de contrainte NOT NULL ou d'unicité ajoutée au modèle au-delà de l'énoncé.

## Captures utiles

1. Arborescence des packages et hibernate.cfg.xml.
2. Console MainTest : salle S1, machine M-101 Dell et nombre de machines.
3. MySQL : tables salles et machines, leurs lignes et la clé étrangère salle_id.
4. Recherche HQL par marque dans MachineDao.
5. Maven : Tests run: 17, Failures: 0, Errors: 0 et BUILD SUCCESS.
6. Test de rollback de création en cascade et ses assertions.

Les bases des précédents TP ne sont pas utilisées.
